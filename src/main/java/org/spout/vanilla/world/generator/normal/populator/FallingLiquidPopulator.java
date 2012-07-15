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
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;

public class FallingLiquidPopulator extends Populator {
	private static final byte WATER_ATTEMPTS = 50;
	private static final byte LAVA_ATTEMPTS = 20;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int height = world.getHeight();
		for (byte count = 0; count < WATER_ATTEMPTS; count++) {
			final int x = chunk.getBlockX(random);
			final int y = random.nextInt(height) + 8;
			final int z = chunk.getBlockZ(random);
			final Block block = world.getBlock(x, y, z, world);
			if (isValidSourcePoint(block)) {
				block.setMaterial(VanillaMaterials.WATER);
			}
		}
		for (byte count = 0; count < LAVA_ATTEMPTS; count++) {
			final int x = chunk.getBlockX(random);
			final int y = random.nextInt(height) + 8;
			final int z = chunk.getBlockZ(random);
			final Block block = world.getBlock(x, y, z, world);
			if (isValidSourcePoint(block)) {
				block.setMaterial(VanillaMaterials.LAVA);
			}
		}
	}

	private boolean isValidSourcePoint(Block block) {
		if (!block.isMaterial(VanillaMaterials.STONE, VanillaMaterials.AIR)
				|| !block.translate(BlockFace.TOP).isMaterial(VanillaMaterials.STONE)
				|| !block.translate(BlockFace.BOTTOM).isMaterial(VanillaMaterials.STONE)) {
			return false;
		}
		byte adjacentStoneCount = 0;
		byte adjacentAirCount = 0;
		for (final BlockFace face : BlockFaces.NSEW) {
			final BlockMaterial material = block.translate(face).getMaterial();
			if (material == VanillaMaterials.AIR) {
				adjacentAirCount++;
			} else if (material == VanillaMaterials.STONE) {
				adjacentStoneCount++;
			}
		}
		return adjacentStoneCount == 3 && adjacentAirCount == 1;
	}
}
