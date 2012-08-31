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
package org.spout.vanilla.util;

import java.util.ArrayList;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.plugin.Platform;
import org.spout.api.protocol.Message;

import org.spout.vanilla.protocol.VanillaProtocol;

public class VanillaNetworkUtil {
	/**
	 * This method takes any amount of messages and sends them to every online player on the server.
	 * @param messages
	 */
	public static void broadcastPacket(Message... messages) {
		//TODO: Let the setNextSpawn of the monster spawner use a protocol event or network component
		Platform platform = Spout.getPlatform();
		if (platform != Platform.SERVER || platform != Platform.PROXY) {
			return;
		}
		for (Player player : ((Server) Spout.getEngine()).getOnlinePlayers()) {
			sendPacket(player, messages);
		}
	}

	/**
	 * This method takes any amount of messages and sends them to all players except the collection of
	 * ignored players specified.
	 * @param ignore Players to ignore when sending messages
	 * @param messages Messages to send
	 */
	public static void broadcastPacket(Player[] ignore, Message... messages) {
		//TODO: Handle client status messages differently
		Platform platform = Spout.getPlatform();
		if (platform != Platform.SERVER || platform != Platform.PROXY) {
			return;
		}
		ArrayList<Player> toSend = new ArrayList<Player>();
		for (Player player : ((Server) Spout.getEngine()).getOnlinePlayers()) {
			for (Player ignored : ignore) {
				if (player.equals(ignored)) {
					continue;
				}
				toSend.add(player);
			}
		}
		for (Player player : toSend) {
			sendPacket(player, messages);
		}
	}

	private static void sendPacket(Player player, Message... messages) {
		if (!(player.getSession().getProtocol() instanceof VanillaProtocol)) {
			return;
		}
		for (Message message : messages) {
			player.getSession().send(false, message);
		}
	}
}
