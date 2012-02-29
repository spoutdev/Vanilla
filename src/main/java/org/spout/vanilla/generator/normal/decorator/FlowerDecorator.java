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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.generator.normal.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.VanillaMaterials;

/**
 * Decorator that decorates a biome with flowers.
 */
public class FlowerDecorator implements BiomeDecorator {

	private static final List<BlockMaterial> flowers = new ArrayList<BlockMaterial>();

	static {
		flowers.add(VanillaMaterials.DANDELION);
		flowers.add(VanillaMaterials.ROSE);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		int howMany = random.nextInt(15);
		BlockMaterial flower = getRandomFlower(random);
		for (int i = 0; i < howMany; i++) {
			int dx = random.nextInt(16);
			int dz = random.nextInt(16);
			int dy = getHighestWorkableBlock(chunk, dx, dz);
			if (dy == -1) {
				continue;
			}
			chunk.setBlockMaterial(dx, dy, dz, flower, chunk.getWorld());
		}
	}

	private BlockMaterial getRandomFlower(Random random) {
		int which = random.nextInt(flowers.size());
		return flowers.get(which);
	}

	private int getHighestWorkableBlock(Chunk c, int px, int pz) {
		int y = 15;
		while (c.getBlockMaterial(px, y, pz) != VanillaMaterials.GRASS) {
			y--;
			if (y == 0 || c.getBlockMaterial(px, y, pz) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
