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
package org.spout.vanilla.world.generator.normal.object.tree;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

public class ShrubObject extends TreeObject {
	// size control
	private byte radius = 2;

	public ShrubObject() {
		this(null);
	}
	
	public ShrubObject(Random random) {
		super(random, (byte) 1, (byte) 1, (short) 3, (short) 0);
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final BlockMaterial material = w.getBlockMaterial(x, y - 1, z);
		return material == VanillaMaterials.DIRT || material == VanillaMaterials.GRASS;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		w.setBlockMaterial(x, y - 1, z, VanillaMaterials.DIRT, (short) 0, w);
		for (byte yy = 0; yy < totalHeight; yy++) {
			w.setBlockMaterial(x, y + yy, z, VanillaMaterials.LOG, logMetadata, w);
		}
		for (byte yy = radius; yy > -1; yy--) {
			for (byte xx = (byte) -yy; xx < yy + 1; xx++) {
				for (byte zz = (byte) -yy; zz < yy + 1; zz++) {
					if (Math.abs(xx) == yy && Math.abs(zz) == yy && random.nextBoolean()) {
						continue;
					}
					if (overridable.contains(w.getBlockMaterial(x + xx, y - yy + radius, z + zz))) {
						w.setBlockMaterial(x + xx, y - yy + radius, z + zz, VanillaMaterials.LEAVES, leavesMetadata, w);
					}
				}
			}
		}
	}

	public void setRadius(byte radius) {
		this.radius = radius;
	}
}
