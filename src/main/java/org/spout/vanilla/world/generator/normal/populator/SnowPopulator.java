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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;

public class SnowPopulator extends Populator {
	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		final int[][] snowHeights = new int[16][16];
		boolean hasSnow = false;
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				final Block block = world.getBlock(x + xx, world.getSurfaceHeight(x + xx, z + zz) + 1, z + zz, world);
				final Biome target = block.getBiomeType();
				if (target instanceof VanillaBiome && ((VanillaBiome) target).getClimate() == Climate.COLD) {
					if (block.isMaterial(VanillaMaterials.AIR)) {
						final Block under = block.translate(BlockFace.BOTTOM);
						if (under.isMaterial(VanillaMaterials.SNOW, VanillaMaterials.ICE)) {
							snowHeights[xx][zz] = (short) block.getY() << 16;
							continue;
						}
						hasSnow = true;
						snowHeights[xx][zz] = (short) block.getY() << 16 | (short) (random.nextInt(5) + 1);
					}
				} else {
					snowHeights[xx][zz] = (short) block.getY() << 16;
				}
			}
		}
		if (!hasSnow) {
			return;
		}
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				short totalSnowHeight = 0;
				short counter = 0;
				for (byte sx = -1; sx <= 1; sx++) {
					for (byte sz = -1; sz <= 1; sz++) {
						if (xx + sx >= 0 && xx + sx < 16 && zz + sz >= 0 && zz + sz < 16) {
							totalSnowHeight += (short) snowHeights[xx + sx][zz + sz];
							counter++;
						}
					}
				}
				snowHeights[xx][zz] = (short) (snowHeights[xx][zz] >> 16) << 16 | (short) (totalSnowHeight / counter);
			}
		}
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				final short height = (short) snowHeights[xx][zz];
				if (height > 0) {
					final Block block = world.getBlock(x + xx, (short) (snowHeights[xx][zz] >> 16), z + zz, world);
					block.setMaterial(VanillaMaterials.SNOW);
					block.setData(height - 1);
				}
			}
		}
	}
}
