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
import org.spout.api.geo.discrete.Point;
import org.spout.api.entity.Player;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.entity.source.HealthChangeReason;
import org.spout.vanilla.event.player.PlayerRespawnEvent;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.ClientStatusMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

public class ClientStatusHandler extends MessageHandler<ClientStatusMessage> {
	@Override
	public void handleServer(Session session, ClientStatusMessage message) {
		// TODO - need check if player has already connected
		if (message.getStatus() == ClientStatusMessage.INITIAL_SPAWN) {
			playerConnect(session, (String) session.getDataMap().get("username"));
		} else if (message.getStatus() == ClientStatusMessage.RESPAWN) {
			if (!session.hasPlayer()) {
				return;
			}

			Player player = session.getPlayer();
			PlayerRespawnEvent event = new PlayerRespawnEvent(player, player.getLastTransform().getPosition().getWorld().getSpawnPoint().getPosition());
			Spout.getEngine().getEventManager().callEvent(event);

			if (event.isCancelled()) {
				return;
			}

			//Set position for the server
			Point point = event.getPoint();
			player.setPosition(point);
			player.getNetworkSynchronizer().setRespawned();
			VanillaPlayerController controller = (VanillaPlayerController) player.getController();
			controller.getHealth().setHealth(controller.getHealth().getMaxHealth(), HealthChangeReason.SPAWN);

			//send spawn to everyone else
			EntityProtocol ep = controller.getType().getEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID);
			if (ep != null) {
				Message[] spawn = ep.getSpawnMessage(player);
				VanillaNetworkUtil.broadcastPacket(new Player[]{player}, spawn);
			}
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
