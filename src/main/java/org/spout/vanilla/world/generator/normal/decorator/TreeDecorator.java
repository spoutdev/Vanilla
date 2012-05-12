/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.normal.object.SmallTreeObject;
import org.spout.vanilla.world.generator.normal.object.SmallTreeObject.SmallTreeType;

public class TreeDecorator implements BiomeDecorator {

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final Biome biome = chunk.getBiomeType(7, 7, 7);
		final SmallTreeObject tree = getTree(random, biome);
		if (tree == null) {
			return;
		}
		final World world = chunk.getWorld();
		final byte amount = getNumberOfTrees(biome);
		for (byte i = 0; i < amount; i++) {
			final int worldX = chunk.getX() * 16 + random.nextInt(16);
			final int worldZ = chunk.getZ() * 16 + random.nextInt(16);
			final int worldY = getHighestWorkableBlock(world, worldX, worldZ);
			if (!tree.canPlaceObject(world, worldX, worldY, worldZ)) {
				continue;
			} else {
				tree.placeObject(world, worldX, worldY, worldZ);
				tree.findNewRandomHeight();
			}
		}
	}

	private int getHighestWorkableBlock(World w, int x, int z) {
		int y = 127;
		while (w.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}

	// trees in jungle : 50
	//		jungle trees have a custom height of random.nextInt(3) + random.nextInt(7) + 4 (according to mc)
	// trees in forest : 10
	// trees in swamp : 2
	// plains don't have trees!!!
	private byte getNumberOfTrees(Biome biome) {
		if (biome == VanillaBiomes.FOREST) {
			return 10;
		} else if (biome == VanillaBiomes.SWAMP) {
			return 2;
		} else if (biome == VanillaBiomes.JUNGLE) {
			return 50;
		} else {
			return 0;
		}
	}

	private SmallTreeObject getTree(Random random, Biome biome) {
		if (biome == VanillaBiomes.FOREST) {
			return new SmallTreeObject(random, SmallTreeType.OAK);
		} else if (biome == VanillaBiomes.SWAMP) {
			final SmallTreeObject tree = new SmallTreeObject(random, SmallTreeType.OAK);
			tree.addVines(true);
			return tree;
		} else if (biome == VanillaBiomes.JUNGLE) {
			final SmallTreeObject tree = new SmallTreeObject(random, SmallTreeType.JUNGLE);
			tree.setBaseHeight((byte) 4);
			tree.setRandHeight((byte) 10);
			return tree;
		} else {
			return null;
		}
	}
}
