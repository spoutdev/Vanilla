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

public class CactusStackObject extends LargePlantObject {
	public CactusStackObject() {
		this(null);
	}

	public CactusStackObject(Random random) {
		super(random, (byte) 1, (byte) 3);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final BlockMaterial bellow = w.getBlockMaterial(x, y - 1, z);
		return (bellow == VanillaMaterials.SAND || bellow == VanillaMaterials.CACTUS)
				&& w.getBlockMaterial(x, y, z) == VanillaMaterials.AIR
				&& w.getBlockMaterial(x - 1, y, z) == VanillaMaterials.AIR
				&& w.getBlockMaterial(x + 1, y, z) == VanillaMaterials.AIR
				&& w.getBlockMaterial(x, y, z - 1) == VanillaMaterials.AIR
				&& w.getBlockMaterial(x, y, z + 1) == VanillaMaterials.AIR;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		for (byte yy = 0; yy < totalHeight; yy++) {
			if (!canPlaceObject(w, x, y + yy, z)) {
				return;
			}
			w.getBlock(x, y + yy, z).setMaterial(VanillaMaterials.CACTUS);
		}
	}
}
