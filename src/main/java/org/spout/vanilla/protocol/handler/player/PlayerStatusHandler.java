/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
import org.spout.api.Server;

import org.spout.api.Spout;
import org.spout.api.datatable.ManagedMap;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;
import org.spout.api.protocol.Session;
import org.spout.api.protocol.event.EntitySyncEvent;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.WorldType;
import org.spout.vanilla.event.cause.HealthChangeCause;
import org.spout.vanilla.event.player.PlayerRespawnEvent;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.player.PlayerStatusMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerLoginRequestMessage;

public class PlayerStatusHandler extends MessageHandler<PlayerStatusMessage> {
	@Override
	public void handleServer(ServerSession session, PlayerStatusMessage message) {
		final Server server = session.getEngine();

		if (message.getStatus() == PlayerStatusMessage.INITIAL_SPAWN) {
			server.getEventManager().callEvent(new PlayerConnectEvent(session, (String) session.getDataMap().get("username"), 10));
			if (server.debugMode()) {
				server.getLogger().info("Login took " + (System.currentTimeMillis() - session.getDataMap().get(VanillaProtocol.LOGIN_TIME)) + " ms");
			}
			final ManagedMap data = session.getPlayer().getWorld().getData();
			final Human human = session.getPlayer().add(Human.class);
			final Difficulty difficulty = data.get(VanillaData.DIFFICULTY);
			final Dimension dimension = data.get(VanillaData.DIMENSION);
			final WorldType worldType = data.get(VanillaData.WORLD_TYPE);

			GameMode gamemode;

			int entityId = session.getPlayer().getId();

			//  MC Packet Order: 0x01 Login, 0xFA Custom (ServerTypeName), 0x06 SpawnPos, 0xCA PlayerAbilities, 0x10 BlockSwitch
			if (human != null && human.getAttachedCount() > 1) {
				gamemode = human.getGameMode();
			} else {
				gamemode = data.get(VanillaData.GAMEMODE);
				if (human != null) {
					human.setGamemode(gamemode);
				}
			}
			PlayerLoginRequestMessage idMsg = new PlayerLoginRequestMessage(entityId, worldType.toString(), gamemode.getId(), (byte) dimension.getId(), difficulty.getId(), (byte) server.getMaxPlayers());
			session.send(true, idMsg);
			session.setState(Session.State.GAME);
		} else if (message.getStatus() == PlayerStatusMessage.RESPAWN) {
			Player player = session.getPlayer();
			Point point = player.getWorld().getSpawnPoint().getPosition();
			if (PlayerRespawnEvent.getHandlerList().getRegisteredListeners().length > 0) {
				PlayerRespawnEvent event = server.getEventManager().callEvent(new PlayerRespawnEvent(player, point));

				if (event.isCancelled()) {
					return;
				}
				point = event.getPoint();
			}
			//Set position for the server
			player.getPhysics().setPosition(point);
			player.getNetwork().forceRespawn();
			Human human = player.add(Human.class);
			human.getHealth().setHealth(human.getHealth().getMaxHealth(), HealthChangeCause.SPAWN);

			final Transform spawn = new Transform(player.getPhysics().getTransform());
			spawn.setPosition(point);
			//send spawn to everyone else
			Set<? extends Player> observers = player.getChunk().getObservingPlayers();
			for (Player otherPlayer : observers) {
				if (player == otherPlayer) {
					continue;
				}
				otherPlayer.getNetwork().callProtocolEvent(new EntitySyncEvent(player, spawn, true, false, false), player);
			}
		}
	}
}
