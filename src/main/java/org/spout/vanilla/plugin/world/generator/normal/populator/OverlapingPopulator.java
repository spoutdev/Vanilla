/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.plugin.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

public abstract class OverlapingPopulator implements GeneratorPopulator {
	protected static final byte OVERLAP = 8;

	@Override
	public void populate(CuboidBlockMaterialBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		if (y >= Region.BLOCKS.SIZE) {
			return;
		}
		final int cx = x >> Chunk.BLOCKS.BITS;
		final int cz = z >> Chunk.BLOCKS.BITS;
		final Vector3 size = blockData.getSize();
		final int sizeX = size.getFloorX() >> Chunk.BLOCKS.BITS;
		final int sizeZ = size.getFloorZ() >> Chunk.BLOCKS.BITS;
		final Random worldRandom = new Random(seed);
		final long firstSeed = worldRandom.nextLong();
		final long secondSeed = worldRandom.nextLong();
		for (int cxx = 0; cxx < sizeX; cxx++) {
			for (int czz = 0; czz < sizeZ; czz++) {
				int dcx = cx + cxx;
				int dcz = cz + czz;
				for (int cxxx = -OVERLAP; cxxx <= OVERLAP; cxxx++) {
					for (int czzz = -OVERLAP; czzz <= OVERLAP; czzz++) {
						int dcxx = dcx + cxxx;
						int dczz = dcz + czzz;
						populate(blockData,
								new Vector3(dcxx << Chunk.BLOCKS.BITS, 0, dczz << Chunk.BLOCKS.BITS),
								new Vector3(dcx << Chunk.BLOCKS.BITS, 0, dcz << Chunk.BLOCKS.BITS),
								new Random((dcxx * firstSeed) ^ (dczz * secondSeed) ^ seed));
					}
				}
			}
		}
	}

	protected abstract void populate(CuboidBlockMaterialBuffer blockData, Vector3 chunk, Vector3 originChunk, Random random);
}
