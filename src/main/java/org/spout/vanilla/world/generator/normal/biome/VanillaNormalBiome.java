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

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.Module;
import net.royawesome.jlibnoise.module.combiner.Add;
import net.royawesome.jlibnoise.module.combiner.Multiply;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.modifier.ScalePoint;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.math.MathHelper;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;

public abstract class VanillaNormalBiome extends VanillaBiome {

	protected static final byte SEA_LEVEL = 64;
	// the master noise to be used by biomes extending this class
	protected static final Add MASTER = new Add();
	// the parts for the master noise
	private static final Perlin ELEVATION = new Perlin();
	private static final Perlin ROUGHNESS = new Perlin();
	private static final Perlin DETAIL = new Perlin();
	// a scaled version of the elevation for block replacing
	private final static ScalePoint BLOCK_REPLACER = new ScalePoint();
	// a modified version of the master, provided by the extending biome
	private final Module modifiedMaster;
	// a turbulent version of the modified master, used for density gen
	private final Turbulence mainMaster = new Turbulence();
	// noise used to gen the height map parts, based on the mainMaster
	private final Clamp bottomHeightMapMaster = new Clamp();
	private final Clamp upperHeightMapMaster = new Clamp();
	// params to be modified by the extending biome
	// these are defaults to gen forest terrain (appropriate for many biomes)
	// limits of height maps
	protected byte minDensityTerrainHeight = 64;
	protected byte maxDensityTerrainHeight = 70;
	protected byte minDensityTerrainThickness = 1;
	protected byte maxDensityTerrainThickness = 3;
	// scale of height maps
	protected float upperHeightMapScale = 2f;
	protected float bottomHeightMapScale = 4f;
	// scale of the density layer
	protected float densityTerrainThicknessScale = 3f;
	protected float densityTerrainHeightScale = 4f;

	static {
		ELEVATION.setFrequency(0.4D);
		ELEVATION.setLacunarity(1D);
		ELEVATION.setNoiseQuality(NoiseQuality.STANDARD);
		ELEVATION.setPersistence(0.7D);
		ELEVATION.setOctaveCount(1);

		ROUGHNESS.setFrequency(0.5D);
		ROUGHNESS.setLacunarity(1D);
		ROUGHNESS.setNoiseQuality(NoiseQuality.STANDARD);
		ROUGHNESS.setPersistence(0.9D);
		ROUGHNESS.setOctaveCount(1);

		DETAIL.setFrequency(0.9D);
		DETAIL.setLacunarity(1D);
		DETAIL.setNoiseQuality(NoiseQuality.STANDARD);
		DETAIL.setPersistence(0.7D);
		DETAIL.setOctaveCount(1);

		final Multiply multiply = new Multiply();
		multiply.SetSourceModule(0, ROUGHNESS);
		multiply.SetSourceModule(1, DETAIL);

		MASTER.SetSourceModule(0, multiply);
		MASTER.SetSourceModule(1, ELEVATION);

		BLOCK_REPLACER.SetSourceModule(0, ELEVATION);
		BLOCK_REPLACER.setxScale(4D);
		BLOCK_REPLACER.setyScale(1D);
		BLOCK_REPLACER.setzScale(4D);
	}

	protected VanillaNormalBiome(int biomeId, Module modifiedMaster, Decorator... decorators) {
		super(biomeId, decorators);

		this.modifiedMaster = modifiedMaster;

		mainMaster.SetSourceModule(0, modifiedMaster);
		mainMaster.setFrequency(0.05D);
		mainMaster.setPower(0.3125D);

		bottomHeightMapMaster.SetSourceModule(0, mainMaster);
		bottomHeightMapMaster.setUpperBound(0D);
		bottomHeightMapMaster.setLowerBound(-minDensityTerrainHeight);

		upperHeightMapMaster.SetSourceModule(0, mainMaster);
		upperHeightMapMaster.setUpperBound(maxDensityTerrainThickness);
		upperHeightMapMaster.setLowerBound(0D);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {

		if (chunkY < 0) {
			return;
		}

		final byte size = (byte) blockData.getSize().getY();

		final int startY = chunkY * 16;
		final int endY = startY + size;

		fill(blockData, x, startY, endY, z);

		replaceBlocks(blockData, x, chunkY, z);
	}

	protected void fill(CuboidShortBuffer blockData, int x, int startY, int endY, int z) {

		final int seed = (int) blockData.getWorld().getSeed();
		ROUGHNESS.setSeed(seed);
		DETAIL.setSeed(seed);
		ELEVATION.setSeed(seed);
		mainMaster.setSeed(seed);

		final int densityTerrainHeightMin = getDensityTerrainHeight(x, z);
		final int densityTerrainHeightMax = densityTerrainHeightMin + getDensityTerrainThickness(x, z);

		for (int y = startY; y < endY; y++) {
			if (y <= densityTerrainHeightMin) {
				final int bottomHeightMapHeight = getBottomHeightMapValue(x, z, densityTerrainHeightMin);
				for (; y <= bottomHeightMapHeight && y < endY; y++) {
					blockData.set(x, y, z, VanillaMaterials.STONE.getId());
				}
				if (y >= endY) { // if not, we fill the rest with density terrain
					break;
				}
			} else if (y > densityTerrainHeightMax) {
				final int value = getUpperHeightMapValue(x, z, densityTerrainHeightMax);
				for (; y < value && y < endY; y++) {
					blockData.set(x, y, z, VanillaMaterials.STONE.getId());
				}
				break; // we're done for the entire world column!
			}
			if (mainMaster.GetValue(x, y, z) > 0) {
				blockData.set(x, y, z, VanillaMaterials.STONE.getId());
			} else {
				blockData.set(x, y, z, VanillaMaterials.AIR.getId());
			}
		}
	}

	private int getDensityTerrainHeight(int x, int z) {
		return (int) MathHelper.clamp(modifiedMaster.GetValue(x, minDensityTerrainHeight, z) * densityTerrainHeightScale
				+ minDensityTerrainHeight + (maxDensityTerrainHeight - minDensityTerrainHeight) / 2,
				minDensityTerrainHeight, maxDensityTerrainHeight);
	}

	private int getDensityTerrainThickness(int x, int z) {
		return (int) MathHelper.clamp(modifiedMaster.GetValue(x, minDensityTerrainHeight, z) * densityTerrainThicknessScale
				+ minDensityTerrainThickness + (maxDensityTerrainThickness - minDensityTerrainThickness) / 2,
				minDensityTerrainThickness, maxDensityTerrainThickness);
	}

	private int getBottomHeightMapValue(int x, int z, int densityTerrainHeightMin) {
		return (int) Math.ceil(bottomHeightMapMaster.GetValue(x, densityTerrainHeightMin, z)
				* bottomHeightMapScale + densityTerrainHeightMin + 1);
	}

	private int getUpperHeightMapValue(int x, int z, int densityTerrainHeightMax) {
		return (int) Math.ceil(upperHeightMapMaster.GetValue(x, densityTerrainHeightMax, z)
				* upperHeightMapScale + densityTerrainHeightMax);
	}

	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {

		final byte size = (byte) blockData.getSize().getY();

		if (size < 16) {
			return; // ignore samples
		}

		final int endY = chunkY * 16;
		final int startY = endY + size - 1;

		final byte maxGroudCoverDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, 0, z) * 2 + 4, 1D, 5D);
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
				if (y <= SEA_LEVEL) {
					blockData.set(x, y, z, VanillaMaterials.STATIONARY_WATER.getId());
				}
			} else {
				if (hasSurface) {
					if (groundCoverDepth == 0) {
						short topCover = y > SEA_LEVEL - 1 ? VanillaMaterials.GRASS.getId() : VanillaMaterials.DIRT.getId();
						blockData.set(x, y, z, topCover);
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
		if (chunkY == 0) {
			final byte bedrockDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, -5, z) * 2 + 4, 1D, 5D);
			for (int y = 0; y <= bedrockDepth; y++) {
				blockData.set(x, y, z, VanillaMaterials.BEDROCK.getId());
			}
		}
	}

	protected CuboidShortBuffer getSample(World world, int x, int startY, int endY, int z) {

		int size;

		if (endY <= startY) {
			size = 0;
		} else {
			size = endY - startY;
		}

		if (size >= 16) {
			size = 15; // samples should not be larger than a column
		}
		// currently, using different size params will cause errors with the cuboid short buffer...
		CuboidShortBuffer sample = new CuboidShortBuffer(world, x, startY, z, size, size, size);
		fill(sample, x, startY, endY, z);
		return sample;
	}
}
