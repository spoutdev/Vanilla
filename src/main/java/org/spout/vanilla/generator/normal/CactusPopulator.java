/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.generator.normal;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.biome.BiomeDecorator;

import java.util.Random;

public class CactusPopulator extends BiomeDecorator {
	@Override
	public void decorate(Chunk c) {
		if (c.getY() != 4) {
			return;
		}
		Random ra = new Random();
		int genInChunk = ra.nextInt(100);
		if (genInChunk <= 20) {
			return;
		}
		int px = ra.nextInt(16);
		int pz = ra.nextInt(16);
		int py = getHighestWorkableBlock(c, px, pz);
		px = c.getX() * 16 + px;
		pz = c.getZ() * 16 + pz;
		if (py == -1) {
			return;
		}
		int height = ra.nextInt(4) + 1;
		for (int i = py; i < py + height; ++i) {
			c.getWorld().setBlockId(px, i, pz, VanillaMaterials.CACTUS.getId(), c.getWorld());
		}
	}

	private int getHighestWorkableBlock(Chunk c, int px, int pz) {
		int y = c.getWorld().getHeight();
		int pozx = c.getX() * 16 + px;
		int pozz = c.getZ() * 16 + pz;
		while (c.getWorld().getBlock(pozx, y, pozz).getBlockMaterial() != VanillaMaterials.SAND && c.getWorld().getBlock(pozx, y, pozz).getBlockMaterial() != VanillaMaterials.SANDSTONE) {
			y--;
			if (y == 0 || c.getWorld().getBlock(pozx, y, pozz).getBlockMaterial() == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
