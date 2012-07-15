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

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;

public class SnowAndIceDecorator extends Decorator {
	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		for (byte xx = 0; xx < 16; xx++) {
			for (byte zz = 0; zz < 16; zz++) {
				final Block block = world.getBlock(x + xx, world.getSurfaceHeight(x + xx, z + zz) + 1, z + zz, world);
				final Biome target = block.getBiomeType();
				if (target instanceof VanillaBiome && ((VanillaBiome) target).getClimate() == Climate.COLD) {
					if (block.isMaterial(VanillaMaterials.AIR)) {
						final Block under = block.translate(BlockFace.BOTTOM);
						if (under.isMaterial(VanillaMaterials.SNOW, VanillaMaterials.ICE)) {
							continue;
						}
						if (under.isMaterial(VanillaMaterials.WATER, VanillaMaterials.STATIONARY_WATER)) {
							under.setMaterial(VanillaMaterials.ICE);
						} else {
							block.setMaterial(VanillaMaterials.SNOW);
						}
					}
				}
			}
		}
	}
}
