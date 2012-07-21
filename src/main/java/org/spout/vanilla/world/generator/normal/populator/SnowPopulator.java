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

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.Populator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.normal.object.SnowObject;

public class SnowPopulator extends Populator {
	private static final Perlin SNOW_HEIGHT= new Perlin();
	
	static {
		SNOW_HEIGHT.setFrequency(0.2D);
		SNOW_HEIGHT.setLacunarity(1D);
		SNOW_HEIGHT.setNoiseQuality(NoiseQuality.STANDARD);
		SNOW_HEIGHT.setPersistence(0.7D);
		SNOW_HEIGHT.setOctaveCount(1);
	}
	
	@Override
	public void populate(Chunk chunk, Random random) {
		SnowObject snowObject = new SnowObject(random);
		final int seed = (int) chunk.getWorld().getSeed();
		SNOW_HEIGHT.setSeed(seed);
		
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				final Block block = world.getBlock(x + xx, world.getHeight() - 1, z + zz, world);
				if (block.getBiomeType() instanceof VanillaBiome) {
					VanillaBiome biome = (VanillaBiome) block.getBiomeType();
					if (biome.getClimate() != Climate.COLD) {
						continue;
					}
				}
				
				int count = (int) ((SNOW_HEIGHT.GetValue(x + xx + 0.001, 0.123, z + zz + 0.1) + 1.0) * 4.0);
				for (int i = 0; i < count; i++) {
					snowObject.placeObject(world, x + xx, 0, z + zz);
				}
			}
		}
	}
}
