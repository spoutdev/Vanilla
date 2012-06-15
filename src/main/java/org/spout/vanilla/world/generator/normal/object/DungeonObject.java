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
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

public class DungeonObject extends RandomObject {
	// dimensions
	private byte height = 6;
	private byte maxSize = 9;
	private byte minSize = 7;
	// blocks
	private BlockMaterial firstWallMaterial = VanillaMaterials.COBBLESTONE;
	private BlockMaterial secondWallMaterial = VanillaMaterials.MOSS_STONE;
	// extras
	private boolean addSpawner = true;
	private boolean addChests = true;

	public DungeonObject() {
		this(null);
	}
	
	public DungeonObject(Random random) {
		super(random);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return w.getBlockMaterial(x, y, z) != VanillaMaterials.AIR;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final byte width = random.nextBoolean() ? maxSize : minSize;
		final byte depth = random.nextBoolean() ? maxSize : minSize;
		for (byte xx = 0; xx < width; xx++) {
			for (byte yy = 0; yy < height; yy++) {
				for (byte zz = 0; zz < depth; zz++) {
					BlockMaterial material = VanillaMaterials.AIR;
					if (xx == 0 || xx == width - 1 || zz == 0 || zz == depth - 1) {
						material = VanillaMaterials.COBBLESTONE;
					}
					if (yy == 0 || yy == height - 1) {
						material = random.nextBoolean() ? firstWallMaterial : secondWallMaterial;
					}
					w.setBlockMaterial(x + xx, y + yy, z + zz, material, (short) 0, w);
				}
			}
		}
		if (addSpawner) {
			w.setBlockMaterial(x + width / 2, y + 1, z + depth / 2, VanillaMaterials.MONSTER_SPAWNER, (short) 0, w);
		}
		if (addChests) {
			w.setBlockMaterial(x + 1, y + 1, z + depth / 2, VanillaMaterials.CHEST, (short) 0, w);
			w.setBlockMaterial(x + width / 2, y + 1, z + 1, VanillaMaterials.CHEST, (short) 0, w);
			//TODO Fill Chests with stuff
		}
	}

	public void setHeight(byte height) {
		this.height = height;
	}

	public void setMaxSize(byte maxSize) {
		this.maxSize = maxSize;
	}

	public void setMinSize(byte minSize) {
		this.minSize = minSize;
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

	public void addSpawner(boolean addSpawner) {
		this.addSpawner = addSpawner;
	}
}
