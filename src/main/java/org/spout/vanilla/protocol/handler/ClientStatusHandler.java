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
package org.spout.vanilla.protocol.handler;

import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.geo.World;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.ClientStatusMessage;
import org.spout.vanilla.protocol.msg.login.request.ServerLoginRequestMessage;

public class ClientStatusHandler extends MessageHandler<ClientStatusMessage> {
	@Override
	public void handleServer(Session session, Player player, ClientStatusMessage message) {
		// TODO Do we handle anything? Should we send chunks when the client says "I am a'okay to log in?", what does "I am a'okay to login even mean?"
		// For now lets print out when the client sends it
		Spout.log("Client sent: " + message.toString());
		World world = Spout.getEngine().getWorlds().iterator().next();
		if (message.getStatus() == ClientStatusMessage.INITIAL_SPAWN) {
			session.send(false, new ServerLoginRequestMessage(player.getEntity().getId(), world.getDataMap().get(VanillaData.WORLD_TYPE).name(), world.getDataMap().get(VanillaData.GAMEMODE).getId(), (byte) world.getDataMap().get(VanillaData.DIMENSION).getId(), world.getDataMap().get(VanillaData.DIFFICULTY).getId(), (short) Spout.getEngine().getMaxPlayers()));
			playerConnect(session, player.getName());
		}

	}

	public static void playerConnect(Session session, String name) {
		Event event = new PlayerConnectEvent(session, name);
		session.getEngine().getEventManager().callEvent(event);
		if (Spout.getEngine().debugMode()) {
			Spout.getLogger().info("Login took " + (System.currentTimeMillis() - session.getDataMap().get(VanillaProtocol.LOGIN_TIME)) + " ms");
		}
	}
}
