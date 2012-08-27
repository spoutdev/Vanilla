/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.component.network;

import java.util.Random;
import java.util.logging.Level;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.event.player.network.PlayerKeepAliveEvent;
import org.spout.vanilla.event.player.network.PlayerPingChangedEvent;
import org.spout.vanilla.event.player.network.PlayerUpdateStatsEvent;
import org.spout.vanilla.event.player.network.PlayerUpdateUserListEvent;

/**
 * Updates health, hunger and food saturation states for the player
 */
public class PlayerNetworkComponent extends PositionUpdateComponent<VanillaPlayerController> {
	private static final int MAX_USER_UPDATES = 40; // the maximum rate (in ticks) at which the user list is updated
	private int maxUserListUpdateCounter = MAX_USER_UPDATES;
	private int lastRequestHash = 0; // after log-in an initial keep-alive is sent with hash 0
	private long lastRequestTime = System.currentTimeMillis();
	private long lastResponseTime = System.currentTimeMillis();
	private long ping = 0L;
	private boolean hasTimeOutMsg = true; //temporary to prevent massive kick notify messages
	private Random random = new Random();
	private int oldHealth = Integer.MIN_VALUE, oldHunger = Integer.MIN_VALUE;
	private float oldFoodSat = Float.MIN_VALUE;

	public PlayerNetworkComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		// Update heath, hunger and food saturation
		if (getParent().isSurvival()) {
			int newHealth = getParent().getHealth().getHealth();
			int newHunger = getParent().getSurvivalComponent().getHunger();
			float newFoodSat = getParent().getSurvivalComponent().getFoodSaturation();
			if (oldHealth != newHealth || oldHunger != newHunger || oldFoodSat != newFoodSat) {
				oldHealth = newHealth;
				oldHunger = newHunger;
				oldFoodSat = newFoodSat;
				getParent().getParent().getNetworkSynchronizer().callProtocolEvent(new PlayerUpdateStatsEvent(getParent().getParent()));
			}
		}

		maxUserListUpdateCounter++;
		long time = System.currentTimeMillis();

		// Send a new request message after half the time-out time has passed
		if ((time - lastRequestTime) > (VanillaConfiguration.PLAYER_TIMEOUT_MS.getInt() / 2)) {
			lastRequestHash = random.nextInt();
			lastRequestTime = time;
			getParent().getParent().getNetworkSynchronizer().callProtocolEvent(new PlayerKeepAliveEvent(lastRequestHash));
			return; // Prevent a response check, could be a tick delay
		}

		// Disconnect the player if there has been no response in time
		if ((time - lastResponseTime) > VanillaConfiguration.PLAYER_TIMEOUT_MS.getLong()) {
			if (hasTimeOutMsg) {
				Spout.getLogger().log(Level.WARNING, "PLAYER " + getParent().getParent().getName() + " Reached connection Timeout! (kick suppressed)");
				hasTimeOutMsg = false;
			}
			//getParent().getParent().kick("Connection Timeout!");
		} else {
			hasTimeOutMsg = true;
		}
	}

	/**
	 * Gets the Player Ping in Milliseconds
	 * @return player ping in Milliseconds
	 */
	public long getPing() {
		return ping;
	}

	/**
	 * Re-sets the Time out times by validating the received hash code
	 * @param responseHash to validate against
	 */
	public void resetTimeout(int responseHash) {
		if (responseHash != lastRequestHash) {
			return; // invalid or older message
		}
		this.lastResponseTime = System.currentTimeMillis();
		long oldPing = this.ping;
		this.ping = this.lastResponseTime - this.lastRequestTime;
		if (oldPing != this.ping) {
			Player p = getParent().getParent();
			Spout.getEventManager().callDelayedEvent(new PlayerPingChangedEvent(p, oldPing, this.ping));
			// Update the user lists for all players
			if (maxUserListUpdateCounter >= MAX_USER_UPDATES) {
				maxUserListUpdateCounter = 0;
				for (Player player : Spout.getEngine().getOnlinePlayers()) {
					player.getNetworkSynchronizer().callProtocolEvent(new PlayerUpdateUserListEvent(p, this.ping));
				}
			}
		}
		this.ping = this.lastResponseTime - this.lastRequestTime;
	}
}
