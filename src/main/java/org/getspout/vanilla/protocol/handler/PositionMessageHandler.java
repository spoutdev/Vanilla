/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
package org.getspout.vanilla.protocol.handler;

import org.getspout.api.entity.Entity;
import org.getspout.api.geo.World;
import org.getspout.api.geo.discrete.Point;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.api.math.Quaternion;
import org.getspout.api.math.Vector3;
import org.getspout.api.player.Player;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.api.protocol.Session;
import org.getspout.vanilla.entity.living.player.MinecraftPlayer;
import org.getspout.vanilla.protocol.msg.PositionMessage;
import org.getspout.vanilla.protocol.msg.PositionRotationMessage;

public final class PositionMessageHandler extends MessageHandler<PositionMessage> {

	@Override
	public void handleServer(Session session, Player player, PositionMessage message) {
		if (player == null) {
			return;
		}
		
		Entity entity = player.getEntity();
		
		if (entity == null) {
			return;
		}
		
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