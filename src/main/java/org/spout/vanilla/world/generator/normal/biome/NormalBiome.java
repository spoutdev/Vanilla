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

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.combiner.Add;
import net.royawesome.jlibnoise.module.combiner.Multiply;
import net.royawesome.jlibnoise.module.modifier.ScalePoint;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.math.MathHelper;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.plant.TallGrass;
import org.spout.vanilla.world.generator.VanillaBiome;
import org.spout.vanilla.world.generator.normal.decorator.TallGrassDecorator.TallGrassFactory;
import org.spout.vanilla.world.generator.normal.decorator.TreeDecorator.TreeWGOFactory;
import org.spout.vanilla.world.generator.normal.object.tree.BigTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.SmallTreeObject;
import org.spout.vanilla.world.generator.normal.object.tree.TreeObject;

public abstract class NormalBiome extends VanillaBiome {
	// the master noise to be used by biomes extending this class
	private static final ScalePoint MASTER = new ScalePoint();
	// the parts for the master noise
	private static final Perlin ELEVATION = new Perlin();
	private static final Perlin ROUGHNESS = new Perlin();
	private static final Perlin DETAIL = new Perlin();
	// a turbulent version of the modified master, used for density gen
	private static final Turbulence TURBULENT_MASTER = new Turbulence();
	// a scaled version of the elevation for block replacing
	protected static final ScalePoint BLOCK_REPLACER = new ScalePoint();
	// height settings
	protected byte min;
	protected byte max;

	static {
		ELEVATION.setFrequency(0.2);
		ELEVATION.setLacunarity(1);
		ELEVATION.setNoiseQuality(NoiseQuality.STANDARD);
		ELEVATION.setPersistence(0.7);
		ELEVATION.setOctaveCount(1);

		ROUGHNESS.setFrequency(0.53);
		ROUGHNESS.setLacunarity(1);
		ROUGHNESS.setNoiseQuality(NoiseQuality.STANDARD);
		ROUGHNESS.setPersistence(0.9);
		ROUGHNESS.setOctaveCount(1);

		DETAIL.setFrequency(0.7);
		DETAIL.setLacunarity(1);
		DETAIL.setNoiseQuality(NoiseQuality.STANDARD);
		DETAIL.setPersistence(0.7);
		DETAIL.setOctaveCount(1);

		final Multiply multiply = new Multiply();
		multiply.SetSourceModule(0, ROUGHNESS);
		multiply.SetSourceModule(1, DETAIL);

		final Add add = new Add();
		add.SetSourceModule(0, multiply);
		add.SetSourceModule(1, ELEVATION);

		MASTER.SetSourceModule(0, add);
		MASTER.setxScale(0.06);
		MASTER.setyScale(0.04);
		MASTER.setzScale(0.06);

		BLOCK_REPLACER.SetSourceModule(0, ELEVATION);
		BLOCK_REPLACER.setxScale(4);
		BLOCK_REPLACER.setyScale(1);
		BLOCK_REPLACER.setzScale(4);

		TURBULENT_MASTER.SetSourceModule(0, MASTER);
		TURBULENT_MASTER.setFrequency(0.005);
		TURBULENT_MASTER.setPower(8);
		TURBULENT_MASTER.setRoughness(1);
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

		final int heightMapHeight = getHeightMapValue(x, z);
		final int densityTerrainHeight = getDensityTerrainThickness(x, z) + heightMapHeight;
		int y = startY;
		for (; y < endY; y++) {
			if (y <= heightMapHeight) {
				blockData.set(x, y, z, VanillaMaterials.STONE.getId());
			} else {
				break;
			}
		}
		for (; y < endY; y++) {
			if (y <= densityTerrainHeight) {
				if (TURBULENT_MASTER.GetValue(x, y, z) > 0) {
					blockData.set(x, y, z, VanillaMaterials.STONE.getId());
				} else {
					blockData.set(x, y, z, VanillaMaterials.AIR.getId());
				}
			} else {
				break;
			}
		}
	}

	private int getDensityTerrainThickness(int x, int z) {
		final float scale = (max - min) / 2f;
		return (int) Math.round(MASTER.GetValue(x, 1337, z) * scale + scale);
	}

	private int getHeightMapValue(int x, int z) {
		final int scale = max - min;
		return (int) Math.round(TURBULENT_MASTER.GetValue(x, 63, z) * scale + scale + min);
	}

	protected void replaceBlocks(CuboidShortBuffer blockData, int x, int chunkY, int z) {
		if (chunkY == 0) {
			final byte bedrockDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x, -5, z) * 2 + 4, 1, 5);
			for (byte y = 0; y <= bedrockDepth; y++) {
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
		final CuboidShortBuffer sample = new CuboidShortBuffer(world, x, startY, z, 1, size, 1);
		fill(sample, x, startY, endY, z);
		return sample;
	}

	protected void setMinMax(byte min, byte max) {
		this.min = min;
		this.max = max;
	}

	public byte getMin() {
		return min;
	}

	public byte getMax() {
		return max;
	}

	public static class NormalTreeWGOFactory implements TreeWGOFactory {
		@Override
		public TreeObject make(Random random) {
			if (random.nextInt(10) == 0) {
				return new BigTreeObject(random);
			} else {
				return new SmallTreeObject(random);
			}
		}

		@Override
		public byte amount(Random random) {
			if (random.nextInt(10) == 0) {
				return 1;
			}
			return 0;
		}
	}

	public static class NormalTallGrassFactory implements TallGrassFactory {
		@Override
		public TallGrass make(Random random) {
			return TallGrass.TALL_GRASS;
		}
	}
}
