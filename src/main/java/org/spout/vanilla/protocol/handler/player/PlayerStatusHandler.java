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
package org.spout.vanilla.protocol.handler.player;

import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.event.cause.HealthChangeCause;
import org.spout.vanilla.component.living.neutral.Human;
import org.spout.vanilla.event.player.PlayerRespawnEvent;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.player.PlayerStatusMessage;

public class PlayerStatusHandler extends MessageHandler<PlayerStatusMessage> {
	@Override
	public void handleServer(Session session, PlayerStatusMessage message) {
		if (message.getStatus() == PlayerStatusMessage.INITIAL_SPAWN) {
			if (PlayerConnectEvent.getHandlerList().getRegisteredListeners().length > 0) {
				Spout.getEventManager().callEvent(new PlayerConnectEvent(session, (String) session.getDataMap().get("username")));
			}
			if (Spout.getEngine().debugMode()) {
				Spout.getLogger().info("Login took " + (System.currentTimeMillis() - session.getDataMap().get(VanillaProtocol.LOGIN_TIME)) + " ms");
			}
		} else if (message.getStatus() == PlayerStatusMessage.RESPAWN) {
			if (!session.hasPlayer()) {
				return;
			}
			Player player = session.getPlayer();
			Point point = player.getWorld().getSpawnPoint().getPosition();
			if (PlayerRespawnEvent.getHandlerList().getRegisteredListeners().length > 0) {
				PlayerRespawnEvent event = Spout.getEventManager().callEvent(new PlayerRespawnEvent(player, point));

				if (event.isCancelled()) {
					return;
				}
				point = event.getPoint();
			}
			//Set position for the server
			player.teleport(point);
			player.getNetworkSynchronizer().setRespawned();
			Human human = player.get(Human.class);
			if (human != null) {
				human.getHealth().setHealth(human.getHealth().getMaxHealth(), HealthChangeCause.SPAWN);
			}

			final Transform spawn = new Transform(player.getScene().getTransform());
			spawn.setPosition(point);
			//send spawn to everyone else
			Set<? extends Player> observers = player.getChunk().getObservingPlayers();
			for (Player otherPlayer : observers) {
				if (player == otherPlayer) {
					continue;
				}
				otherPlayer.getNetworkSynchronizer().syncEntity(player, spawn, true, false, false);
			}
		}
	}
}
