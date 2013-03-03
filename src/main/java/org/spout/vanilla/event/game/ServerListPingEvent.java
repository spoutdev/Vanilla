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
package org.spout.vanilla.event.game;

import java.net.InetAddress;

import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;

public class ServerListPingEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	private final InetAddress address;
	private String motd;
	private int numPlayers, maxPlayers;

	public ServerListPingEvent(InetAddress address, String motd, int numPlayers, int maxPlayers) {
		this.address = address;
		this.motd = motd;
		this.numPlayers = numPlayers;
		this.maxPlayers = maxPlayers;
	}

	/**
	 * Gets the address that requested the ping response
	 * @return address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * Gets the message of the day to send.
	 * @return message of the day
	 */
	public String getMotd() {
		return motd;
	}

	/**
	 * Sets the message of the day to send.
	 * @param motd message of the day to set, can not be null.
	 */
	public void setMotd(String motd) {
		if (motd == null) {
			throw new NullPointerException("Message of the day can not be null");
		}
		this.motd = motd;
	}

	/**
	 * Gets the number of players reported to be online.
	 * @return players online
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * Sets the number of players online
	 * @param numPlayers online
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	/**
	 * Gets the maximum number of players that can log in for this server.
	 * @return maximum number of players.
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * Sets the maximum number of players.
	 * @param maxPlayers maximum amount of people that can log in.
	 */
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	/**
	 * Gets the full message response that will be sent to the packet.
	 * @return packet message
	 */
	public String getMessage() {
		return motd + '\u0000' + numPlayers + '\u0000' + maxPlayers + '\u0000';
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
