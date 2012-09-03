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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;
import org.spout.api.generator.Populator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;

public class GroundCoverPopulator extends Populator {
	public GroundCoverPopulator() {
		super(true);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		for (byte xx = 0; xx < Chunk.BLOCKS.SIZE; xx++) {
			for (byte zz = 0; zz < Chunk.BLOCKS.SIZE; zz++) {
				final Biome biome = chunk.getBiome(xx, 0, zz);
				if (biome instanceof NormalBiome) {
					final NormalBiome normalBiome = (NormalBiome) biome;
					int y = getNextStone(world, x + xx, NormalGenerator.HEIGHT, z + zz);
					while (y > 5) {
						y = getNextStone(world, x + xx, y, z + zz);
						y -= normalBiome.placeGroundCover(world, x + xx, y, z + zz);
						y = getNextAir(world, x + xx, y, z + zz);
					}
				}
			}
		}
	}

	private int getNextStone(World world, int x, int y, int z) {
		for (; !world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.STONE) && y > 0; y--) {
			// iterate until we reach stone
		}
		return y;
	}

	private int getNextAir(World world, int x, int y, int z) {
		for (; world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.STONE) && y > 0; y--) {
			// iterate until we exit the stone column
		}
		return y;
	}
}
