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
package org.spout.vanilla.world.generator.nether;

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
import org.spout.api.generator.biome.BiomePopulator;
import org.spout.api.generator.biome.selector.PerBlockBiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.MathHelper;
import org.spout.api.util.cuboid.CuboidShortBuffer;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.world.generator.VanillaBiomeChunkGenerator;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.nether.populator.NetherCavePopulator;

public class NetherGenerator extends VanillaBiomeChunkGenerator {
	public static final int HEIGHT = 128;
	public static final int SEA_LEVEL = 31;
	// noise for generation
	private static final Perlin ELEVATION = new Perlin();
	private static final Perlin ROUGHNESS = new Perlin();
	private static final Perlin DETAIL = new Perlin();
	private static final Turbulence TURBULENCE = new Turbulence();
	private static final ScalePoint SCALE = new ScalePoint();
	private static final Clamp FINAL = new Clamp();
	// noise for block replacement
	private static final Perlin BLOCK_REPLACER = new Perlin();

	static {
		ELEVATION.setFrequency(0.012);
		ELEVATION.setLacunarity(1);
		ELEVATION.setNoiseQuality(NoiseQuality.STANDARD);
		ELEVATION.setPersistence(0.7);
		ELEVATION.setOctaveCount(1);

		ROUGHNESS.setFrequency(0.0318);
		ROUGHNESS.setLacunarity(1);
		ROUGHNESS.setNoiseQuality(NoiseQuality.STANDARD);
		ROUGHNESS.setPersistence(0.9);
		ROUGHNESS.setOctaveCount(1);

		DETAIL.setFrequency(0.042);
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

		TURBULENCE.SetSourceModule(0, add);
		TURBULENCE.setFrequency(0.01);
		TURBULENCE.setPower(8);
		TURBULENCE.setRoughness(1);

		SCALE.SetSourceModule(0, TURBULENCE);
		SCALE.setxScale(0.7);
		SCALE.setyScale(1);
		SCALE.setzScale(0.7);

		FINAL.SetSourceModule(0, SCALE);
		FINAL.setLowerBound(-1);
		FINAL.setUpperBound(1);

		BLOCK_REPLACER.setFrequency(0.35);
		BLOCK_REPLACER.setLacunarity(1);
		BLOCK_REPLACER.setNoiseQuality(NoiseQuality.FAST);
		BLOCK_REPLACER.setPersistence(0.7);
		BLOCK_REPLACER.setOctaveCount(1);
	}

	public NetherGenerator() {
		super(HEIGHT, VanillaBiomes.NETHERRACK);
	}

	@Override
	public void registerBiomes() {
		setSelector(new PerBlockBiomeSelector(VanillaBiomes.NETHERRACK));
		addPopulators(new NetherCavePopulator(), new BiomePopulator(getBiomeMap()));
		register(VanillaBiomes.NETHERRACK);
	}

	@Override
	public String getName() {
		return "VanillaNether";
	}

	@Override
	protected void generateTerrain(CuboidShortBuffer blockData, int x, int y, int z, BiomeManager biomeManager, long seed) {
		ELEVATION.setSeed((int) seed * 23);
		ROUGHNESS.setSeed((int) seed * 29);
		DETAIL.setSeed((int) seed * 17);
		TURBULENCE.setSeed((int) seed * 53);
		final int size = Chunk.BLOCKS.SIZE;
		final double[][][] noise = WorldGeneratorUtils.fastNoise(FINAL, size, size, size, 4, x, y, z);
		// build the density maps
		if (y >= HEIGHT - size * 2) {
			// density noise with higher values with greater y + yy
			final int smoothOffset = size * 2 - HEIGHT + y;
			for (int xx = 0; xx < size; xx++) {
				for (int yy = 0; yy < size; yy++) {
					for (int zz = 0; zz < size; zz++) {
						noise[xx][yy][zz] += (1d / (size + size / 2)) * (yy + smoothOffset);
					}
				}
			}
		} else if (y <= size) {
			// density noise with higher values with lower y + yy
			for (int xx = 0; xx < size; xx++) {
				for (int yy = 0; yy < size; yy++) {
					for (int zz = 0; zz < size; zz++) {
						noise[xx][yy][zz] += -(1d / (size + size / 2)) * (yy + y) + (1d / (size + size / 2)) * (size * 2 - 1);
					}
				}
			}
		}
		// build the chunk from the density map
		for (int xx = 0; xx < size; xx++) {
			for (int yy = 0; yy < size; yy++) {
				for (int zz = 0; zz < size; zz++) {
					final double value = noise[xx][yy][zz];
					if (value >= 0) {
						blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.NETHERRACK.getId());
					} else {
						if (y <= SEA_LEVEL) {
							blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.STATIONARY_LAVA.getId());
						} else {
							blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.AIR.getId());
						}
					}
				}
			}
		}
		//place the bedrock
		replaceBlocks(blockData, x, y, z, seed);
	}

	private void replaceBlocks(CuboidShortBuffer blockData, int x, int y, int z, long seed) {
		BLOCK_REPLACER.setSeed((int) seed * 83);
		final int size = Chunk.BLOCKS.SIZE;
		if (y == 0) {
			for (int xx = 0; xx < size; xx++) {
				for (int zz = 0; zz < size; zz++) {
					final byte bedrockDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x + xx, -5, z + zz) * 2 + 4, 1, 4);
					for (y = 0; y < bedrockDepth; y++) {
						blockData.set(x + xx, y, z + zz, VanillaMaterials.BEDROCK.getId());
					}
				}
			}
		} else if (y == HEIGHT - size) {
			for (int xx = 0; xx < size; xx++) {
				for (int zz = 0; zz < size; zz++) {
					final byte bedrockDepth = (byte) MathHelper.clamp(BLOCK_REPLACER.GetValue(x + xx, -73, z + zz) * 2 + 4, 1, 4);
					for (y = HEIGHT - 1; y >= HEIGHT - 1 - bedrockDepth; y--) {
						blockData.set(x + xx, y, z + zz, VanillaMaterials.BEDROCK.getId());
					}
				}
			}
		}
	}

	@Override
	public Point getSafeSpawn(World world) {
		final Random random = new Random();
		for (byte attempts = 0; attempts < 32; attempts++) {
			final int x = random.nextInt(256) - 127;
			final int z = random.nextInt(256) - 127;
			final int y = getHighestSolidBlock(world, x, z);
			if (y != -1) {
				return new Point(world, x, y + 0.5f, z);
			}
		}
		return new Point(world, 0, 80, 0);
	}

	private int getHighestSolidBlock(World world, int x, int z) {
		int y = HEIGHT / 2;
		if (!world.getBlockMaterial(x, y, z).equals(VanillaMaterials.AIR)) {
			return -1;
		}
		while (world.getBlockMaterial(x, y, z).equals(VanillaMaterials.AIR)) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) instanceof Liquid) {
				return -1;
			}
		}
		return ++y;
	}

	@Override
	public int[][] getSurfaceHeight(World world, int chunkX, int chunkZ) {
		int height = world.getHeight() - 1;
		int[][] heights = new int[Chunk.BLOCKS.SIZE][Chunk.BLOCKS.SIZE];
		for (int x = 0; x < Chunk.BLOCKS.SIZE; x++) {
			for (int z = 0; z < Chunk.BLOCKS.SIZE; z++) {
				heights[x][z] = height;
			}
		}
		return heights;
	}
}
