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
package org.spout.vanilla.generator.theend.biome;

import org.spout.api.Spout;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.generator.VanillaBiomeType;
import org.spout.vanilla.generator.theend.decorator.SpireDecorator;

import net.royawesome.jlibnoise.module.source.Perlin;

public class EndStoneBiome extends VanillaBiomeType {
	Perlin heightMap = new Perlin();

	public EndStoneBiome() {
		super(9, new SpireDecorator());
		heightMap.setOctaveCount(1);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		final int y = chunkY * 16;
		final int height = (int) ((heightMap.GetValue(x / 16.0 + 0.005, 0.05, z / 16.0 + 0.005) + 1.0) * 4.0 + 60.0);
		final Transform spawn = blockData.getWorld().getSpawnPoint();
		final Point currPOS = new Point(blockData.getWorld(), (float) x, (float) chunkY, (float) z);

		heightMap.setSeed((int) blockData.getWorld().getSeed());
		for (int dy = y; dy < y + 16; dy++) {
			//Used 100 as it first came to mind lol
			if (currPOS.distance(spawn.getPosition()) > 80) {
				blockData.set(x, dy, z, VanillaMaterials.AIR.getId());
			} else {
				blockData.set(x, dy, z, getBlockId(height, dy));
			}
		}
	}

	protected short getBlockId(int noise, int dy) {
		short id;
		if (dy > noise) {
			id = VanillaMaterials.AIR.getId();
		} else {
			id = VanillaMaterials.END_STONE.getId();
		}
		return id;
	}
}
