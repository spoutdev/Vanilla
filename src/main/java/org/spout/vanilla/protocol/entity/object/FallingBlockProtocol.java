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
package org.spout.vanilla.protocol.entity.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.reposition.RepositionManager;

import org.spout.vanilla.component.substance.object.FallingBlock;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityObjectMessage;

public class FallingBlockProtocol extends ObjectEntityProtocol {
	public FallingBlockProtocol(ObjectType type) {
		super(type);
	}

	@Override
	public List<Message> getSpawnMessages(Entity entity, RepositionManager rm) {
		FallingBlock block = entity.get(FallingBlock.class);
		if (block != null) {
			VanillaBlockMaterial material = block.getMaterial();
			int messageData = material.getMinecraftId() | (material.getMinecraftData(material.getData()) >> 16);
			List<Message> messages = new ArrayList<Message>();

			final double p = 32d;
			Point pos = entity.getTransform().getPosition();
			int x = (int) Math.floor(pos.getX() * p);
			int y = (int) Math.floor(pos.getY() * p);
			int z = (int) Math.floor(pos.getZ() * p);
			short fallSpeed = (short) (block.getFallingSpeed() * 8000d);
			messages.add(new EntityObjectMessage(entity.getId(), (byte) typeId, x, y, z, messageData, (short) 0, fallSpeed, (short) 0, rm));
			messages.add(new EntityMetadataMessage(entity.getId(), getSpawnParameters(entity)));
			return messages;
		} else {
			return Collections.emptyList();
		}
	}
}
