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

import org.spout.vanilla.world.generator.normal.biome.GrassyBiome;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;
import org.spout.vanilla.world.generator.normal.biome.SandyBiome;
import org.spout.vanilla.world.generator.normal.biome.SnowyBiome;

public class SmoothPopulator extends Populator {
	private static final byte CHUNK_SIZE = 16;
	private static final byte SMOOTH_SIZE = 5;
	private static final byte TOTAL_SIZE = CHUNK_SIZE + SMOOTH_SIZE * 2;

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
		final NormalBiome[] biomeMap = new NormalBiome[TOTAL_SIZE * TOTAL_SIZE];
		for (byte xx = 0; xx < TOTAL_SIZE; xx++) {
			for (byte zz = 0; zz < TOTAL_SIZE; zz++) {
				final Biome biome = world.getBiomeType(x + xx - SMOOTH_SIZE, 64, z + zz - SMOOTH_SIZE);
				if (biome instanceof NormalBiome) {
					biomeMap[xx + zz * TOTAL_SIZE] = (NormalBiome) biome;
				}
			}
		}
		// build a chunk sized biome map with smooth biomes at seams
		final NormalBiome[] smoothBiomeMap = new NormalBiome[CHUNK_SIZE * CHUNK_SIZE];
		for (byte xx = 0; xx < CHUNK_SIZE; xx++) {
			for (byte zz = 0; zz < CHUNK_SIZE; zz++) {
				final NormalBiome current = biomeMap[xx + SMOOTH_SIZE + (zz + SMOOTH_SIZE) * TOTAL_SIZE];
				nearbyCheck:
				for (byte sx = (byte) (xx - SMOOTH_SIZE); sx <= xx + SMOOTH_SIZE; sx++) {
					for (byte sz = (byte) (zz - SMOOTH_SIZE); sz <= zz + SMOOTH_SIZE; sz++) {
						final NormalBiome nearby = biomeMap[sx + SMOOTH_SIZE + (sz + SMOOTH_SIZE) * TOTAL_SIZE];
						if (current != nearby) {
							if (current instanceof GrassyBiome) {
								final GrassySmoothBiome smoothBiome = new GrassySmoothBiome();
								smoothBiome.setTopCover(((GrassyBiome) current).getTopCover());
								smoothBiomeMap[xx + zz * CHUNK_SIZE] = smoothBiome;
							} else if (current instanceof SandyBiome) {
								smoothBiomeMap[xx + zz * CHUNK_SIZE] = new SandySmoothBiome();
							} else if (current instanceof SnowyBiome) {
								smoothBiomeMap[xx + zz * CHUNK_SIZE] = new IcySmoothBiome();
							} else {
								smoothBiomeMap[xx + zz * CHUNK_SIZE] = current;
							}
							break nearbyCheck;
						}
					}
				}
				if (smoothBiomeMap[xx + zz * CHUNK_SIZE] == null) {
					smoothBiomeMap[xx + zz * CHUNK_SIZE] = current;
				}
			}
		}
		// change the values of the smooth biomes to averages
		for (byte xx = 0; xx < CHUNK_SIZE; xx++) {
			for (byte zz = 0; zz < CHUNK_SIZE; zz++) {
				final NormalBiome biome = smoothBiomeMap[xx + zz * CHUNK_SIZE];
				if (!(biome instanceof GrassySmoothBiome)
						&& !(biome instanceof SandySmoothBiome)
						&& !(biome instanceof IcySmoothBiome)) {
					continue;
				}
				short minTotal = 0;
				short maxTotal = 0;
				short counter = 0;
				for (byte sx = (byte) (xx - SMOOTH_SIZE); sx < xx + SMOOTH_SIZE; sx++) {
					for (byte sz = (byte) (zz - SMOOTH_SIZE); sz < zz + SMOOTH_SIZE; sz++) {
						final NormalBiome nearby = biomeMap[sx + SMOOTH_SIZE + (sz + SMOOTH_SIZE) * TOTAL_SIZE];
						minTotal += nearby.getMin();
						maxTotal += nearby.getMax();
						counter++;
					}
				}
				if (biome instanceof GrassySmoothBiome) {
					((GrassySmoothBiome) biome).setMinMax((byte) Math.round(minTotal / counter), (byte) Math.round(maxTotal / counter));
				} else if (biome instanceof SandySmoothBiome) {
					((SandySmoothBiome) biome).setMinMax((byte) Math.round(minTotal / counter), (byte) Math.round(maxTotal / counter));
				} else if (biome instanceof IcySmoothBiome) {
					((IcySmoothBiome) biome).setMinMax((byte) Math.round(minTotal / counter), (byte) Math.round(maxTotal / counter));
				}
			}
		}
		// apply the changes by regenerating terrain with the new averaged values
		for (byte xx = 0; xx < CHUNK_SIZE; xx++) {
			for (byte zz = 0; zz < CHUNK_SIZE; zz++) {
				final NormalBiome biome = smoothBiomeMap[xx + zz * CHUNK_SIZE];
				if (!(biome instanceof GrassySmoothBiome)
						&& !(biome instanceof SandySmoothBiome)
						&& !(biome instanceof IcySmoothBiome)) {
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

	private static class GrassySmoothBiome extends GrassyBiome {
		public GrassySmoothBiome() {
			super(1001);
		}

		@Override
		public String getName() {
			return "Grassy Smooth Biome";
		}

		@Override
		public void setMinMax(byte min, byte max) {
			this.min = min;
			this.max = max;
		}

		public void setTopCover(BlockMaterial topCover) {
			this.topCover = topCover;
		}
	}

	private static class SandySmoothBiome extends SandyBiome {
		public SandySmoothBiome() {
			super(1002);
		}

		@Override
		public String getName() {
			return "Grassy Smooth Biome";
		}

		@Override
		public void setMinMax(byte min, byte max) {
			this.min = min;
			this.max = max;
		}
	}

	private static class IcySmoothBiome extends SnowyBiome {
		public IcySmoothBiome() {
			super(1003);
		}

		@Override
		public String getName() {
			return "Grassy Smooth Biome";
		}

		@Override
		public void setMinMax(byte min, byte max) {
			this.min = min;
			this.max = max;
		}
	}
}
