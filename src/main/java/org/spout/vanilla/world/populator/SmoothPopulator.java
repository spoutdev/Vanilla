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
package org.spout.vanilla.world.populator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.generator.Populator;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

/**
 * Populator ran at stage 2 of world generation which serves to smooth the
 * borders between biomes.
 */
public class SmoothPopulator extends Populator {
	// area to smooth per populate call
	private final static byte SMOOTH_SIZE = 20;
	private final static byte OFFSET;
	// the floor of half the value of the side of the square
	// to sample when smoothing. (byte) Math.floor((double) SIDE_LENGTH / 2D);
	private final static byte SAMPLE_SIZE = 2;
	private final static float SMOOTHING_POWER = 0.85f;
	// ignored materials for height mapping
	private static final Set<BlockMaterial> IGNORED = new HashSet<BlockMaterial>();
	// fluids
	private static final Set<BlockMaterial> FLUIDS = new HashSet<BlockMaterial>();

	static {
		OFFSET = (SMOOTH_SIZE - 16) / 2;
		FLUIDS.add(VanillaMaterials.AIR);
		FLUIDS.add(VanillaMaterials.WATER);
		FLUIDS.add(VanillaMaterials.STATIONARY_WATER);
		FLUIDS.add(VanillaMaterials.LAVA);
		FLUIDS.add(VanillaMaterials.STATIONARY_LAVA);
		FLUIDS.add(VanillaMaterials.ICE);
		IGNORED.add(VanillaMaterials.ICE);
		IGNORED.add(VanillaMaterials.SNOW);
	}
	
	public SmoothPopulator() {
		super(true);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;// only one populate call per 16x16 'world column'
		}
		// work at world level to prevent bad smoothing of vertical edges
		final World world = chunk.getWorld();
		final int worldX = chunk.getBlockX() - OFFSET;
		final int worldZ = chunk.getBlockZ() - OFFSET;
		// allocate the height map to smooth
		final byte[] heightMap = new byte[SMOOTH_SIZE * SMOOTH_SIZE];
		// fill the height map to smooth
		// and check for biome variation at the same time
		// if there's no variation, smoothing will be aborted
		Biome lastBiome = null;
		boolean hasBiomeVariations = false;
		for (byte x = 0; x < SMOOTH_SIZE; x++) {
			for (byte z = 0; z < SMOOTH_SIZE; z++) {
				byte y = getHighestNonFluidTopBlock(world, worldX + x, worldZ + z);
				if (!hasBiomeVariations) {
					final Biome currentBiome = world.getBiomeType(worldX + x, y, worldZ + z);
					if (lastBiome != null) {
						hasBiomeVariations = currentBiome != lastBiome;
					}
					lastBiome = currentBiome;
				}
				heightMap[x + z * SMOOTH_SIZE] = y;
			}
		}
		if (!hasBiomeVariations) {
			return;// no biome variations, smoothing is aborted
		}
		final byte[] smoothedHeightMap = smooth(heightMap, SMOOTH_SIZE, SMOOTH_SIZE);
		// apply the changes
		for (byte x = 0; x < SMOOTH_SIZE; x++) {
			for (byte z = 0; z < SMOOTH_SIZE; z++) {
				final short index = (short) (x + z * SMOOTH_SIZE);
				shiftBlockColumnOnY(world, worldX + x, worldZ + z, (byte) (smoothedHeightMap[index] - heightMap[index]));
			}
		}
	}

	// get the highest solid block, at world level
	private byte getHighestSolidBlock(World world, int x, int z) {
		byte y = 127;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		return y;
	}

	// get the highest non fluid block, at world level, starting from the bottom
	private byte getHighestNonFluidBottomBlock(World world, int x, int z) {
		byte y = 0;
		BlockMaterial material;
		while (!FLUIDS.contains(material = world.getBlockMaterial(x, y, z))
				&& !IGNORED.contains(material)) {
			y++;
			if (y == 127) {
				return -1;
			}
		}
		y--;
		return y;
	}

	// get the highest non fluid block, at world level, starting from the top
	private byte getHighestNonFluidTopBlock(World world, int x, int z) {
		byte y = 127;
		BlockMaterial material;
		while (FLUIDS.contains(material = world.getBlockMaterial(x, y, z))
				|| IGNORED.contains(material)) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		return y;
	}

	// get the lowest block from the top most overhang
	private byte getLowestOverhangBlock(World world, int x, int z) {
		boolean hasHitSolid = false;
		for (byte y = 127; y >= 0; y--) {
			BlockMaterial material = world.getBlockMaterial(x, y, z);
			if (FLUIDS.contains(material) || IGNORED.contains(material)) {
				if (!hasHitSolid) {
					continue;
				} else {
					y++;
					return y;
				}
			} else {
				hasHitSolid = true;
			}
		}
		return -1;
	}

	// get the lowest non bedrock block, at world level
	private byte getLowestNonBedrockBlock(World world, int x, int z) {
		byte y = 0;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.BEDROCK) {
			y++;
			if (y == 127) {
				return -1;
			}
		}
		return y;
	}

	// shift a column on y, replacing the blocks missing
	// with the last block of the column, ignoring bedrock, water and air
	// positive shift values move the column upwards, negative, downwards
	private void shiftBlockColumnOnY(World world, int x, int z, byte shift) {
		if (shift == 0) { // no shift
			return;
		}
		if (shift >> 31 != 0) { // shift down
			// get the starting y
			byte startY;
			startY = getLowestOverhangBlock(world, x, z);
			if (startY == -1) {
				startY = getLowestNonBedrockBlock(world, x, z);
				if (startY == -1) {
					return;
				}
				startY -= shift;
			}
			// do the actual shifting
			for (byte y = startY; y < 127; y++) {
				final Block block = world.getBlock(x, y, z);
				final BlockMaterial material = block.getMaterial();
				if (FLUIDS.contains(material)) {
					continue;
				}
				final Block destination = block.translate(0, shift, 0); // shift is negative!
				if (destination.getMaterial() == VanillaMaterials.BEDROCK) {
					return;
				}
				destination.setMaterial(material);
				block.setMaterial(y <= NormalGenerator.SEA_LEVEL
						? VanillaMaterials.STATIONARY_WATER : VanillaMaterials.AIR);
			}
			// fix the surface
			final byte highestNonFluidBlockY = getHighestNonFluidBottomBlock(world, x, z);
			if (highestNonFluidBlockY != -1) {
				fixSurface(world, x, highestNonFluidBlockY, z);
			}
			final byte highestBlockY = getHighestSolidBlock(world, x, z);
			if (highestBlockY != -1) {
				fixIceAndSnow(world, x, highestBlockY, z);
			}
		} else { // shift up
			// get the end y
			final byte endY = getLowestNonBedrockBlock(world, x, z);
			if (endY == -1) {
				return;
			}
			// do the actual shifting
			for (byte y = 127; y >= 0; y--) {
				final Block block = world.getBlock(x, y, z);
				BlockMaterial currentMaterial = block.getMaterial();
				if (FLUIDS.contains(currentMaterial)) {
					continue;
				}
				block.translate(0, shift, 0).setMaterial(currentMaterial);
			}
			final BlockMaterial lastMaterial = world.getBlockMaterial(x, endY + shift, z);
			for (byte y = (byte) (endY + shift - 1); y <= endY; y--) {
				world.setBlockMaterial(x, y, z, lastMaterial, (short) 0, world);
			}
			// fix the surface
			final byte highestNonFluidBlockY = getHighestNonFluidBottomBlock(world, x, z);
			if (highestNonFluidBlockY != -1) {
				fixSurface(world, x, highestNonFluidBlockY, z);
			}
		}
	}

	// restore the surface cover
	private void fixSurface(World world, int x, int y, int z) {
		final BlockMaterial material = world.getBlockMaterial(x, y, z);
		if (material == VanillaMaterials.DIRT
				&& world.getBlockMaterial(x, y + 1, z) == VanillaMaterials.AIR) {
			final Biome biome = world.getBiomeType(x, y, z);
			if (biome == VanillaBiomes.MUSHROOM) {
				world.setBlockMaterial(x, y, z, VanillaMaterials.MYCELIUM, (short) 0, world);
			} else {
				world.setBlockMaterial(x, y, z, VanillaMaterials.GRASS, (short) 0, world);
			}
		} else if ((material == VanillaMaterials.GRASS
				|| material == VanillaMaterials.MYCELIUM)
				&& world.getBlockMaterial(x, y + 1, z) != VanillaMaterials.AIR) {
			world.setBlockMaterial(x, y, z, VanillaMaterials.DIRT, (short) 0, world);
		}
	}

	// freeze water and remove snow when necessaty
	private void fixIceAndSnow(World world, int x, int y, int z) {
		final Block middle = world.getBlock(x, y, z);
		final BlockMaterial material = middle.getMaterial();
		if (material == VanillaMaterials.STATIONARY_WATER) {
			for (BlockFace face : BlockFaces.NSEW) {
				if (middle.translate(face).getMaterial() == VanillaMaterials.ICE) {
					middle.setMaterial(VanillaMaterials.ICE);
					return;
				}
			}
		} else if (material == VanillaMaterials.SNOW) {
			for (BlockFace face : BlockFaces.NSEW) {
				final BlockMaterial adjacent = middle.translate(face).getMaterial();
				if (adjacent == VanillaMaterials.ICE
						|| adjacent == VanillaMaterials.STATIONARY_WATER) {
					middle.setMaterial(adjacent);
					return;
				}
			}
		}
	}

	// simple smoothing based on the 'box filtering' algorithm
	// will ruin rough terrain
	private byte[] smooth(byte[] heightMap, byte width, byte height) {
		final byte[] smoothHeightMap = new byte[width * height];
		for (byte x = 0; x < width; x++) {
			for (byte y = 0; y < height; y++) {
				short totalValue = 0;
				byte valueCounter = 0;
				for (byte xx = (byte) (x - SAMPLE_SIZE); xx < x + SAMPLE_SIZE; xx++) {
					for (byte yy = (byte) (y - SAMPLE_SIZE); yy < y + SAMPLE_SIZE; yy++) {
						if (xx < 0 || yy < 0 || xx >= width || yy >= height) {
							continue;
						}
						totalValue += heightMap[xx + yy * width];
						valueCounter++;
					}
				}
				final byte oldValue = heightMap[x + y * width];
				final byte difference = (byte) ((totalValue / valueCounter - oldValue) * SMOOTHING_POWER);
				smoothHeightMap[x + y * width] = (byte) (oldValue + difference);
			}
		}
		return smoothHeightMap;
	}
}
