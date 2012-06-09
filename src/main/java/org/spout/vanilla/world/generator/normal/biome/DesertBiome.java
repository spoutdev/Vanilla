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

import net.royawesome.jlibnoise.module.modifier.ScalePoint;

import org.spout.api.math.MathHelper;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.configuration.BiomeConfiguration;
import org.spout.vanilla.material.VanillaMaterials;

public class DesertBiome extends NormalBiome {

	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, NormalBiome.MASTER);
		NOISE.setxScale(BiomeConfiguration.DESERT_X_SCALE.getDouble());
		NOISE.setyScale(BiomeConfiguration.DESERT_Y_SCALE.getDouble());
		NOISE.setzScale(BiomeConfiguration.DESERT_Z_SCALE.getDouble());
	}

	public DesertBiome(int biomeId) {
		super(biomeId, NOISE/*
				 * , new CactusDecorator()
				 */);

		this.minDensityTerrainHeight = BiomeConfiguration.DESERT_MIN_DENSITY_TERRAIN_HEIGHT.getByte();
		this.maxDensityTerrainHeight = BiomeConfiguration.DESERT_MAX_DENSITY_TERRAIN_HEIGHT.getByte();

		this.minDensityTerrainThickness = BiomeConfiguration.BEACH_MIN_DENSITY_TERRAIN_THICKNESS.getByte();
		this.maxDensityTerrainThickness = BiomeConfiguration.DESERT_MAX_DENSITY_TERRAIN_THICKNESS.getByte();

		this.upperHeightMapScale = BiomeConfiguration.DESERT_UPPER_HEIGHT_MAP_SCALE.getFloat();
		this.bottomHeightMapScale = BiomeConfiguration.DESERT_BOTTOM_HEIGHT_MAP_SCALE.getFloat();

		this.densityTerrainThicknessScale = BiomeConfiguration.DESERT_DENSITY_TERRAIN_THICKNESS_SCALE.getFloat();
		this.densityTerrainHeightScale = BiomeConfiguration.DESERT_DENSITY_TERRAIN_HEIGHT_SCALE.getFloat();
	}

	@Override
	public String getName() {
		return "Desert";
	}

	@Override
	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {

		final byte size = (byte) blockData.getSize().getY();

		if (size < 16) {
			return; // ignore samples
		}

		final int endY = chunkY * 16;
		final int startY = endY + size - 1;

		final byte sandDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, -5, z) * 4 + 4, 3, 4);
		final byte sandstoneDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, -6, z) * 4 + 3, 0, 3);

		final byte maxGroudCoverDepth = (byte) (sandDepth + sandstoneDepth);
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
			} else {
				if (hasSurface) {
					if (groundCoverDepth < sandDepth) {
						blockData.set(x, y, z, VanillaMaterials.SAND.getId());
						groundCoverDepth++;
					} else if (groundCoverDepth < maxGroudCoverDepth) {
						blockData.set(x, y, z, VanillaMaterials.SANDSTONE.getId());
						groundCoverDepth++;
					} else {
						hasSurface = false;
					}
				}
			}
		}
		// place bedrock
		if (chunkY == 0) {
			final byte bedrockDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, -5, z) * 2 + 4, 1D, 5D);
			for (int y = 0; y <= bedrockDepth; y++) {
				blockData.set(x, y, z, VanillaMaterials.BEDROCK.getId());
			}
		}
	}
}
