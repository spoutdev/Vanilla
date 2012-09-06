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
package org.spout.vanilla.world.generator.normal;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.combiner.Add;
import net.royawesome.jlibnoise.module.combiner.Multiply;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.modifier.ScalePoint;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.generator.biome.BiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.util.LogicUtil;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.world.generator.VanillaBiomeGenerator;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;
import org.spout.vanilla.world.generator.normal.populator.GroundCoverPopulator;
import org.spout.vanilla.world.selector.VanillaBiomeSelector;

public class NormalGenerator extends VanillaBiomeGenerator {
	public static final int HEIGHT = 256;
	public static final int SEA_LEVEL = 63;
	// noise for generation
	private static final Perlin ELEVATION = new Perlin();
	private static final Perlin ROUGHNESS = new Perlin();
	private static final Perlin DETAIL = new Perlin();
	private static final Turbulence TURBULENCE = new Turbulence();
	private static final ScalePoint SCALE = new ScalePoint();
	private static final Clamp FINAL = new Clamp();
	// a perlin for determining groud cover depth
	protected static final Perlin BLOCK_REPLACER = new Perlin();
	private static final byte BEDROCK_DEPTH = 5;
	// smoothing stuff
	private static final int SMOOTH_SIZE = 2;

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

		SCALE.SetSourceModule(0, add);
		SCALE.setxScale(0.06);
		SCALE.setyScale(0.06);
		SCALE.setzScale(0.06);

		TURBULENCE.SetSourceModule(0, SCALE);
		TURBULENCE.setFrequency(0.01);
		TURBULENCE.setPower(8);
		TURBULENCE.setRoughness(1);

		FINAL.SetSourceModule(0, SCALE);
		FINAL.setLowerBound(-1);
		FINAL.setUpperBound(1);

		BLOCK_REPLACER.setFrequency(0.35);
		BLOCK_REPLACER.setLacunarity(1);
		BLOCK_REPLACER.setNoiseQuality(NoiseQuality.FAST);
		BLOCK_REPLACER.setPersistence(0.7);
		BLOCK_REPLACER.setOctaveCount(1);
	}

	@Override
	public void registerBiomes() {
		// if you want to check out a particular biome, use this!
		//setSelector(new PerBlockBiomeSelector(VanillaBiomes.MOUNTAINS));
		setSelector(new VanillaBiomeSelector());
		addPopulators(new GroundCoverPopulator() //, new RockyShieldPopulator(), new CavePopulator(), new RavinePopulator(),
				//new PondPopulator(), new DungeonPopulator(), new OrePopulator(), new BiomePopulator(getBiomeMap()),
				//new FallingLiquidPopulator(), new SnowPopulator()
		);
		register(VanillaBiomes.OCEAN);
		register(VanillaBiomes.FROZEN_OCEAN);
		register(VanillaBiomes.PLAINS);
		register(VanillaBiomes.DESERT);
		register(VanillaBiomes.DESERT_HILLS);
		register(VanillaBiomes.SMALL_MOUNTAINS);
		register(VanillaBiomes.MOUNTAINS);
		register(VanillaBiomes.BEACH);
		register(VanillaBiomes.SWAMP);
		register(VanillaBiomes.FOREST);
		register(VanillaBiomes.FOREST_HILLS);
		register(VanillaBiomes.FROZEN_RIVER);
		register(VanillaBiomes.RIVER);
		register(VanillaBiomes.JUNGLE);
		register(VanillaBiomes.JUNGLE_HILLS);
		register(VanillaBiomes.MUSHROOM);
		register(VanillaBiomes.MUSHROOM_SHORE);
		register(VanillaBiomes.TUNDRA);
		register(VanillaBiomes.TUNDRA_HILLS);
		register(VanillaBiomes.TAIGA);
		register(VanillaBiomes.TAIGA_HILLS);
	}

	@Override
	public String getName() {
		return "VanillaNormal";
	}

	@Override
	protected void generateTerrain(CuboidShortBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		final Vector3 size = blockData.getSize();
		final int sizeX = size.getFloorX();
		final int sizeY = size.getFloorY();
		final int sizeZ = size.getFloorZ();
		ELEVATION.setSeed((int) seed * 23);
		ROUGHNESS.setSeed((int) seed * 29);
		DETAIL.setSeed((int) seed * 17);
		TURBULENCE.setSeed((int) seed * 53);
		BLOCK_REPLACER.setSeed((int) seed * 127);
		final double[][][] noise = WorldGeneratorUtils.fastNoise(FINAL, sizeX, sizeY, sizeZ, 4, x, y, z);
		final BiomeSelector selector = getSelector();
		for (byte xx = 0; xx < sizeX; xx++) {
			for (byte zz = 0; zz < sizeZ; zz++) {
				float maxSum = 0;
				float minSum = 0;
				byte count = 0;
				for (int sx = -SMOOTH_SIZE; sx <= SMOOTH_SIZE; sx++) {
					for (int sz = -SMOOTH_SIZE; sz <= SMOOTH_SIZE; sz++) {
						if (xx + sx < 0 || zz + sz < 0
								|| xx + sx >= sizeX || zz + sz >= sizeZ) {
							final NormalBiome adjacent = (NormalBiome) selector.pickBiome(x + xx + sx, y, z + zz + sz, seed);
							minSum += adjacent.getMin();
							maxSum += adjacent.getMax();
						} else {
							final NormalBiome adjacent = (NormalBiome) biomes.getBiome(xx + sx, y, zz + sz);
							minSum += adjacent.getMin();
							maxSum += adjacent.getMax();
						}
						count++;
					}
				}
				final double minElevation = minSum / count;
				final double maxElevation = maxSum / count;
				final double smoothHeight = (maxElevation - minElevation) / 2d;
				final double lowSmoothStart = minElevation + smoothHeight;
				final double upperSmoothStart = maxElevation - smoothHeight;
				for (byte yy = 0; yy < sizeY; yy++) {
					double noiseValue = noise[xx][yy][zz];
					if (smoothHeight > 0) {
						if (yy + y < lowSmoothStart) {
							noiseValue += (1 / smoothHeight) * (lowSmoothStart - yy - y);
						} else if (yy + y >= upperSmoothStart) {
							noiseValue -= (1 / smoothHeight) * (y + yy - upperSmoothStart);
						}
					}
					if (noiseValue >= 0) {
						blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.STONE.getId());
					} else {
						if (y + yy <= SEA_LEVEL) {
							if (y + yy == SEA_LEVEL && ((NormalBiome) biomes.getBiome(xx, 0, zz)).getClimate() == Climate.COLD) {
								blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.ICE.getId());
							} else {
								blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.WATER.getId());
							}
						} else {
							blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.AIR.getId());
						}
					}
				}
				if (y == 0) {
					final byte bedrockDepth = (byte) Math.ceil(BLOCK_REPLACER.GetValue(x + xx, -5, z + zz)
							* (BEDROCK_DEPTH / 2d) + BEDROCK_DEPTH / 2d);
					for (byte yy = 0; yy < bedrockDepth; yy++) {
						blockData.set(x + xx, yy, z + zz, VanillaMaterials.BEDROCK.getId());
					}
				}
			}
		}
	}

	@Override
	public Point getSafeSpawn(World world) {
		short shift = 0;
		final BiomeSelector selector = getSelector();
		while (LogicUtil.equalsAny(selector.pickBiome(shift, 0, world.getSeed()),
				VanillaBiomes.OCEAN, VanillaBiomes.BEACH, VanillaBiomes.RIVER, VanillaBiomes.SWAMP)
				&& shift < 1600) {
			shift += 16;
		}
		final Random random = new Random();
		for (byte attempts = 0; attempts < 32; attempts++) {
			final int x = random.nextInt(256) - 127 + shift;
			final int z = random.nextInt(256) - 127;
			final int y = getHighestSolidBlock(world, x, z);
			if (y != -1) {
				return new Point(world, x, y + 0.5f, z);
			}
		}
		return new Point(world, shift, 80, 0);
	}

	private int getHighestSolidBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) instanceof Liquid) {
				return -1;
			}
		}
		return ++y;
	}

	@Override
	public int[][] getSurfaceHeight(World world, int chunkX, int chunkY) {
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
				heights[x][z] = SEA_LEVEL;
			}
		}
		return heights;
	}
}
