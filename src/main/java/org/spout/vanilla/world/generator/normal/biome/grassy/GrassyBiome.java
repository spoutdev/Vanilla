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
package org.spout.vanilla.world.generator.normal.biome.grassy;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.MathHelper;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;

public abstract class GrassyBiome extends NormalBiome {
	protected BlockMaterial topCover = VanillaMaterials.GRASS;

	public GrassyBiome(int biomeId, Decorator... decorators) {
		super(biomeId, decorators);
	}

	@Override
	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {

		final short size = (short) blockData.getSize().getY();

		if (size < 16) {
			return; // ignore samples
		}

		final int endY = chunkY * 16;
		final int startY = endY + size - 1;

		final byte maxGroudCoverDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, 0, z) * 2 + 4, 2, 5);
		final byte sampleSize = (byte) (maxGroudCoverDepth + 1);

		boolean hasSurface = false;
		byte groundCoverDepth = 0;
		// check the column above by sampling, determining any missing blocks
		// to add to the current column
		if (blockData.get(x, startY, z) != VanillaMaterials.AIR.getId()) {
			final int nextChunkStart = (chunkY + 1) * 16;
			final int nextChunkEnd = nextChunkStart + sampleSize;
			final CuboidShortBuffer sample = getSample(blockData.getWorld(), x, nextChunkStart, nextChunkEnd, z);
			for (int y = nextChunkStart; y < nextChunkEnd; y++) {
				if (sample.get(x, y, z) != VanillaMaterials.AIR.getId()) {
					groundCoverDepth++;
				} else {
					hasSurface = true;
					break;
				}
			}
		}
		// place ground cover
		for (int y = startY; y >= endY; y--) {
			final short id = blockData.get(x, y, z);
			if (id == VanillaMaterials.AIR.getId()) {
				hasSurface = true;
				groundCoverDepth = 0;
				if (y <= NormalGenerator.SEA_LEVEL) {
					blockData.set(x, y, z, VanillaMaterials.STATIONARY_WATER.getId());
				}
			} else {
				if (hasSurface) {
					if (groundCoverDepth == 0) {
						if (y >= NormalGenerator.SEA_LEVEL) {
							blockData.set(x, y, z, topCover.getId());
						} else {
							blockData.set(x, y, z, VanillaMaterials.DIRT.getId());
						}
						groundCoverDepth++;
					} else if (groundCoverDepth < maxGroudCoverDepth) {
						blockData.set(x, y, z, VanillaMaterials.DIRT.getId());
						groundCoverDepth++;
					} else {
						hasSurface = false;
					}
				}
			}
		}
		// place bedrock
		super.replaceBlocks(blockData, x, chunkY, z);
	}

	public BlockMaterial getTopCover() {
		return topCover;
	}
}
