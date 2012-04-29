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

import org.spout.vanilla.material.VanillaMaterials;

public class CactusDecorator implements BiomeDecorator {
	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		int genInChunk = random.nextInt(100);
		if (genInChunk <= 20) {
			return;
		}
		int px = random.nextInt(16);
		int pz = random.nextInt(16);
		int py = getHighestWorkableBlock(chunk, px, pz);
		px = chunk.getX() * 16 + px;
		pz = chunk.getZ() * 16 + pz;
		if (py == -1) {
			return;
		}
		int height = random.nextInt(3) + 1;
		for (int i = py; i < py + height; ++i) {
			chunk.getBlock(px, i, pz).setMaterial(VanillaMaterials.CACTUS).update(true);
		}
	}

	private int getHighestWorkableBlock(Chunk c, int px, int pz) {
		int y = c.getWorld().getHeight();
		int pozx = c.getX() * 16 + px;
		int pozz = c.getZ() * 16 + pz;
		while (c.getWorld().getBlockMaterial(pozx, y, pozz) != VanillaMaterials.SAND && c.getWorld().getBlockMaterial(pozx, y, pozz) != VanillaMaterials.SANDSTONE) {
			y--;
			if (y == 0 || c.getWorld().getBlockMaterial(pozx, y, pozz) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
