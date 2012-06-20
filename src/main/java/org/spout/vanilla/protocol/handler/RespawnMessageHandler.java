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
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.Data;
import org.spout.vanilla.event.player.PlayerRespawnEvent;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;
import org.spout.vanilla.util.VanillaNetworkUtil;

public class RespawnMessageHandler extends MessageHandler<RespawnMessage> {
	@Override
	public void handleServer(Session session, Player player, RespawnMessage message) {
		PlayerRespawnEvent event = new PlayerRespawnEvent(player.getEntity(), player.getEntity().getLastTransform().getPosition().getWorld().getSpawnPoint().getPosition());
		Spout.getEngine().getEventManager().callEvent(event);

		if (event.isCancelled()) {
			return;
		}

		//Set position for the server
		Point point = event.getPoint();
		player.getEntity().setPosition(point);
		player.getNetworkSynchronizer().setPositionDirty();
		VanillaPlayer controller = (VanillaPlayer) player.getEntity().getController();
		controller.setHealth(controller.getMaxHealth(), HealthChangeReason.SPAWN);

		//Send respawn packet back to the client.
		//TODO We need worlds associated with vanilla storing characteristics
		int dimension = point.getWorld().getDataMap().get(Data.DIMENSION).getId();
		byte difficulty = point.getWorld().getDataMap().get(Data.DIFFICULTY).getId();
		byte gamemode = ((VanillaPlayer) player.getEntity().getController()).getGameMode().getId();
		String worldType = point.getWorld().getDataMap().get(Data.WORLD_TYPE).toString();
		RespawnMessage respawn = new RespawnMessage(dimension, difficulty, gamemode, 256, worldType);
		session.send(respawn);

		//send spawn to everyone else
		SpawnPlayerMessage spawn = new SpawnPlayerMessage(player.getEntity().getId(), player.getDisplayName(), point, (int) player.getEntity().getYaw(), (int) player.getEntity().getPitch(), 0);
		VanillaNetworkUtil.broadcastPacket(new Player[]{player}, spawn);
	}
}
