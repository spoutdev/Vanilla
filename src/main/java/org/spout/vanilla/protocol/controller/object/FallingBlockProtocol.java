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
package org.spout.vanilla.protocol.controller.object;

import java.util.Arrays;

import org.spout.api.entity.component.controller.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.controller.block.MovingBlock;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.controller.BasicVehicleEntityProtocol;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;

public class FallingBlockProtocol extends BasicVehicleEntityProtocol {
	public static final int BLOCK_TYPE_METADATA_INDEX = 30;

	public FallingBlockProtocol() {
		super(21);
	}

	@Override
	public Message[] getSpawnMessage(Entity entity) {
		final Controller controller = entity.getController();
		int spawnId = 0;
		Block block = null;
		if (controller instanceof MovingBlock) {
			block = ((MovingBlock) controller).getBlock();
			BlockMaterial mat = block.getMaterial();
			if (mat.equals(VanillaMaterials.DRAGON_EGG)) {
				spawnId = 74;
			} else if (mat.equals(VanillaMaterials.GRAVEL)) {
				spawnId = 71;
			} else if (mat.equals(VanillaMaterials.SAND)) {
				spawnId = 70;
			}
		}
		if (spawnId > 0) {
			Point position = entity.getPosition();
			int x = MathHelper.floor(position.getX());
			int y = MathHelper.floor(position.getY());
			int z = MathHelper.floor(position.getZ());
			SpawnVehicleMessage msg = new SpawnVehicleMessage(entity.getId(), spawnId, x, y, z);
			return new Message[]{msg, new EntityMetadataMessage(entity.getId(), Arrays.<Parameter<?>>asList(new Parameter<Short>(Parameter.TYPE_SHORT, BLOCK_TYPE_METADATA_INDEX, VanillaMaterials.getMinecraftId(block.getMaterial()))))};
		}
		return null;
	}
}
