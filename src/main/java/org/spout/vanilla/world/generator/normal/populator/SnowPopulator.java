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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.GenericMath;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Snow;
import org.spout.vanilla.world.generator.biome.VanillaBiome;

public class SnowPopulator extends Populator {
	private static final Perlin HEIGHTS = new Perlin();
	private static final Clamp SNOW_HEIGHTS = new Clamp();

	static {
		HEIGHTS.setFrequency(0.1);
		HEIGHTS.setNoiseQuality(NoiseQuality.BEST);
		HEIGHTS.setOctaveCount(2);

		SNOW_HEIGHTS.SetSourceModule(0, HEIGHTS);
		SNOW_HEIGHTS.setLowerBound(-1);
		SNOW_HEIGHTS.setUpperBound(1);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		HEIGHTS.setSeed((int) world.getSeed() * 51);
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		final double[][] heights = WorldGeneratorUtils.fastNoise(SNOW_HEIGHTS, 16, 16, 4, x, 0, z);
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				if (((VanillaBiome) world.getBiome(x + xx, 63, z + zz)).getClimate().hasSnowfall()) {
					final int y = getHighestWorkableBlock(world, x + xx, z + zz);
					if (y != -1 && ((VanillaBlockMaterial) world.getBlockMaterial(x + xx, y - 1, z + zz)).
							canSupport(VanillaMaterials.SNOW, BlockFace.TOP)) {
						// normalize [-1, 1] to [0, 1] then scale to [0, 7]
						final short height = (short) GenericMath.floor((heights[xx][zz] * 0.5 + 0.5) * 7);
						world.setBlockMaterial(x + xx, y, z + zz, Snow.SNOW[height], height, null);
					}
				}
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = world.getSurfaceHeight(x, z) + 16;
		while (world.getBlockMaterial(x, y, z).isInvisible()) {
			if (--y <= 0) {
				return -1;
			}
		}
		return ++y;
	}
}
