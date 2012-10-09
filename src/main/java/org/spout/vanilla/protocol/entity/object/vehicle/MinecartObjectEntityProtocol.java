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
package org.spout.vanilla.protocol.entity.object.vehicle;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.util.Parameter;

import org.spout.vanilla.component.substance.object.vehicle.Minecart;
import org.spout.vanilla.protocol.entity.ObjectEntityProtocol;

public class MinecartObjectEntityProtocol extends ObjectEntityProtocol {
	public MinecartObjectEntityProtocol() {
		super(Minecart.ID);
	}

	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		List<Parameter<?>> params = super.getSpawnParameters(entity);
		params.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, (byte) 0)); // Powered flag
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 17, 0)); // Unknown flag; initialized to 0. (Probably time since last collision)
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 18, 1)); // Unknown flag; initialized to 1. (Probably acceleration)
		params.add(new Parameter<Integer>(Parameter.TYPE_INT, 19, 0)); // Damage taken; breaks at 40.
		return params;
	}
}
