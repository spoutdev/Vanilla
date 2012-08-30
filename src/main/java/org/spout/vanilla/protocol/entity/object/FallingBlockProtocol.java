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
package org.spout.vanilla.protocol.entity.object;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.protocol.Message;
import org.spout.api.util.Parameter;

import org.spout.vanilla.components.object.moving.MovingBlock;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.BasicVehicleEntityProtocol;
import org.spout.vanilla.protocol.msg.entity.EntityMetadataMessage;
import org.spout.vanilla.protocol.msg.entity.EntitySpawnVehicleMessage;

public class FallingBlockProtocol extends BasicVehicleEntityProtocol {
	public static final int BLOCK_TYPE_METADATA_INDEX = 30;

	public FallingBlockProtocol() {
		super(21);
	}

	@Override
	public List<Message> getSpawnMessages(Entity entity) {
		final Controller controller = entity.getController();
		if (!(controller instanceof MovingBlock)) {
			return Collections.emptyList();
		}
		int spawnId;
		BlockMaterial mat = ((MovingBlock) controller).getMaterial();
		if (mat.equals(VanillaMaterials.DRAGON_EGG)) {
			spawnId = 74;
		} else if (mat.equals(VanillaMaterials.GRAVEL)) {
			spawnId = 71;
		} else {
			spawnId = 70; // sand
		}
		Point position = entity.getPosition();
		List<Parameter<?>> parameters = Arrays.<Parameter<?>>asList(new Parameter<Short>(Parameter.TYPE_SHORT, BLOCK_TYPE_METADATA_INDEX, VanillaMaterials.getMinecraftId(mat)));
		return Arrays.<Message>asList(new EntitySpawnVehicleMessage(entity.getId(), spawnId, position), new EntityMetadataMessage(entity.getId(), parameters));
	}
}
