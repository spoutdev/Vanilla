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
package org.spout.vanilla.protocol.handler.player;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionMessage;

public final class PlayerPositionHandler extends MessageHandler<PlayerPositionMessage> {
	@Override
	public void handleServer(Session session, PlayerPositionMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Player holder = session.getPlayer();

		Point newPosition = new Point(message.getPosition(), holder.getWorld());
		Point position = holder.getTransform().getPosition();
		
		if (holder.getNetworkSynchronizer().isTeleportPending()) {
			if (position.getX() == newPosition.getX() && position.getZ() == newPosition.getZ()) {
				holder.getNetworkSynchronizer().clearTeleportPending();
			}
		} else {
			if (!position.equals(newPosition)) {
				if (position.distance(newPosition) < 4) {
					holder.getTransform().setPosition(newPosition);
				} else {
					holder.sendMessage("Restoring position due to fast movement");
					holder.teleport(holder.getTransform().getTransform());
				}
			}
		}
	}
}
