/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
package org.spout.vanilla.protocol.entity.object;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.controller.object.MovingBlock;
import org.spout.vanilla.protocol.entity.BasicVehicleEntityProtocol;
import org.spout.vanilla.protocol.msg.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;

import java.util.Arrays;

/**
 *
 * @author zml2008
 */
public class FallingBlockProtocol extends BasicVehicleEntityProtocol {
	public static final int BLOCK_TYPE_METADATA_INDEX = 30;
	@Override
	public int getSpawnedVehicleType() {
		return 70;
	}

	@Override
	public Message[] getSpawnMessage(Entity entity) {
		final Controller controller = entity.getController();
		BlockMaterial block = VanillaMaterials.SAND;
		int spawnId = 70; //TODO: support for other moving block types?
		if (controller instanceof MovingBlock) {
			block = ((MovingBlock) controller).getBlock();
			if (block == VanillaMaterials.DRAGON_EGG) {
				spawnId = 74;
			} else if (block == VanillaMaterials.GRAVEL) {
				spawnId = 71;
			}
		}
		if (spawnId > 0) {
			Point position = entity.getPosition();
			int x = MathHelper.floor(position.getX());
			int y = MathHelper.floor(position.getY());
			int z = MathHelper.floor(position.getZ());
			SpawnVehicleMessage msg = new SpawnVehicleMessage(entity.getId(), spawnId, x, y, z);
			return new Message[] {msg, new EntityMetadataMessage(entity.getId(), Arrays.<Parameter<?>>asList(new Parameter<Short>(Parameter.TYPE_SHORT, BLOCK_TYPE_METADATA_INDEX, block.getId())))};
		}
		return null;
	}
}
