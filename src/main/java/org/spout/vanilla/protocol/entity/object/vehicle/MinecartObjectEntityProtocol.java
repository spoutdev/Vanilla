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
package org.spout.vanilla.protocol.entity.object.vehicle;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.entity.substance.vehicle.minecart.MinecartBase;
import org.spout.vanilla.protocol.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.protocol.entity.object.ObjectType;

public class MinecartObjectEntityProtocol extends ObjectEntityProtocol {
	public MinecartObjectEntityProtocol(ObjectType type) {
		super(type);
		if (!ObjectType.MINECART.equals(type)) {
			throw new IllegalStateException("Invalid minecart type!");
		}
	}

	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> params = super.getSpawnParameters(entity);
		MinecartBase base = entity.get(MinecartBase.class);
		int block = base.getMinecraftBlockID();

		params.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, (byte) 0)); // Powered flag
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 17, 0)); // Unknown flag; initialized to 0. (Probably time since last collision)
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 18, 1)); // Unknown flag; initialized to 1. (Probably acceleration)
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 19, 0)); // Damage taken; breaks at 40.
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 20, block)); // Block ID to display in the minecart
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 21, 6)); // Display Data, where to display the block, 6 is default.
		params.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 22, (byte) (block != 0 ? 1 : 0))); // 0 == Block Display off, 1 == Block Display on
		return params;
	}
}
