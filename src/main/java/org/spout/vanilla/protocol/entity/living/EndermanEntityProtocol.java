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
package org.spout.vanilla.protocol.entity.living;

import java.util.List;

import org.spout.api.entity.Entity;
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;

import org.spout.vanilla.components.living.Enderman;
import org.spout.vanilla.protocol.entity.BasicMobEntityProtocol;

public class EndermanEntityProtocol extends BasicMobEntityProtocol {
	public EndermanEntityProtocol() {
		super(58);
	}

	@Override
	public List<Parameter<?>> getSpawnParameters(Entity entity) {
		Enderman enderman = entity.add(Enderman.class);
		ItemStack held = enderman.getHeldItem();
		if (held != null && !held.getMaterial().equals(enderman.getPreviouslyHeldItem().getMaterial())) {
			List<Parameter<?>> parameters = super.getSpawnParameters(entity);
			parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, (byte) held.getMaterial().getId()));
			parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 17, (byte) held.getData()));
			return parameters;
		}
	}
}
