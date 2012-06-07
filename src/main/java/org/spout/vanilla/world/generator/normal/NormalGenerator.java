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
package org.spout.vanilla.world.generator.normal;

import java.util.Random;

import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.generator.biome.BiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.VanillaGenerator;
import org.spout.vanilla.world.selector.VanillaBiomeSelector;

public class NormalGenerator extends BiomeGenerator implements VanillaGenerator {
	private BiomeSelector selector;
	public final static int SEA_LEVEL = 63;

	@Override
	public void registerBiomes() {
		selector = new VanillaBiomeSelector(2.0, 2.0, 0.35, 0.05, false);
		// if you want to check out a particular biome, use this!
		//selector = new PerBlockBiomeSelector(VanillaBiomes.DESERT);
		setSelector(selector);
		//addPopulator(new SmoothPopulator());
		register(VanillaBiomes.OCEAN);
		register(VanillaBiomes.PLAIN);
		register(VanillaBiomes.DESERT);
		register(VanillaBiomes.MOUNTAINS);
		register(VanillaBiomes.BEACH);
		register(VanillaBiomes.FOREST);
		register(VanillaBiomes.SMALL_MOUNTAINS);
	}

	@Override
	public String getName() {
		return "VanillaNormal";
	}

	@Override
	public Point getSafeSpawn(World world) {
		final Random random = new Random();

		//Moves the spawn out of the ocean (and likely on to a beach, as in MC).
		int shift = 0;
		while (selector.pickBiome(shift, 0, world.getSeed()) == VanillaBiomes.OCEAN && shift < 16000) {
			shift += 16;
		}

		for (byte attempts = 0; attempts < 10; attempts++) {
			final int x = random.nextBoolean() ? -random.nextInt(16) : random.nextInt(16);
			final int z = random.nextBoolean() ? -random.nextInt(16) : random.nextInt(16);
			final int y = getHighestSolidBlock(world, x + shift, z);
			if (y != -1) {
				return new Point(world, x + shift, y + 0.5f, z);
			}
		}
		return new Point(world, shift, 80, 0);
	}

	private int getHighestSolidBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) instanceof Liquid) {
				return -1;
			}
		}
		return y + 2;
	}

	@Override
	public int[][] getSurfaceHeight(World world, int chunkX, int chunkY) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
				heights[x][z] = SEA_LEVEL;
			}
		}
		return heights;
	}
}
