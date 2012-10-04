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
package org.spout.vanilla.world.generator.flat;

import java.util.Random;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.Populator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaGenerator;

public class FlatGenerator implements VanillaGenerator {
	private final int height;

	public FlatGenerator(int height) {
		this.height = height;
	}

	@Override
	public void generate(CuboidShortBuffer blockData, int chunkX, int chunkY, int chunkZ, World world) {
		int x = chunkX << 4, z = chunkZ << 4;
		for (int dx = x; dx < x + 16; ++dx) {
			for (int dz = z; dz < z + 16; ++dz) {
				final int startY = chunkY << Chunk.BLOCKS.BITS;
				final int endY = Math.min(Chunk.BLOCKS.SIZE + startY, height);
				for (int y = startY; y < endY; y++) {
					if (y <= 0) {
						blockData.set(dx, y, dz, VanillaMaterials.BEDROCK.getId());
					} else if (y == height - 1) {
						blockData.set(dx, y, dz, VanillaMaterials.GRASS.getId());
					} else {
						blockData.set(dx, y, dz, VanillaMaterials.DIRT.getId());
					}
				}
			}
		}
	}

	@Override
	public Populator[] getPopulators() {
		return new Populator[0];
	}

	@Override
	public GeneratorPopulator[] getGeneratorPopulators() {
		return new GeneratorPopulator[0];
	}

	@Override
	public String getName() {
		return "VanillaFlat";
	}

	@Override
	public Point getSafeSpawn(World world) {
		final Random random = new Random();
		final int x = 16 - random.nextInt(32);
		final int z = 16 - random.nextInt(32);
		int y = world.getHeight();
		for (; !world.getBlockMaterial(x, y, z).isSolid(); y--) {
		}
		return new Point(world, x, y + 1.5f, z);
	}

	@Override
	public int[][] getSurfaceHeight(World world, int chunkX, int chunkZ) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
				heights[x][z] = height - 1;
			}
		}
		return heights;
	}
}
