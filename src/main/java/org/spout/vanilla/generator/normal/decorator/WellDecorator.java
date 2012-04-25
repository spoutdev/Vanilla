/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Decorator that decorates a biome with a well structure.
 */
public class WellDecorator implements BiomeDecorator {

	// generation odd, 'ODD' chunk per chunk
	private static final short ODD = 1000;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		if (random.nextInt(ODD) != 0) {
			return;
		}
		final int x = random.nextInt(16) + 8;
		final int z = random.nextInt(16) + 8;
		final int y = getHighestWorkableBlock(chunk, x, z);
		if (y == -1) {
			return;
		}
		generateDesertWell(chunk.getWorld(), chunk.getX() * 16 + x, chunk.getY() * 16 + y, chunk.getZ() * 16 + z);
	}

	private void generateDesertWell(World world, int x, int y, int z) {
		y -= 1;
		if (world.getBlockMaterial(x, y, z) != VanillaMaterials.SAND) {
			return;
		}
		if (!canBuildDesertWell(world, x, y, z)) {
			return;
		}
		for (int xx = x - 2; xx < x + 3; xx++) {
			for (int zz = z - 2; zz < z + 3; zz++) {
				for (int yy = y - 1; yy < y + 5; yy++) {
					if (yy > y - 2 && yy < y + 1) {
						world.setBlockMaterial(xx, yy, zz, VanillaMaterials.SANDSTONE, (short) 0, false, world);
					}
					if (yy == y) {
						if ((xx == x && z == zz)
								|| ((xx == x - 1 || xx == x + 1) && zz == z)
								|| ((zz == z + 1 || zz == z - 1) && x == xx)) {
							world.setBlockMaterial(xx, yy, zz, VanillaMaterials.WATER, (short) 0, false, world);
						}
					}
					if (yy == y + 1) {
						if (xx == x - 2 || xx == x + 2 || zz == z - 2 || zz == z + 2) {
							world.setBlockMaterial(xx, yy, zz, VanillaMaterials.SANDSTONE, (short) 0, false, world);
						}
						if (((xx == x - 2 || xx == x + 2) && zz == z)
								|| ((zz == z + 2 || zz == z - 2) && x == xx)) {
							world.setBlockMaterial(xx, yy, zz, VanillaMaterials.SLAB, (short) 1, false, world);
						}
					}
					if (yy == y + 4 && xx > x - 2 && xx < x + 2 && zz > z - 2 && zz < z + 2) {
						if (xx == 0 && zz == 0) {
							world.setBlockMaterial(xx, yy, zz, VanillaMaterials.SANDSTONE, (short) 0, false, world);
						} else {
							world.setBlockMaterial(xx, yy, zz, VanillaMaterials.SLAB, (short) 1, false, world);
						}
					}
					if (yy > y && yy < y + 4) {
						if ((xx == x - 1 || xx == x + 1) && (zz == z + 1 || zz == z - 1)) {
							world.setBlockMaterial(xx, yy, zz, VanillaMaterials.SANDSTONE, (short) 0, false, world);
						}
					}
				}
			}
		}
	}

	private boolean canBuildDesertWell(World world, int x, int y, int z) {
		for (int xx = x - 2; xx < x + 3; xx++) {
			for (int zz = z - 2; zz < z + 3; zz++) {
				Block block = world.getBlock(xx, y - 1, zz);
				if (block.getMaterial() == VanillaMaterials.AIR
						&& block.move(BlockFace.BOTTOM).getMaterial() == VanillaMaterials.AIR) {
					return false;
				}
			}
		}
		return true;
	}

	private int getHighestWorkableBlock(Chunk chunk, int x, int z) {
		int y = 15;
		while (chunk.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || chunk.getBlockMaterial(x, y, z) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
