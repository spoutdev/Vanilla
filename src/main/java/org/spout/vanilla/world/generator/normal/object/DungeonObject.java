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
package org.spout.vanilla.world.generator.normal.object;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.misc.Dye;
import org.spout.vanilla.material.item.misc.MusicDisc;
import org.spout.vanilla.world.generator.object.RandomObject;

public class DungeonObject extends RandomObject {
	// base dimensions
	private byte height = 3;
	private byte baseRadius = 2;
	private byte randRadius = 2;
	// randomly generated dimension
	private byte radiusX;
	private byte radiusZ;
	// blocks
	private BlockMaterial firstWallMaterial = VanillaMaterials.COBBLESTONE;
	private BlockMaterial secondWallMaterial = VanillaMaterials.MOSS_STONE;
	// extras
	private boolean addSpawner = true;
	private boolean addChests = true;
	private byte maxNumberOfChests = 2;
	private final LootChestObject chestObject;

	public DungeonObject() {
		this(null);
	}

	public DungeonObject(Random random) {
		super(random);
		randomizeRadius();
		final double ELEVEN = 1.0 / 11.0;
		chestObject = new LootChestObject(random);
		chestObject.setMaxNumberOfStacks(8);
		chestObject.addMaterial(VanillaMaterials.SADDLE, 		ELEVEN, 		1, 1)
					.addMaterial(VanillaMaterials.IRON_INGOT, 	ELEVEN, 		1, 4)
					.addMaterial(VanillaMaterials.BREAD, 		ELEVEN, 		1, 2)
					.addMaterial(VanillaMaterials.WHEAT, 		ELEVEN, 		1, 4)
					.addMaterial(VanillaMaterials.GUNPOWDER, 	ELEVEN, 		1, 4)
					.addMaterial(VanillaMaterials.STRING, 		ELEVEN, 		1, 4)
					.addMaterial(VanillaMaterials.BUCKET, 		ELEVEN, 		1, 1)
					.addMaterial(Dye.COCOA_BEANS, 				ELEVEN, 		1, 3)
					.addMaterial(VanillaMaterials.REDSTONE_DUST, 1.0 / 22.0, 	1, 4)
					.addMaterial(VanillaMaterials.GOLDEN_APPLE, 1.0 / 1100.0, 	1, 1);
		final double discProbability = (1.0 / 110.0) / (double) MusicDisc.values().length;
		for (Material disc:MusicDisc.values()) {
			chestObject.addMaterial(disc, discProbability, 						1, 1);
		}
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		byte unsuportedEdgeBlockCount = 0;
		for (byte yy = -1; yy < height + 2; yy++) {
			for (byte xx = (byte) (-radiusX - 1); xx < radiusX + 2; xx++) {
				for (byte zz = (byte) (-radiusZ - 1); zz < radiusZ + 2; zz++) {
					BlockMaterial material = w.getBlockMaterial(x + xx, y + yy, z + zz);
					if ((yy == -1 || yy == height + 1) && material == VanillaMaterials.AIR) {
						return false;
					}
					if ((xx == -radiusX - 1 || xx == radiusX + 1
							|| zz == -radiusZ - 1 || zz == radiusZ + 1)
							&& yy == 0 && w.getBlockMaterial(x + xx, y + yy, z + zz) == VanillaMaterials.AIR
							&& w.getBlockMaterial(x + xx, y + yy + 1, z + zz) == VanillaMaterials.AIR) {
						unsuportedEdgeBlockCount++;
					}
				}
			}
		}
		return unsuportedEdgeBlockCount > 0 && unsuportedEdgeBlockCount < 6;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		for (int yy = height; yy > -2; yy--) {
			for (byte xx = (byte) (-radiusX - 1); xx < radiusX + 2; xx++) {
				for (byte zz = (byte) (-radiusZ - 1); zz < radiusZ + 2; zz++) {
					if (xx == -radiusX - 1 || yy == -1 || zz == -radiusZ - 1
							|| xx == radiusX + 1 || yy == height + 1 || zz == radiusZ + 1) {
						if (yy > -1 && w.getBlockMaterial(x + xx, y + yy - 1, z + zz) == VanillaMaterials.AIR) {
							w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.AIR, (short) 0, w);
							continue;
						}
						if (w.getBlockMaterial(x + xx, y + yy, z + zz) == VanillaMaterials.AIR) {
							continue;
						}
						if (yy == -1 && random.nextInt(4) != 0) {
							w.setBlockMaterial(x + xx, y + yy, z + zz, secondWallMaterial, (short) 0, w);
						} else {
							w.setBlockMaterial(x + xx, y + yy, z + zz, firstWallMaterial, (short) 0, w);
						}
					} else {
						w.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.AIR, (short) 0, w);
					}
				}
			}
		}
		if (addChests) {
			chestObject.setRandom(random);
			byte chestCount = 0;
			for (byte attempts = 0; attempts < 6; attempts++) {
				final int xx = random.nextInt(radiusX * 2 + 1) - radiusX + x;
				final int zz = random.nextInt(radiusZ * 2 + 1) - radiusZ + z;
				final Block middle = w.getBlock(xx, y, zz, w);
				if (middle.getMaterial() != VanillaMaterials.AIR) {
					continue;
				}
				byte adjacentSolidBlockCount = 0;
				for (final BlockFace face : BlockFaces.NESW) {
					if (middle.translate(face).getMaterial() != VanillaMaterials.AIR) {
						adjacentSolidBlockCount++;
					}
				}
				if (adjacentSolidBlockCount != 1) {
					continue;
				}
				chestObject.placeObject(w, xx, y, zz);
				chestCount ++;
				if (chestCount == maxNumberOfChests) {
					break;
				}
			}
		}
		if (addSpawner) {
			w.setBlockMaterial(x, y, z, VanillaMaterials.MONSTER_SPAWNER, (short) 0, w);
		}
	}

	public void setHeight(byte height) {
		this.height = height;
	}

	public final void randomizeRadius() {
		radiusX = (byte) (random.nextInt(randRadius) + baseRadius);
		radiusZ = (byte) (random.nextInt(randRadius) + baseRadius);
	}

	public void setBaseRadius(byte baseRadius) {
		this.baseRadius = baseRadius;
	}

	public void setRandRadius(byte randRadius) {
		this.randRadius = randRadius;
	}

	public void setRadiusX(byte radiusX) {
		this.radiusX = radiusX;
	}

	public void setRadiusZ(byte radiusZ) {
		this.radiusZ = radiusZ;
	}

	public void setFirstWallMaterial(BlockMaterial firstWallMaterial) {
		this.firstWallMaterial = firstWallMaterial;
	}

	public void setSecondWallMaterial(BlockMaterial secondWallMaterial) {
		this.secondWallMaterial = secondWallMaterial;
	}

	public void addChests(boolean addChests) {
		this.addChests = addChests;
	}

	public void setMaxNumberOfChests(byte maxNumberOfChests) {
		this.maxNumberOfChests = maxNumberOfChests;
	}

	public void addSpawner(boolean addSpawner) {
		this.addSpawner = addSpawner;
	}

	@Override
	public void randomize() {
		randomizeRadius();
	}
}
