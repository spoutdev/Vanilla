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
import net.royawesome.jlibnoise.module.combiner.Add;
import net.royawesome.jlibnoise.module.combiner.Multiply;
import net.royawesome.jlibnoise.module.modifier.Exponent;
import net.royawesome.jlibnoise.module.modifier.ScalePoint;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.math.MathHelper;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiome;

public abstract class NormalBiome extends VanillaBiome {
	// the master noise to be used by biomes extending this class
	private static final ScalePoint MASTER = new ScalePoint();
	// the parts for the master noise
	private static final Perlin ELEVATION = new Perlin();
	private static final Perlin ROUGHNESS = new Perlin();
	private static final Perlin DETAIL = new Perlin();
	// scale of height maps
	private static final float HEIGHT_MAP_SCALE = 4f;
	// a turbulent version of the modified master, used for density gen
	private static final Turbulence TURBULENT_MASTER = new Turbulence();
	// a scaled version of the elevation for block replacing
	protected static final ScalePoint BLOCK_REPLACER = new ScalePoint();
	// height settings
	protected byte min;
	protected byte max;
	protected byte diff;

	static {
		ELEVATION.setFrequency(0.2D);
		ELEVATION.setLacunarity(1D);
		ELEVATION.setNoiseQuality(NoiseQuality.STANDARD);
		ELEVATION.setPersistence(0.7D);
		ELEVATION.setOctaveCount(1);

		ROUGHNESS.setFrequency(0.53D);
		ROUGHNESS.setLacunarity(1D);
		ROUGHNESS.setNoiseQuality(NoiseQuality.STANDARD);
		ROUGHNESS.setPersistence(0.9D);
		ROUGHNESS.setOctaveCount(1);

		DETAIL.setFrequency(0.7D);
		DETAIL.setLacunarity(1D);
		DETAIL.setNoiseQuality(NoiseQuality.STANDARD);
		DETAIL.setPersistence(0.7D);
		DETAIL.setOctaveCount(1);

		final Multiply multiply = new Multiply();
		multiply.SetSourceModule(0, ROUGHNESS);
		multiply.SetSourceModule(1, DETAIL);

		final Add add = new Add();
		add.SetSourceModule(0, multiply);
		add.SetSourceModule(1, ELEVATION);
		
		MASTER.SetSourceModule(0, add);
		MASTER.setxScale(0.08);
		MASTER.setyScale(0.04);
		MASTER.setzScale(0.08);

		BLOCK_REPLACER.SetSourceModule(0, ELEVATION);
		BLOCK_REPLACER.setxScale(4D);
		BLOCK_REPLACER.setyScale(1D);
		BLOCK_REPLACER.setzScale(4D);
		
		final Exponent contrast = new Exponent();
		contrast.SetSourceModule(0, MASTER);
		contrast.setExponent(1.5D);

		TURBULENT_MASTER.SetSourceModule(0, contrast);
		TURBULENT_MASTER.setFrequency(0.005D);
		TURBULENT_MASTER.setPower(6D);
	}

	protected NormalBiome(int biomeId, Decorator... decorators) {
		super(biomeId, decorators);
	}

	@Override
	public void generateColumn(CuboidShortBuffer blockData, int x, int chunkY, int z) {

		if (chunkY < 0) {
			return;
		}

		final short size = (short) blockData.getSize().getY();

		final int startY = chunkY * 16;
		final int endY = startY + size;

		fill(blockData, x, startY, endY, z);

		replaceBlocks(blockData, x, chunkY, z);
	}

	protected void fill(CuboidShortBuffer blockData, int x, int startY, int endY, int z) {

		final int seed = (int) blockData.getWorld().getSeed();
		ELEVATION.setSeed(seed);
		ROUGHNESS.setSeed(seed * 2);
		DETAIL.setSeed(seed * 3);
		TURBULENT_MASTER.setSeed(seed * 5);

		final int densityTerrainHeightMin = getDensityTerrainHeight(x, z);
		final int densityTerrainHeightMax = densityTerrainHeightMin + getDensityTerrainThickness(x, z);

		final int bottomHeightMapHeight = getBottomHeightMapValue(x, z, densityTerrainHeightMin);
		final int upperHeightMapHeight = getUpperHeightMapValue(x, z, densityTerrainHeightMax);

		for (int y = startY; y < endY; y++) {
			if (y <= densityTerrainHeightMin) {
				for (; y <= bottomHeightMapHeight && y < endY; y++) {
					blockData.set(x, y, z, VanillaMaterials.STONE.getId());
				}
				if (y >= endY) { // if not, we fill the rest with density terrain
					break;
				}
			} else if (y > densityTerrainHeightMax) {
				for (; y < upperHeightMapHeight && y < endY; y++) {
					blockData.set(x, y, z, VanillaMaterials.STONE.getId());
				}
				break; // we're done for the entire world column!
			}
			if (TURBULENT_MASTER.GetValue(x, y, z) > 0) {
				blockData.set(x, y, z, VanillaMaterials.STONE.getId());
			} else {
				blockData.set(x, y, z, VanillaMaterials.AIR.getId());
			}
		}
	}

	private int getDensityTerrainHeight(int x, int z) {
		return (int) MathHelper.clamp(MASTER.GetValue(x, min, z) * diff / 2 + diff / 2 + min, min, max);
	}

	private int getDensityTerrainThickness(int x, int z) {
		return (int) MathHelper.clamp(MASTER.GetValue(x, -min, z) * diff / 2 + diff / 2, 0, diff);
	}

	private int getBottomHeightMapValue(int x, int z, int densityTerrainHeightMin) {
		return (int) Math.ceil(TURBULENT_MASTER.GetValue(x, densityTerrainHeightMin, z)
				* HEIGHT_MAP_SCALE + densityTerrainHeightMin + 1);
	}

	private int getUpperHeightMapValue(int x, int z, int densityTerrainHeightMax) {
		return (int) Math.ceil(TURBULENT_MASTER.GetValue(x, densityTerrainHeightMax, z)
				* HEIGHT_MAP_SCALE + densityTerrainHeightMax);
	}

	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {
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
		CuboidShortBuffer sample = new CuboidShortBuffer(world, x, startY, z, 1, size, 1);
		fill(sample, x, startY, endY, z);
		return sample;
	}

	protected void setMinMax(byte min, byte max) {
		this.min = min;
		this.max = max;
		this.diff = (byte) (max - min);
	}

	public byte getMin() {
		return min;
	}

	public byte getMax() {
		return max;
	}
}
