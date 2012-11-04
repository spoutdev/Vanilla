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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.plant.Sapling;

public class SmallTreeObject extends TreeObject {
	//size control
	private byte leavesHeight = 3;
	protected byte radiusIncrease = 0;
	// extras
	private boolean addLeavesVines = false;
	private boolean addLogVines = false;
	private boolean addCocoaPlants = false;

	public SmallTreeObject() {
		this(null);
	}

	public SmallTreeObject(Random random) {
		super(random, (byte) 4, (byte) 3, (short) 0);
		overridable.add(VanillaMaterials.AIR);
		overridable.add(VanillaMaterials.LEAVES);
		overridable.add(Sapling.DEFAULT);
		overridable.add(Sapling.BIRCH);
		overridable.add(Sapling.JUNGLE);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		if (!super.canPlaceObject(w, x, y, z)) {
			return false;
		}
		byte radiusToCheck = radiusIncrease;
		for (byte yy = 0; yy < totalHeight + 2; yy++) {
			if (yy == 1 || yy == totalHeight - 1) {
				radiusToCheck++;
			}
			for (byte xx = (byte) -radiusToCheck; xx < radiusToCheck + 1; xx++) {
				for (byte zz = (byte) -radiusToCheck; zz < radiusToCheck + 1; zz++) {
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
		w.setBlockMaterial(x, y - 1, z, VanillaMaterials.DIRT, (short) 0, null);
		for (byte yy = (byte) (totalHeight - leavesHeight); yy < totalHeight + 1; yy++) {
			final byte yRadius = (byte) (yy - totalHeight);
			final byte xzRadius = (byte) ((radiusIncrease + 1) - yRadius / 2);
			for (byte xx = (byte) -xzRadius; xx < xzRadius + 1; xx++) {
				for (byte zz = (byte) -xzRadius; zz < xzRadius + 1; zz++) {
					final BlockMaterial material = w.getBlockMaterial(x + xx, y + yy, z + zz);
					if (Math.abs(xx) != xzRadius || Math.abs(zz) != xzRadius
							|| random.nextBoolean() && yRadius != 0
							&& !(material instanceof Solid || material instanceof Liquid)) {
						w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.LEAVES, leavesMetadata, null);
					}
				}
			}
		}
		for (byte yy = 0; yy < totalHeight; yy++) {
			w.setBlockMaterial(x, y + yy, z, VanillaMaterials.LOG, logMetadata, null);
			if (addLogVines) {
				placeVines(w, x, y + yy, z, (byte) 3, false);
			}
		}
		if (addLeavesVines) {
			for (byte yy = (byte) (totalHeight - leavesHeight); yy < totalHeight + 1; yy++) {
				final byte yRadius = (byte) (yy - totalHeight);
				final byte xzRadius = (byte) ((radiusIncrease + 2) - yRadius / 2);
				for (byte xx = (byte) -xzRadius; xx < xzRadius + 1; xx++) {
					for (byte zz = (byte) -xzRadius; zz < xzRadius + 1; zz++) {
						if (w.getBlockMaterial(x + xx, y + yy, z + zz) == VanillaMaterials.LEAVES) {
							placeVines(w, x + xx, y + yy, z + zz, (byte) 4, true);
						}
					}
				}
			}
		}
		if (addCocoaPlants) {
			if (totalHeight >= 6 && random.nextInt(5) == 0) {
				for (byte yy = 0; yy < 2; yy++) {
					final byte odd = (byte) (4 - yy);
					final Vector3 position = new Vector3(x, y + yy, z);
					for (BlockFace face : BlockFaces.NSEW) {
						if (random.nextInt(odd) != 0) {
							continue;
						}
						final Block block = w.getBlock(position.add(face.getOffset()));
						block.setMaterial(VanillaMaterials.COCOA_PLANT);
						VanillaMaterials.COCOA_PLANT.setAttachedFace(block, face.getOpposite(), null);
						VanillaMaterials.COCOA_PLANT.setGrowthStage(block, random.nextInt(3));
					}
				}
			}
		}
	}

	private void placeVines(World w, int x, int y, int z, byte faceOdd, boolean grow) {
		final byte lenght = (byte) (grow ? 5 : 1);
		for (BlockFace face : BlockFaces.NSEW) {
			if (random.nextInt(faceOdd) != 0) {
				continue;
			}
			final BlockFace facing = face.getOpposite();
			for (byte yy = 0; yy < lenght; yy++) {
				final Block block = w.getBlock(face.getOffset().add(x, y - yy, z));
				if (block.isMaterial(VanillaMaterials.AIR)) {
					block.setMaterial(VanillaMaterials.VINES);
					VanillaMaterials.VINES.setFaceAttached(block, facing, true);
				} else {
					break;
				}
			}
		}
	}

	public void addLogVines(boolean addVines) {
		this.addLogVines = addVines;
	}

	public void addLeavesVines(boolean addLeavesVines) {
		this.addLeavesVines = addLeavesVines;
	}

	public void setLeavesXZRadiusIncrease(byte radiusIncrease) {
		this.radiusIncrease = radiusIncrease;
	}

	public void addCocoaPlants(boolean addCocoaPlants) {
		this.addCocoaPlants = addCocoaPlants;
	}
}
