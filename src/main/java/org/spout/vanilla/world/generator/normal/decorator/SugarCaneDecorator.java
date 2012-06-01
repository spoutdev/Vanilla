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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

/**
 * Decorator that decorates a biome with sugar canes.
 */
public class SugarCaneDecorator implements Decorator {
	/* How many times should we try to generate a sugar cane stack.
		 * Seems a lot, but vanilla mc does 10 reed decorations per chunk.
		 * Each decoration does 20 tries, so 200 tries total.
		 * We should stick at 50 for now.
		 */
	private static final short TRIES = 50;
	// Height control
	private static final byte BASE_HEIGHT = 2;
	private static final byte RAND_HEIGHT = 3;
	// Offset from main point control
	private static final byte RAND_X = 4;
	private static final byte RAND_Z = 4;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		int x = random.nextInt(16) + 8;
		int z = random.nextInt(16) + 8;
		int y = getHighestWorkableBlock(chunk, x, z);
		if (y == -1) {
			return;
		}
		final World world = chunk.getWorld();
		x += chunk.getBlockX();
		y += chunk.getBlockY();
		z += chunk.getBlockZ();
		for (int i = 0; i < TRIES; i++) {
			generateSugarCaneStack(world, random, x + (random.nextBoolean() ? random.nextInt(RAND_X) : -random.nextInt(RAND_X)), y, z + (random.nextBoolean() ? random.nextInt(RAND_Z) : -random.nextInt(RAND_Z)));
		}
	}

	private void generateSugarCaneStack(World world, Random random, int x, int y, int z) {
		if (!canPlaceSugarCane(world, x, y, z)) {
			return;
		}
		final int height = y + BASE_HEIGHT + random.nextInt(RAND_HEIGHT);
		for (; y < height; y++) {
			if (world.getBlockMaterial(x, y, z) != VanillaMaterials.AIR) {
				return;
			}
			world.getBlock(x, y, z).setMaterial(VanillaMaterials.SUGAR_CANE_BLOCK);
		}
	}

	private boolean canPlaceSugarCane(World world, int x, int y, int z) {
		BlockMaterial bellow = world.getBlockMaterial(x, y - 1, z);
		return (bellow == VanillaMaterials.DIRT || bellow == VanillaMaterials.GRASS || bellow == VanillaMaterials.SAND || bellow == VanillaMaterials.SUGAR_CANE_BLOCK) && world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR && (world.getBlockMaterial(x - 1, y - 1, z) == VanillaMaterials.STATIONARY_WATER || world.getBlockMaterial(x + 1, y - 1, z) == VanillaMaterials.STATIONARY_WATER || world.getBlockMaterial(x, y - 1, z - 1) == VanillaMaterials.STATIONARY_WATER || world.getBlockMaterial(x, y - 1, z + 1) == VanillaMaterials.STATIONARY_WATER);
	}

	private int getHighestWorkableBlock(Chunk c, int x, int z) {
		byte y = 15;
		while (c.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || c.getBlockMaterial(x, y, z) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
