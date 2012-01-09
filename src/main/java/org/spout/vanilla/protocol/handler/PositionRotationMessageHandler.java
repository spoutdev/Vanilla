/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.protocol.handler;

import org.spout.api.entity.Entity;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.entity.living.player.MinecraftPlayer;
import org.spout.vanilla.protocol.msg.PositionRotationMessage;

public final class PositionRotationMessageHandler extends MessageHandler<PositionRotationMessage> {
	@Override
	public void handleServer(Session session, Player player, PositionRotationMessage message) {
		if (player == null) {
			return;
		}
		
		Entity entity = player.getEntity();
		
		if (entity == null) {
			return;
		}
		
		float pitch = message.getPitch();
		float rot = message.getRotation();
		
		// Stance and Y swapped for server
		double stance = message.getStance();
		double x = message.getX();
		double y = message.getY();
		double z = message.getZ();
		
		Transform liveTransform = entity.getLiveTransform();
		World w = liveTransform.getPosition().getWorld();
		// TODO - include rotation
		entity.setTransform(new Transform(new Point(w, (float)x, ((float)y), (float)z), Quaternion.identity, Vector3.Forward));
	}
	
	public void handleClient(Session session, Player player, PositionRotationMessage message) {
	}
}