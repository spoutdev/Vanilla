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
package org.spout.vanilla.component.player;

import java.util.Random;

import org.spout.api.chat.style.ChatStyle;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.vanilla.event.player.network.PlayerPingEvent;

public class PingComponent extends EntityComponent {
	private Player player;
	private final Random random = new Random();
	private float timeout = 30;
	private float repeatRate = 4; // sends 4 pings per timeout period
	private float pingTimer = 0;
	private float kickTimer = 0;
	private float ping;
	private float lastRequest;
	private int lastHash;

	@Override
	public void onAttached() {
		if (!(getHolder() instanceof Player)) {
			throw new IllegalStateException("PingComponent may only be attached to a player.");
		}
		player = (Player) getHolder();
	}

	@Override
	public void onTick(float dt) {
		pingTimer += dt;
		kickTimer += dt;
		if (pingTimer > lastRequest + (timeout / repeatRate)) {
			request();
		}
		if (kickTimer > timeout) {
			player.kick(ChatStyle.RED, "Connection timed out!");
		}
	}

	/**
	 * Gets the Player Ping in seconds
	 * @return player ping in seconds
	 */
	public float getPing() {
		return ping;
	}

	/**
	 * Re-sets the Time out times by validating the received hash code
	 * @param hash of the message sent to validate against
	 */
	public void response(int hash) {
		if (hash == lastHash) {
			ping = lastRequest - pingTimer;
			kickTimer = 0;
		}
	}

	/**
	 * Sends a new request to the client to return a ping message.
	 */
	public void request() {
		lastRequest = pingTimer;
		lastHash = random.nextInt(Integer.MAX_VALUE);
		player.getNetworkSynchronizer().callProtocolEvent(new PlayerPingEvent(lastHash));
	}
}
