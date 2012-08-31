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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.MaterialRegistry;
import org.spout.api.util.cuboid.CuboidShortBuffer;
import org.spout.api.util.map.TIntPairObjectHashMap;

import org.spout.vanilla.world.generator.normal.biome.NormalBiome;
import org.spout.vanilla.world.generator.normal.biome.grassy.GrassyBiome;
import org.spout.vanilla.world.generator.normal.biome.icy.IcyBiome;
import org.spout.vanilla.world.generator.normal.biome.sandy.SandyBiome;

public class SmoothPopulator extends Populator {
	private static final byte CHUNK_SIZE = (byte) Chunk.BLOCKS.SIZE;
	private static final byte SMOOTH_SIZE = 5;
	private static final byte TOTAL_SIZE = (byte) (CHUNK_SIZE + SMOOTH_SIZE * 2);

	public SmoothPopulator() {
		super(true);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		// smooth only once per world column
		if (chunk.getY() != 4) {
			return;
		}
		// some useful variables
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		// build the biome map
		final TIntPairObjectHashMap<NormalBiome> biomeMap = new TIntPairObjectHashMap<NormalBiome>(TOTAL_SIZE * TOTAL_SIZE);
		for (byte xx = -SMOOTH_SIZE; xx < CHUNK_SIZE + SMOOTH_SIZE; xx++) {
			for (byte zz = -SMOOTH_SIZE; zz < CHUNK_SIZE + SMOOTH_SIZE; zz++) {
				final Biome biome = world.getBiomeType(x + xx, 64, z + zz);
				if (biome instanceof NormalBiome) {
					biomeMap.put(xx, zz, (NormalBiome) biome);
				}
			}
		}
		// find the biomes to smooth
		final TIntPairObjectHashMap<NormalBiome> smoothBiomeMap = new TIntPairObjectHashMap<NormalBiome>(CHUNK_SIZE * CHUNK_SIZE);
		for (byte xx = 0; xx < CHUNK_SIZE; xx++) {
			for (byte zz = 0; zz < CHUNK_SIZE; zz++) {
				final NormalBiome current = biomeMap.get(xx, zz);
				nearbyCheck:
				for (byte sx = -SMOOTH_SIZE; sx <= SMOOTH_SIZE; sx++) {
					for (byte sz = -SMOOTH_SIZE; sz <= SMOOTH_SIZE; sz++) {
						if (current != biomeMap.get(xx + sx, zz + sz)) {
							if (current instanceof GrassyBiome) {
								final GrassySmoothBiome grassySmoothBiome = new GrassySmoothBiome();
								grassySmoothBiome.setTopCover(((GrassyBiome) current).getTopCover());
								smoothBiomeMap.put(xx, zz, grassySmoothBiome);
							} else if (current instanceof SandyBiome) {
								smoothBiomeMap.put(xx, zz, new SandySmoothBiome());
							} else if (current instanceof IcyBiome) {
								smoothBiomeMap.put(xx, zz, new IcySmoothBiome());
							} else {
								smoothBiomeMap.put(xx, zz, current);
							}
							break nearbyCheck;
						}
					}
				}
				if (!smoothBiomeMap.containsKey(xx, zz)) {
					smoothBiomeMap.put(xx, zz, current);
				}
			}
		}
		// change the values of the smooth biomes to averages
		for (byte xx = 0; xx < CHUNK_SIZE; xx++) {
			for (byte zz = 0; zz < CHUNK_SIZE; zz++) {
				final NormalBiome biome = smoothBiomeMap.get(xx, zz);
				if (!(biome instanceof SmoothBiome)) {
					continue;
				}
				float minTotal = 0;
				float maxTotal = 0;
				short counter = 0;
				for (byte sx = -SMOOTH_SIZE; sx <= SMOOTH_SIZE; sx++) {
					for (byte sz = -SMOOTH_SIZE; sz <= SMOOTH_SIZE; sz++) {
						final NormalBiome nearby = biomeMap.get(xx + sx, zz + sz);
						minTotal += nearby.getMin();
						maxTotal += nearby.getMax();
						counter++;
					}
				}
				if (biome instanceof SmoothBiome) {
					((SmoothBiome) biome).setMinMax(minTotal / counter, maxTotal / counter);
				}
			}
		}
		// apply the changes by regenerating terrain with the new averaged values
		for (byte xx = 0; xx < CHUNK_SIZE; xx++) {
			for (byte zz = 0; zz < CHUNK_SIZE; zz++) {
				final NormalBiome biome = smoothBiomeMap.get(xx, zz);
				if (!(biome instanceof SmoothBiome)) {
					continue;
				}
				final int lx = x + xx;
				final int lz = z + zz;
				final CuboidShortBuffer buffer = new CuboidShortBuffer(world, lx, 0, lz, 1, 256, 1);
				biome.generateColumn(buffer, lx, 0, lz);
				for (short y = 0; y < 256; y++) {
					world.setBlockMaterial(lx, y, lz, (BlockMaterial) MaterialRegistry.get(buffer.get(lx, y, lz)), (short) 0, world);
				}
			}
		}
	}

	private interface SmoothBiome {
		public void setMinMax(float min, float max);
	}

	private static class GrassySmoothBiome extends GrassyBiome implements SmoothBiome {
		public GrassySmoothBiome() {
			super(1001);
		}

		@Override
		public String getName() {
			return "Grassy Smooth Biome";
		}

		@Override
		public void setMinMax(float min, float max) {
			this.min = min;
			this.max = max;
		}

		public void setTopCover(BlockMaterial topCover) {
			this.topCover = topCover;
		}
	}

	private static class SandySmoothBiome extends SandyBiome implements SmoothBiome {
		public SandySmoothBiome() {
			super(1002);
		}

		@Override
		public String getName() {
			return "Grassy Smooth Biome";
		}

		@Override
		public void setMinMax(float min, float max) {
			this.min = min;
			this.max = max;
		}
	}

	private static class IcySmoothBiome extends IcyBiome implements SmoothBiome {
		public IcySmoothBiome() {
			super(1003);
		}

		@Override
		public String getName() {
			return "Grassy Smooth Biome";
		}

		@Override
		public void setMinMax(float min, float max) {
			this.min = min;
			this.max = max;
		}
	}
}
