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
package org.spout.vanilla.plugin.world.generator.normal.object.tree;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.SinusHelper;
import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.material.block.Liquid;
import org.spout.vanilla.plugin.material.block.Solid;
import org.spout.vanilla.plugin.material.block.plant.Sapling;
import org.spout.vanilla.plugin.util.MathHelper;

public class HugeTreeObject extends TreeObject {
	// size control
	private byte leavesGroupHeight = 2;
	private byte branchLength = 5;
	// extras
	private boolean addVines = true;

	public HugeTreeObject() {
		this(null);
	}

	public HugeTreeObject(Random random) {
		super(random, (byte) 10, (byte) 30, (short) 3);
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
		overridable.add(VanillaMaterials.DIRT);
		overridable.add(VanillaMaterials.GRASS);
		overridable.add(VanillaMaterials.LOG);
		overridable.add(Sapling.JUNGLE);
		overridable.add(Sapling.BIRCH);
		overridable.add(Sapling.DEFAULT);
		overridable.add(Sapling.SPRUCE);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		if (!super.canPlaceObject(w, x, y, z)) {
			return false;
		}
		byte checkRadius = 1;
		for (byte yy = 0; yy < totalHeight + 2; yy++) {
			if (yy == 1) {
				checkRadius++;
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
		generateLeaves(w, x, y + totalHeight, z, (byte) 2);
		final byte leavesEnd = (byte) (totalHeight - 2 - random.nextInt(4));
		for (byte yy = (byte) (totalHeight / 2); yy < leavesEnd; yy += random.nextInt(4) + 2) {
			Vector2 randomOffset = SinusHelper.getRandom2DAxis(random);
			generateLeaves(w, (int) (x + (randomOffset.getX() * 4f + 0.5f)), y + yy, (int) (z + (randomOffset.getY() * 4f + 0.5f)), (byte) 0);
			for (byte branchLengthCount = 0; branchLengthCount < branchLength; branchLengthCount++) {
				final int bx = (int) (randomOffset.getX() * branchLengthCount + 1.5f);
				final int by = -3 + branchLengthCount / 2;
				final int bz = (int) (randomOffset.getY() * branchLengthCount + 1.5f);
				final Block block = w.getBlock(x + bx, y + yy + by, z + bz);
				block.setMaterial(VanillaMaterials.LOG);
				block.setData(logMetadata);
				VanillaMaterials.LOG.setFacing(block, BlockFace.fromYaw(MathHelper.getLookAtYaw(new Vector3(bx, by, bz))));
			}
		}
		for (byte yy = -1; yy < totalHeight - 1; yy++) {
			for (byte xx = 0; xx < 2; xx++) {
				for (byte zz = 0; zz < 2; zz++) {
					if (yy == -1) {
						w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.DIRT, (short) 0, null);
					} else {
						w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.LOG, logMetadata, null);
						if (addVines && yy > 0) {
							placeVines(w, x + xx, y + yy, z + zz, (byte) 3);
						}
					}
				}
			}
		}
		w.setBlockMaterial(x, y + totalHeight - 1, z, VanillaMaterials.LOG, logMetadata, null);
	}

	private void generateLeaves(World world, int x, int y, int z, byte sizeIncrease) {
		for (byte yy = (byte) -leavesGroupHeight; yy < 1; yy++) {
			final byte radius = (byte) (sizeIncrease - yy + 1);
			for (byte xx = (byte) -radius; xx < radius + 1; xx++) {
				for (byte zz = (byte) -radius; zz < radius + 1; zz++) {
					final short circle = (short) (xx * xx + zz * zz - 1);
					final BlockMaterial material = world.getBlockMaterial(x + xx, y + yy, z + zz);
					if (!(material instanceof Solid || material instanceof Liquid)
							&& (xx > -1 || zz > -1 || circle < radius * radius)
							&& ((xx < 1 && zz < 1) || circle < (radius + 1) * (radius + 1))
							&& (random.nextInt(4) != 0 || circle < (radius - 1) * (radius - 1))) {
						world.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.LEAVES, leavesMetadata, null);
					}
				}
			}
		}
	}

	private void placeVines(World w, int x, int y, int z, byte faceOdd) {
		if (w.getBlockMaterial(x + 1, y, z) == VanillaMaterials.AIR && random.nextInt(faceOdd) != 0) {
			w.setBlockMaterial(x + 1, y, z, VanillaMaterials.VINES, (short) 2, null);
		}
		if (w.getBlockMaterial(x - 1, y, z) == VanillaMaterials.AIR && random.nextInt(faceOdd) > 0) {
			w.setBlockMaterial(x - 1, y, z, VanillaMaterials.VINES, (short) 8, null);
		}
		if (w.getBlockMaterial(x, y, z + 1) == VanillaMaterials.AIR && random.nextInt(faceOdd) > 0) {
			w.setBlockMaterial(x, y, z + 1, VanillaMaterials.VINES, (short) 4, null);
		}
		if (w.getBlockMaterial(x, y, z - 1) == VanillaMaterials.AIR && random.nextInt(faceOdd) > 0) {
			w.setBlockMaterial(x, y, z - 1, VanillaMaterials.VINES, (short) 1, null);
		}
	}

	public void addVines(boolean addVines) {
		this.addVines = addVines;
	}

	public void setBranchLength(byte branchLength) {
		this.branchLength = branchLength;
	}

	public void setLeavesGroupHeight(byte leavesGroupHeight) {
		this.leavesGroupHeight = leavesGroupHeight;
	}
}
