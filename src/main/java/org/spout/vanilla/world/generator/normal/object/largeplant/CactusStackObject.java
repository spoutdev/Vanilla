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
package org.spout.vanilla.world.generator.normal.object.largeplant;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.LargePlantObject;

public class CactusStackObject extends LargePlantObject {
	public CactusStackObject() {
		this(null);
	}

	public CactusStackObject(Random random) {
		super(random, (byte) 1, (byte) 3);
		randomize();
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final BlockMaterial below = w.getBlockMaterial(x, y - 1, z);
		return (below.isMaterial(VanillaMaterials.SAND, VanillaMaterials.CACTUS))
				&& w.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.AIR)
				&& w.getBlockMaterial(x - 1, y, z).isMaterial(VanillaMaterials.AIR)
				&& w.getBlockMaterial(x + 1, y, z).isMaterial(VanillaMaterials.AIR)
				&& w.getBlockMaterial(x, y, z - 1).isMaterial(VanillaMaterials.AIR)
				&& w.getBlockMaterial(x, y, z + 1).isMaterial(VanillaMaterials.AIR);
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		for (byte yy = 0; yy < totalHeight; yy++) {
			if (!canPlaceObject(w, x, y + yy, z)) {
				return;
			}
			w.setBlockMaterial(x, y + yy, z, VanillaMaterials.CACTUS, (short) 0, null);
		}
	}

	@Override
	public final void randomize() {
		totalHeight = (byte) (baseHeight + random.nextInt(randomHeight));
	}
}
