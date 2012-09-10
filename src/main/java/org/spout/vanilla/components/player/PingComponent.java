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
package org.spout.vanilla.components.player;

import java.util.Random;
import java.util.logging.Level;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.plugin.Platform;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.event.player.network.PlayerKeepAliveEvent;
import org.spout.vanilla.event.player.network.PlayerPingChangedEvent;
import org.spout.vanilla.event.player.network.PlayerUpdateUserListEvent;

public class PingComponent extends EntityComponent {
	private static final int MAX_USER_UPDATES = 40;
	private int maxUserListUpdateCounter = MAX_USER_UPDATES;
	private long lastRequestTime = System.currentTimeMillis();
	private long lastResponseTime = System.currentTimeMillis();
	private int lastRequestHash = 0;
	private Random random = new Random();
	private long ping = 0L;

	/**
	 * Retrieve the latest random hash sent to the client
	 *
	 * @return The latest random hash.
	 */
	public int getLastRequestHash() {
		return lastRequestHash;
	}

	@Override
	public void onTick(float dt) {
		long time = System.currentTimeMillis();

		// Send a new request message after half the time-out time has passed
		if ((time - lastRequestTime) > (VanillaConfiguration.PLAYER_TIMEOUT_MS.getLong() / 2)) {
			lastRequestHash = random.nextInt();
			getHolder().getNetwork().callProtocolEvent(new PlayerKeepAliveEvent(lastRequestHash));
			lastRequestTime = time;
			return;
		}

		if ((time - lastResponseTime) > VanillaConfiguration.PLAYER_TIMEOUT_MS.getLong()) {
			Spout.getLogger().log(Level.WARNING, "PLAYER " + ((Player) getHolder()).getName() + " Reached connection Timeout!");
			((Player) getHolder()).kick("Ping Timeout!");
		}

	}

	/**
	 * Gets the Player Ping in Milliseconds
	 *
	 * @return player ping in Milliseconds
	 */
	public long getPing() {
		return ping;
	}

	/**
	 * Re-sets the Time out times by validating the received hash code
	 *
	 * @param responseHash to validate against
	 */
	public void resetTimeout(int responseHash) {
		Platform platform = Spout.getPlatform();
		if (!platform.equals(Platform.SERVER) && !platform.equals(Platform.PROXY)) {
			return;
		}

		if (responseHash != lastRequestHash) {
			((Player) getHolder()).kick("Illegal KeepAlive response!");
			return;
		}
		this.lastResponseTime = System.currentTimeMillis();
		long oldPing = this.ping;
		this.ping = this.lastResponseTime - this.lastRequestTime;
		if (oldPing != this.ping) {
			Player p = (Player) getHolder();
			Spout.getEventManager().callDelayedEvent(new PlayerPingChangedEvent(p, oldPing, this.ping));
			// Update the user lists for all players
			if (maxUserListUpdateCounter >= MAX_USER_UPDATES) {
				maxUserListUpdateCounter = 0;
				for (Player player : ((Server) Spout.getEngine()).getOnlinePlayers()) {
					player.getNetworkSynchronizer().callProtocolEvent(new PlayerUpdateUserListEvent(p, this.ping));
				}
			}
		}
	}
}
