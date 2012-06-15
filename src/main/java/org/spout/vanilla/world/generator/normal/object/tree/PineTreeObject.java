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

import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;
import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

public class PineTreeObject extends TreeObject {
	private byte leavesSizeY = -1;
	private byte leavesAbsoluteMaxRadius = -1;

	public PineTreeObject() {
		this(null);
	}
	
	public PineTreeObject(Random random) {
		super(random, (byte) 6, (byte) 4, (short) 1);
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		if (y < 1 || y + totalHeight + 1 > 128) {
			return false;
		}
		findRandomLeavesSize();
		int checkRadius = 0;
		for (byte yy = 0; yy < totalHeight + 2; yy++) {
			if (yy == leavesSizeY) {
				checkRadius = leavesAbsoluteMaxRadius;
			}
			for (byte xx = (byte) -checkRadius; xx < checkRadius + 1; xx++) {
				for (byte zz = (byte) -checkRadius; zz < checkRadius + 1; zz++) {
					if (!overridable.contains(w.getBlockMaterial(x + xx, y + yy, z + zz))) {
						return false;
					}
				}
			}
		}
		final BlockMaterial under = w.getBlockMaterial(x, y - 1, z);
		return under == VanillaMaterials.DIRT || under == VanillaMaterials.GRASS;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		if (leavesSizeY == -1 || leavesAbsoluteMaxRadius == -1) {
			findRandomLeavesSize();
		}
		w.setBlockMaterial(x, y - 1, z, VanillaMaterials.DIRT, (short) 0, w);
		byte leavesRadius = (byte) random.nextInt(2);
		byte leavesMaxRadius = 1;
		final byte leavesBottomY = (byte) (totalHeight - leavesSizeY);
		boolean firstMaxedRadius = false;
		for (int leavesY = 0; leavesY < leavesBottomY + 1; leavesY++) {
			int yy = totalHeight - leavesY;
			for (byte xx = (byte) -leavesRadius; xx < leavesRadius + 1; xx++) {
				for (byte zz = (byte) -leavesRadius; zz < leavesRadius + 1; zz++) {
					if (Math.abs(xx) != leavesRadius || Math.abs(zz) != leavesRadius || leavesRadius <= 0) {
						w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.LEAVES, leavesMetadata, w);
					}
				}
			}
			if (leavesRadius >= leavesMaxRadius) {
				leavesRadius = (byte) (firstMaxedRadius ? 1 : 0);
				firstMaxedRadius = true;
				if (++leavesMaxRadius > leavesAbsoluteMaxRadius) {
					leavesMaxRadius = leavesAbsoluteMaxRadius;
				}
			} else {
				leavesRadius++;
			}
		}
		final byte trunkHeightReducer = (byte) random.nextInt(3);
		for (int yy = 0; yy < totalHeight - trunkHeightReducer; yy++) {
			w.setBlockMaterial(x, y + yy, z, VanillaMaterials.LOG, logMetadata, w);
		}
	}

	private void findRandomLeavesSize() {
		leavesSizeY = (byte) (random.nextInt(2) + 1);
		leavesAbsoluteMaxRadius = (byte) (2 + random.nextInt(2));
	}

	public void setLeavesAbsoluteMaxRadius(byte leavesAbsoluteMaxRadius) {
		this.leavesAbsoluteMaxRadius = leavesAbsoluteMaxRadius;
	}

	public void setLeavesSizeY(byte leavesSizeY) {
		this.leavesSizeY = leavesSizeY;
	}
}
