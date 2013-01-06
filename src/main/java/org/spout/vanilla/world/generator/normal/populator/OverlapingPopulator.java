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
package org.spout.vanilla.world.generator.normal.populator;

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
		final int chunkX = x >> Chunk.BLOCKS.BITS;
		final int chunkZ = z >> Chunk.BLOCKS.BITS;
		final Random worldRandom = new Random(seed);
		final long firstSeed = worldRandom.nextLong();
		final long secondSeed = worldRandom.nextLong();
		for (int cx = chunkX - OVERLAP; cx <= chunkX + OVERLAP; cx++) {
			for (int cz = chunkZ - OVERLAP; cz <= chunkZ + OVERLAP; cz++) {
				final Random chunkRandom = new Random((cx * firstSeed) ^ (cz * secondSeed) ^ seed);
				generate(blockData, new Vector3(cx << Chunk.BLOCKS.BITS, 0, cz << Chunk.BLOCKS.BITS),
						new Vector3(chunkX << Chunk.BLOCKS.BITS, 0, chunkZ << Chunk.BLOCKS.BITS), chunkRandom);
			}
		}
	}

	protected abstract void generate(CuboidBlockMaterialBuffer blockData, Vector3 chunk, Vector3 originChunk, Random random);
}
