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
package org.spout.vanilla.world.generator.normal.biome;

import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.vanilla.material.VanillaMaterials;

public class TundraBiome extends PlainBiome {
	public TundraBiome(int biomeId) {
		super(biomeId);
	}

	@Override
	public String getName() {
		return "Tundra";
	}

	protected void generateWateredStack(CuboidShortBuffer blockData, int x, int y, int z, int maxSeaLevel) {
		boolean first = true;
		for (int dy = y + 15; dy >= y; dy--) {
			final int curBlock = blockData.get(x, dy, z);
			if (dy < maxSeaLevel && (curBlock == VanillaMaterials.AIR.getId() || curBlock == VanillaMaterials.SNOW.getId())) {
				if (first) {
					blockData.set(x, dy, z, VanillaMaterials.ICE.getId());
					first = false;
				} else {
					blockData.set(x, dy, z, VanillaMaterials.WATER.getId());
				}
			} else {
				break;
			}
		}
	}

	protected short getBlockId(int top, int dy) {
		if (dy == top + 1) {
			return VanillaMaterials.SNOW.getId();
		} else {
			return super.getBlockId(top, dy);
		}
	}
}
