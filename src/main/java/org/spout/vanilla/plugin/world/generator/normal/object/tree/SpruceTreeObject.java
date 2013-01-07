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

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.plant.Sapling;

public class SpruceTreeObject extends TreeObject {
	private byte leavesBottomY = -1;
	private byte leavesMaxRadius = -1;

	public SpruceTreeObject() {
		this(null);
	}

	public SpruceTreeObject(Random random) {
		super(random, (byte) 7, (byte) 5, (short) 1);
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
		overridable.add(Sapling.SPRUCE);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		if (!super.canPlaceObject(w, x, y, z)) {
			return false;
		}
		findRandomLeavesSize();
		int checkRadius = 0;
		for (byte yy = 0; yy < totalHeight + 2; yy++) {
			if (yy == leavesBottomY) {
				checkRadius = leavesMaxRadius;
			}
			for (byte xx = (byte) -checkRadius; xx < checkRadius + 1; xx++) {
				for (byte zz = (byte) -checkRadius; zz < checkRadius + 1; zz++) {
					if (!overridable.contains(w.getBlockMaterial(x + xx, y + yy, z + zz))) {
						return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		if (leavesBottomY == -1 || leavesMaxRadius == -1) {
			findRandomLeavesSize();
		}
		w.setBlockMaterial(x, y - 1, z, VanillaMaterials.DIRT, (short) 0, null);
		byte leavesRadius = 0;
		for (byte yy = totalHeight; yy >= leavesBottomY; yy--) {
			for (byte xx = (byte) -leavesRadius; xx < leavesRadius + 1; xx++) {
				for (byte zz = (byte) -leavesRadius; zz < leavesRadius + 1; zz++) {
					if (Math.abs(xx) != leavesRadius || Math.abs(zz) != leavesRadius || leavesRadius <= 0) {
						w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.LEAVES, leavesMetadata, null);
					}
				}
			}
			if (leavesRadius > 0 && yy == y + leavesBottomY + 1) {
				leavesRadius--;
			} else if (leavesRadius < leavesMaxRadius) {
				leavesRadius++;
			}
		}
		for (int yy = 0; yy < totalHeight - 1; yy++) {
			w.setBlockMaterial(x, y + yy, z, VanillaMaterials.LOG, logMetadata, null);
		}
	}

	private void findRandomLeavesSize() {
		leavesBottomY = (byte) (totalHeight - random.nextInt(2) - 3);
		leavesMaxRadius = (byte) (1 + random.nextInt(totalHeight - leavesBottomY + 1));
	}

	public void setLeavesBottomY(byte leavesBottomY) {
		this.leavesBottomY = leavesBottomY;
	}

	public void setLeavesMaxRadius(byte leavesMaxRadius) {
		this.leavesMaxRadius = leavesMaxRadius;
	}
}
