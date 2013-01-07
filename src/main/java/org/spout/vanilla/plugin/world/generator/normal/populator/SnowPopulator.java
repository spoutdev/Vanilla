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
package org.spout.vanilla.plugin.world.generator.normal.populator;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.plugin.world.generator.normal.object.SnowObject;

public class SnowPopulator extends Populator {
	private static final Perlin SNOW_HEIGHT = new Perlin();

	static {
		SNOW_HEIGHT.setFrequency(0.2D);
		SNOW_HEIGHT.setLacunarity(1D);
		SNOW_HEIGHT.setNoiseQuality(NoiseQuality.STANDARD);
		SNOW_HEIGHT.setPersistence(0.7D);
		SNOW_HEIGHT.setOctaveCount(1);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int seed = (int) (world.getSeed() * 51);
		SNOW_HEIGHT.setSeed(seed);
		final SnowObject snow = new SnowObject();
		snow.setRandom(random);
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		double[][] heights = WorldGeneratorUtils.fastNoise(SNOW_HEIGHT, 16, 16, 4, x, 63, z);
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				if (!snow.canPlaceObject(world, x + xx, 63, z + zz)) {
					continue;
				}
				int count = (int) ((heights[xx][zz] + 1) * 4.0);
				for (int i = 0; i < count; i++) {
					snow.placeObject(world, x + xx, 0, z + zz);
				}
			}
		}
	}
}
