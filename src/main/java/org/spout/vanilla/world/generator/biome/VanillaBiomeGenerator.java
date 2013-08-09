/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.world.generator.biome;

import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.geo.World;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaGenerator;

public abstract class VanillaBiomeGenerator extends BiomeGenerator implements VanillaGenerator {
	private int lowestChunkY = 0;
	private int highestChunkY = 15;

	@Override
	public void generate(CuboidBlockMaterialBuffer blockData, World world) {
		if (blockData.getBaseChunkY() > highestChunkY) {
			blockData.flood(VanillaMaterials.AIR);
		} else if (blockData.getBaseChunkY() < lowestChunkY) {
			blockData.flood(VanillaMaterials.BEDROCK);
		} else {
			super.generate(blockData, world);
		}
	}

	public int getLowestChunkY() {
		return lowestChunkY;
	}

	public void setLowestChunkY(int lowestChunkY) {
		this.lowestChunkY = lowestChunkY;
	}

	public int getHighestChunkY() {
		return highestChunkY;
	}

	public void setHighestChunkY(int highestChunkY) {
		this.highestChunkY = highestChunkY;
	}
}
