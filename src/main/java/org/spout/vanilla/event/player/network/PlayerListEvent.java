/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.event.player.network;

import org.spout.api.event.HandlerList;
import org.spout.api.event.ProtocolEvent;

public class PlayerListEvent extends ProtocolEvent {
	private static final HandlerList handlers = new HandlerList();
	private final long ping;
	private final boolean online;
	private final String playerDisplayName;

	public PlayerListEvent(String playerDisplayName, long pingDelayMS, boolean online) {
		this.ping = pingDelayMS;
		this.online = online;
		this.playerDisplayName = playerDisplayName;
	}

	/**
	 * Gets the name of the player that this event relates to
	 * @return the player's name
	 */
	public String getPlayerDisplayName() {
		return this.playerDisplayName;
	}

	/**
	 * Gets the player's online status
	 * @return true if the player is online
	 */
	public boolean getOnline() {
		return this.online;
	}

	/**
	 * Gets the network delay between the server and the player
	 * @return true if the player is online
	 */
	public long getPingDelay() {
		return this.ping;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
