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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity.living.player;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.protocol.EntityProtocol;
import org.spout.api.protocol.Message;
import org.spout.vanilla.protocol.msg.DestroyEntityMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;

public class MinecraftPlayerEntityProtocol implements EntityProtocol {
	@Override
	public Message getSpawnMessage(Entity entity) {
		Controller c = entity.getLiveController();
		//TODO: this if-else structure is terrible and not OO. Fix!
		if (c != null) {
			MinecraftPlayer mcp = (MinecraftPlayer)c;
			int id = entity.getId();
			String name = mcp.getPlayer().getName();
			Transform t = entity.getTransform();
			int x = (int)(t.getPosition().getX() * 32);
			int y = (int)(t.getPosition().getY() * 32);
			int z = (int)(t.getPosition().getZ() * 32);
			int r = 0;
			int p = 0;
			int item = 0;
			return new SpawnPlayerMessage(id, name, x, y, z, r, p, item);
		} else {
			return null;
		}
	}

	@Override
	public Message getDestroyMessage(Entity entity) {
		return new DestroyEntityMessage(entity.getId());
	}

	@Override
	public Message getUpdateMessage(Entity entity) {
		Transform t = entity.getLiveTransform();
		int id = entity.getId();
		int x = (int)(t.getPosition().getX() * 32);
		int y = (int)(t.getPosition().getY() * 32);
		int z = (int)(t.getPosition().getZ() * 32);
		int r = 0;
		int p = 0;
		// TODO - improve efficiency
		return new EntityTeleportMessage(id, x, y, z, r, p);
	}
}
