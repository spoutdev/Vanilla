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
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.VanillaMaterials;

public class DungeonDecorator implements BiomeDecorator {
	private final static int HEIGHT = 6;
	private final static int PROBABILITY = 2000;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (random.nextInt(PROBABILITY) == 0) {
			final int width = random.nextBoolean() ? 9 : 7;
			final int height = random.nextBoolean() ? 9 : 7;

			int cx = chunk.getX() * 16 + random.nextInt(16);
			int cy = chunk.getY() * 16 + random.nextInt(16);
			int cz = chunk.getZ() * 16 + random.nextInt(16);

			if (chunk.getWorld().getBlockMaterial(cx, cy, cz) == VanillaMaterials.AIR) {
				return; //No dungeons in the air, plox!
			}

			for (int x = cx; x < cx + width; x++) {
				for (int y = cy; y < cy + HEIGHT; y++) {
					for (int z = cz; z < cz + height; z++) {
						BlockMaterial material = VanillaMaterials.AIR;
						if (x == cx || x == cx + width - 1 || z == cz || z == cz + height - 1) {
							material = VanillaMaterials.COBBLESTONE;
						}
						if (y == cy || y == cy + HEIGHT - 1) {
							material = random.nextBoolean() ? VanillaMaterials.COBBLESTONE : VanillaMaterials.MOSS_STONE;
						}
						chunk.getWorld().setBlockMaterial(x, y, z, material, (short) 0, true, chunk.getWorld());
					}
				}
			}

			chunk.getWorld().setBlockMaterial(cx + width / 2, cy + 1, cz + height / 2, VanillaMaterials.MONSTER_SPAWNER, (short) 0, true, chunk.getWorld());

			chunk.getWorld().setBlockMaterial(cx + 1, cy + 1, cz + height / 2, VanillaMaterials.CHEST, (short) 0, true, chunk.getWorld());

			chunk.getWorld().setBlockMaterial(cx + width / 2, cy + 1, cz + 1, VanillaMaterials.CHEST, (short) 0, true, chunk.getWorld());

			//TODO Fill Chests with stuff, kinda waiting for inventories in worlds.
		}
	}
}
