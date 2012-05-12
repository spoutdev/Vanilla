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
package org.spout.vanilla.world.generator.theend.decorator;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.controller.object.misc.EnderCrystal;
import org.spout.vanilla.material.VanillaMaterials;

import java.util.Random;

public class SpireDecorator implements BiomeDecorator {
	// Generation odds, chunk per 'ODD' chunk
	private static final byte ODD = 5;
	// These control the height of the spires
	private static final byte BASE_HEIGHT = 6;
	private static final byte RAND_HEIGHT = 32;
	// These control the radius of the spires
	private static final byte BASE_RADIUS = 2;
	private static final byte RAND_RADIUS = 4;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		if (random.nextInt(ODD) != 0) {
			return;
		}
		int x = random.nextInt(16) + 8;
		int z = random.nextInt(16) + 8;
		int y = getHighestWorkableBlock(chunk, x, z);
		if (y == -1) {
			return;
		}
		generateSpire(chunk.getWorld(), random, chunk.getX() * 16 + x, chunk.getY() * 16 + y, chunk.getZ() * 16 + z);
	}

	private void generateSpire(World world, Random random, int x, int y, int z) {
		final byte radius = (byte) (random.nextInt(RAND_RADIUS) + BASE_RADIUS);
		x -= radius;
		z -= radius;
		final byte height = (byte) (random.nextInt(RAND_HEIGHT) + BASE_HEIGHT);
		final short radiusSquare = (short) (radius * radius + 1);
		final byte diameter = (byte) (radius * 2);
		if (!canBuildSpire(world, x, y, z, diameter, radiusSquare)) {
			return;
		}
		for (byte xx = (byte) -radius; xx <= diameter; xx++) {
			for (byte zz = (byte) -radius; zz <= diameter; zz++) {
				if (xx * xx + zz * zz <= radiusSquare) {
					for (byte yy = 0; yy < height; yy++) {
						world.getBlock(x + xx, y + yy, z + zz).setMaterial(VanillaMaterials.OBSIDIAN).update(true);
					}
				}
			}
		}
		world.getBlock(x, y + height, z).setMaterial(VanillaMaterials.BEDROCK).update(true);
		spawnEnderCrystal(world, (float) x + 0.5f, y + height, (float) z + 0.5f);
	}

	private boolean canBuildSpire(World world, int x, int y, int z, byte diameter, short radiusSquare) {
		for (byte xx = 0; xx < diameter; xx++) {
			for (byte zz = 0; zz < diameter; zz++) {
				if (xx * xx + zz * zz <= radiusSquare) {
					if (world.getBlockMaterial(x + xx, y - 1, z + zz) == VanillaMaterials.AIR) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private void spawnEnderCrystal(World world, float x, int y, float z) {
		world.createAndSpawnEntity(new Point(world, x, y, z), new EnderCrystal());
	}

	private int getHighestWorkableBlock(Chunk c, int x, int z) {
		byte y = 15;
		while (c.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}
}

