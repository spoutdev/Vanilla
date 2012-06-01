/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
import org.spout.vanilla.world.generator.VanillaBiome;

public class SwampBiome extends VanillaBiome {
	public SwampBiome(int biomeId) {
		super(biomeId);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		int y = chunkY * 16, height = 64;

		for (int dy = y; dy < y + 16; dy++) {
			blockData.set(x, dy, z, getBlockId(height, dy));
		}

		if (x % 16 == 0 && height << 4 == chunkY) {
			blockData.set(x, height, z, VanillaMaterials.NETHERRACK.getId()); // until biome is implemented
		}
	}

	@Override
	public String getName() {
		return "Swampland";
	}

	protected short getBlockId(int top, int dy) {
		short id;
		if (dy > top) {
			id = VanillaMaterials.AIR.getId();
		} else if (dy == top) {
			id = VanillaMaterials.DIRT.getId();
		} else if (dy + 4 >= top) {
			id = VanillaMaterials.DIRT.getId();
		} else if (dy != 0) {
			id = VanillaMaterials.STONE.getId();
		} else {
			id = VanillaMaterials.BEDROCK.getId();
		}
		return id;
	}
}
