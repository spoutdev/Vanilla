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
import org.spout.api.geo.discrete.Point;
import org.spout.vanilla.VanillaMaterials;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.source.Perlin;

public class CaveDecorator implements BiomeDecorator {
	private Perlin noise = new Perlin();

	public CaveDecorator() {
		noise.setNoiseQuality(NoiseQuality.BEST);
		noise.setOctaveCount(8);
		noise.setFrequency(2);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		noise.setSeed((int) chunk.getWorld().getSeed());

		int x = chunk.getX() * 16;
		int y = chunk.getY() * 16;
		int z = chunk.getZ() * 16;
		Point pt = new Point(chunk.getWorld(), x + random.nextInt(16), y + random.nextInt(16), z + random.nextInt(16));
		for (int dx = x; dx < x + 16; dx++) {
			for (int dz = z; dz < z + 16; dz++) {
				for (int dy = y; dy < y + 16; dy++) {
					if (Math.sqrt(Math.pow(dx - pt.getX(), 2) + Math.pow(dy - pt.getY(), 2) + Math.pow(dz - pt.getZ(), 2)) > 6) {
						continue;
					}
					if (noise.GetValue(dx / 5.0 + 0.005, dy / 5.0 + 0.005, dz / 5.0 + 0.005) > 0 && chunk.getBlockId(dx, dy, dz) == VanillaMaterials.STONE.getId()) {
						chunk.setBlockId(dx, dy, dz, VanillaMaterials.AIR.getId(), chunk.getWorld());
					}
				}
			}
		}
	}
}
