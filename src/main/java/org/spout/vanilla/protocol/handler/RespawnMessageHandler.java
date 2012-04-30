/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
import org.spout.vanilla.event.player.PlayerRespawnEvent;
import org.spout.vanilla.generator.flat.FlatGenerator;
import org.spout.vanilla.generator.nether.NetherGenerator;
import org.spout.vanilla.generator.normal.NormalGenerator;
import org.spout.vanilla.generator.theend.TheEndGenerator;
import org.spout.vanilla.protocol.msg.RespawnMessage;

public class RespawnMessageHandler extends MessageHandler<RespawnMessage> {
	@Override
	public void handleServer(Session session, Player player, RespawnMessage message) {
		if (player == null || player.getEntity() == null) {
			return;
		}

		PlayerRespawnEvent event = new PlayerRespawnEvent(player.getEntity(), player.getEntity().getPosition());
		Spout.getEngine().getEventManager().callEvent(event);

		//Set position for the server
		Point point = event.getPoint();
		player.getEntity().setPosition(point);
		player.getNetworkSynchronizer().setPositionDirty();

		//Send respawn packet back to the client.
		//TODO We need worlds associated with vanilla storing characteristics
		RespawnMessage respawn;
		if (point.getWorld().getGenerator() instanceof NormalGenerator) {
			respawn = new RespawnMessage(0, (byte) 1, (((VanillaPlayer) player.getEntity()).getGameMode().getId()), 256, "DEFAULT");
		} else if (point.getWorld().getGenerator() instanceof FlatGenerator) {
			respawn = new RespawnMessage(0, (byte) 1, (((VanillaPlayer) player.getEntity()).getGameMode().getId()), 256, "SUPERFLAT");
		} else if (point.getWorld().getGenerator() instanceof NetherGenerator) {
			respawn = new RespawnMessage(-1, (byte) 1, (((VanillaPlayer) player.getEntity()).getGameMode().getId()), 256, "DEFAULT");
		} else if (point.getWorld().getGenerator() instanceof TheEndGenerator) {
			respawn = new RespawnMessage(1, (byte) 1, (((VanillaPlayer) player.getEntity()).getGameMode().getId()), 256, "DEFAULT");
		} else {
			return;
		}
		session.send(respawn);
	}
}
