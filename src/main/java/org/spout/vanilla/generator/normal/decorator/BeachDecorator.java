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
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.vanilla.VanillaMaterials;

public class BeachDecorator implements BiomeDecorator {

	@Override
	public void populate(Chunk c, Random r) {
		if (c.getY() != 3) {
			return; //no work for us here.
		}
		for (int x = c.getX() * 16; x < c.getX() * 16 + Chunk.CHUNK_SIZE; x++) {
			for (int z = c.getZ() * 16; z < c.getZ() * 16 + Chunk.CHUNK_SIZE; z++) {
				for (int y = 65; y >= 63; y--) {
					BlockMaterial current = c.getWorld().getBlockMaterial(x, y, z);
					if (!current.equals(VanillaMaterials.GRASS)) {
						continue;
					}

					BlockFace faces[] = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};
					for (BlockFace face : faces) {
						int tx = (int) (x + face.getOffset().getX());
						int tz = (int) (z + face.getOffset().getZ());
						BlockMaterial mat = c.getWorld().getBlockMaterial(tx, y, tz);
						if (mat.equals(VanillaMaterials.STATIONARY_WATER) || mat.equals(VanillaMaterials.WATER)) {
							placeSandBall(c.getWorld(), x, y, z, 4);
							break;
						}
					}
				}
			}
		}
	}

	public void placeSandBall(World world, int x, int y, int z, int radius) {
		while (radius > 0) {
			BlockMaterial existing = world.getBlockMaterial(x, y, z);
			if (existing.equals(VanillaMaterials.GRASS)) {
				world.setBlockMaterial(x, y, z, VanillaMaterials.SAND, world);
			} else if ((existing.equals(VanillaMaterials.WATER) || existing.equals(VanillaMaterials.STATIONARY_WATER)) && world.getBlockMaterial(x, y - 1, z).equals(VanillaMaterials.DIRT)) {
				world.setBlockMaterial(x, y - 1, z, VanillaMaterials.SAND, world);
			} else {
				BlockFace faces[] = {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.BOTTOM, BlockFace.TOP};
				radius--;
				if (radius > 0) {
					for (BlockFace face : faces) {
						int tx = (int) (x + face.getOffset().getX());
						int ty = (int) (y + face.getOffset().getY());
						int tz = (int) (z + face.getOffset().getZ());
						placeSandBall(world, tx, ty, tz, radius);
					}
				}
			}
		}
	}
}
