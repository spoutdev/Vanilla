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

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.component.living.neutral.Human;
import org.spout.vanilla.component.player.PingComponent;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.configuration.WorldConfigurationNode;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.player.pos.PlayerPositionMessage;

public final class PlayerPositionHandler extends MessageHandler<PlayerPositionMessage> {
	@Override
	public void handleServer(Session session, PlayerPositionMessage message) {
		if (!session.hasPlayer()) {
			return;
		}
		Player holder = session.getPlayer();

		if (holder.has(PingComponent.class)) {
			holder.get(PingComponent.class).refresh();
		}

		Point newPosition = new Point(message.getPosition(), holder.getWorld());
		Point position = holder.getTransform().getPosition();
		
		newPosition = ((VanillaNetworkSynchronizer)session.getPlayer().getNetworkSynchronizer()).toServer(newPosition);
		
		if (holder.getNetworkSynchronizer().isTeleportPending()) {
			if (position.getX() == newPosition.getX() && position.getZ() == newPosition.getZ() && Math.abs(position.getY() - newPosition.getY()) < 16) {
				holder.getNetworkSynchronizer().clearTeleportPending();
			}
		} else {
			if (!position.equals(newPosition)) {
				//TODO: better movement checking
				final double dx = position.getX() - newPosition.getX();
				final double dy = position.getY() - newPosition.getY();
				final double dz = position.getZ() - newPosition.getZ();
				final double dist = dx * dx + dy * dy + dz * dz;
				WorldConfigurationNode node = VanillaConfiguration.WORLDS.get(holder.getWorld());
				if (dist < 100 || node.ALLOW_FLIGHT.getBoolean()) {
					holder.getTransform().setPosition(newPosition);
					holder.get(Human.class).setOnGround(message.isOnGround());
				} else {
					holder.kick("Moved too quickly");
				}
			}
		}
	}
}
