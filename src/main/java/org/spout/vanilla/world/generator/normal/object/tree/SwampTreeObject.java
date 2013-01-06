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
package org.spout.vanilla.world.generator.normal.object.tree;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

public class SwampTreeObject extends SmallTreeObject {
	public SwampTreeObject(Random random) {
		super(random);
		addLeavesVines(true);
		setLeavesXZRadiusIncrease((byte) 1);
	}

	public SwampTreeObject() {
		this(null);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		// Can only place trees within height range of the world
		if (y < 1 || y + totalHeight + 2 > w.getHeight()) {
			return false;
		}
		// Can only place trees on dirt and grass surfaces
		if (!w.getBlockMaterial(x, y - 1, z).isMaterial(VanillaMaterials.DIRT, VanillaMaterials.GRASS)) {
			return false;
		}
		byte radiusToCheck = radiusIncrease;
		for (byte yy = 0; yy < totalHeight + 2; yy++) {
			if (yy == 1 || yy == totalHeight - 1) {
				radiusToCheck++;
			}
			for (byte xx = (byte) -radiusToCheck; xx < radiusToCheck + 1; xx++) {
				for (byte zz = (byte) -radiusToCheck; zz < radiusToCheck + 1; zz++) {
					final BlockMaterial material = w.getBlockMaterial(x + xx, y + yy, z + zz);
					if (!overridable.contains(material)) {
						if (yy == 0 && material.isMaterial(VanillaMaterials.WATER)) {
							continue;
						}
						return false;
					}
				}
			}
		}
		return true;
	}
}
