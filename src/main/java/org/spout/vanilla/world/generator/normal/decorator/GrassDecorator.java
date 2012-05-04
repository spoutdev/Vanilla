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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;

public class GrassDecorator implements BiomeDecorator {
	private int minSteps = 7, maxSteps = 20, chance = 30;

	@Override
	public void populate(Chunk source, Random random) {

		if (source.getY() < 4) {
			return;
		}
		if (random.nextInt(100) > chance) {
			return;
		}

		int x = (source.getX() << Chunk.CHUNK_SIZE_BITS);
		int z = (source.getZ() << Chunk.CHUNK_SIZE_BITS);
		int y;
		int numSteps = random.nextInt(maxSteps - minSteps + 1) + minSteps;
		for (int i = 0; i < numSteps; i++) {
			x += random.nextInt(3) - 1;
			z += random.nextInt(3) - 1;
			y = (source.getY() << Chunk.CHUNK_SIZE_BITS) + 15;
			Block b = source.getWorld().getBlock(x, y, z);
			while (b.getMaterial() == VanillaMaterials.AIR && y >= 0) {
				b = b.translate(BlockFace.BOTTOM);
			}
			if (b.getY() == -1) {
				return;
			}
			if (b.getMaterial() == VanillaMaterials.GRASS) {
				b = b.translate(BlockFace.TOP).setMaterial(VanillaMaterials.TALL_GRASS).update(true);
			}
		}
	}
}
