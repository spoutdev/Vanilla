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
package org.spout.vanilla.world.generator.normal.object.largeplant;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.LargePlantObject;

public class SugarCaneStackObject extends LargePlantObject {
	public SugarCaneStackObject() {
		this(null);
	}

	public SugarCaneStackObject(Random random) {
		super(random, (byte) 2, (byte) 4);
		randomize();
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final BlockMaterial below = w.getBlockMaterial(x, y - 1, z);
		return below.isMaterial(VanillaMaterials.DIRT, VanillaMaterials.GRASS, VanillaMaterials.SAND)
				&& w.getBlockMaterial(x, y, z) == VanillaMaterials.AIR
				&& (w.getBlockMaterial(x - 1, y - 1, z) == VanillaMaterials.STATIONARY_WATER
				|| w.getBlockMaterial(x + 1, y - 1, z) == VanillaMaterials.STATIONARY_WATER
				|| w.getBlockMaterial(x, y - 1, z - 1) == VanillaMaterials.STATIONARY_WATER
				|| w.getBlockMaterial(x, y - 1, z + 1) == VanillaMaterials.STATIONARY_WATER);
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		for (byte yy = 0; yy < totalHeight; yy++) {
			if (w.getBlockMaterial(x, y + yy, z) != VanillaMaterials.AIR) {
				return;
			}
			w.setBlockMaterial(x, y + yy, z, VanillaMaterials.SUGAR_CANE_BLOCK, (short) 0, w);
		}
	}

	@Override
	public final void randomize() {
		totalHeight = (byte) (baseHeight + random.nextInt(randomHeight));
	}
}
