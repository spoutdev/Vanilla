/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;

public class ShrubDecorator implements BiomeDecorator {
	// How many shrub decorations per chunk
	private static final byte AMOUNT = 1;
	// Control the size of the shrubs (pyramids, basically)
	private static final byte SIZE = 2;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		if (random.nextInt(4) != 0) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = random.nextInt(16) + chunk.getX() * 16;
		final int z = random.nextInt(16) + chunk.getZ() * 16;
		final int y = getHighestWorkableBlock(world, x, z);
		if (y == -1) {
			return;
		}
		for (int i = 0; i < AMOUNT; i++) {
			generateShrub(world, random, x, y, z, (short) 3, (short) 0);
		}
	}

	private void generateShrub(World world, Random random, int x, int y, int z, short leaves, short wood) {
		if (canBuildShrub(world, x, y, z)) {
			return;
		}
		world.getBlock(x, y, z).setMaterial(VanillaMaterials.LOG, wood);
		for (byte yy = SIZE; yy > -1; yy--) {
			for (byte xx = (byte) -yy; xx < yy + 1; xx++) {
				for (byte zz = (byte) -yy; zz < yy + 1; zz++) {
					if (Math.abs(xx) == yy && Math.abs(zz) == yy && random.nextBoolean()) {
						continue;
					}
					Block block = world.getBlock(x + xx, y - yy + SIZE, z + zz);
					BlockMaterial material = block.getMaterial();
					if (!material.isOpaque() && material != VanillaMaterials.LOG) {
						block.setMaterial(VanillaMaterials.LEAVES, leaves);
					}
				}
			}
		}
	}

	private boolean canBuildShrub(World world, int x, int y, int z) {
		BlockMaterial material = world.getBlockMaterial(x, y - 1, z);
		return material == VanillaMaterials.DIRT || material == VanillaMaterials.GRASS;
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		BlockMaterial material;
		while ((material = world.getBlockMaterial(x, y, z)) == VanillaMaterials.AIR || material == VanillaMaterials.LEAVES) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
