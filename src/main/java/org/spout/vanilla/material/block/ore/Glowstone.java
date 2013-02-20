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
package org.spout.vanilla.material.block.ore;

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.material.InitializableMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Ore;
import org.spout.vanilla.material.block.component.SignBase;

public class Glowstone extends Ore implements InitializableMaterial {
	public Glowstone(String name, int id) {
		super(name, id, VanillaMaterialModels.GLOWSTONE);
		this.setHardness(0.3F).setResistance(0.5F);
	}

	@Override
	public void initialize() {
		this.getDrops().DEFAULT.clear().addRange(VanillaMaterials.GLOWSTONE_DUST, 2, 4);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return material instanceof SignBase || material.equals(VanillaMaterials.REDSTONE_WIRE);
	}

	@Override
	public boolean isRedstoneConductor() {
		return false;
	}

	@Override
	public byte getLightLevel(short data) {
		return 15;
	}
}
