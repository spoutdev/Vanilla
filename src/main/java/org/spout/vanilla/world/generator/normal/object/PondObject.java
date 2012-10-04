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
package org.spout.vanilla.world.generator.normal.object;

import java.util.Random;

import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.world.generator.normal.biome.grassy.GrassyBiome;
import org.spout.vanilla.world.generator.normal.biome.sandy.SandyBiome;
import org.spout.vanilla.world.generator.normal.biome.snowy.SnowyBiome;
import org.spout.vanilla.world.generator.object.RandomObject;
import org.spout.vanilla.world.generator.object.RandomizableObject;

public class PondObject extends RandomObject implements RandomizableObject {
	// pond instance for generating the height maps
	private final PondHole pond;
	// height maps for generation
	private byte[] holeHeightMap;
	private byte[] topHeightMap;
	// the liquid, can be any block really
	private BlockMaterial liquid;
	// extras
	private boolean stoneWalls;
	private boolean stonyTop;
	private boolean biomeAdaptedSurface;

	public PondObject(PondType type) {
		this(null, type);
	}

	public PondObject(Random random, PondType type) {
		super(random);
		pond = new PondHole();
		liquid = type.liquid;
		stoneWalls = type.stoneWalls;
		stonyTop = type.stonyTop;
		biomeAdaptedSurface = type.biomeSurface;
		randomize();
	}

	@Override
	public boolean canPlaceObject(World world, int x, int y, int z) {
		x -= 8;
		z -= 8;
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = 1; py < 5; py++) {
					if (isWallBlock(px, py, pz, holeHeightMap)) {
						final BlockMaterial material = world.getBlockMaterial(x + px, y - py, z + pz);
						if (material.isPenetrable() && material != liquid) {
							return false;
						}
					}
					if (isWallBlock(px, py, pz, topHeightMap)) {
						if (world.getBlockMaterial(x + px, y + py, z + pz) instanceof Liquid) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public void placeObject(World world, int x, int y, int z) {
		x -= 8;
		z -= 8;
		final boolean sandy = world.getBiome(x, y, z) instanceof SandyBiome;
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				boolean columnHasWater = false;
				for (byte py = (byte) -holeHeightMap[16 * px + pz]; py < 0; py++) {
					world.setBlockMaterial(px + x, py + y, pz + z, liquid, (short) 0, world);
					columnHasWater = true;
				}
				if (stoneWalls) {
					for (byte py = 1; py < 5; py++) {
						if (isWallBlock(px, py, pz, holeHeightMap)) {
							world.setBlockMaterial(x + px, y - py, z + pz, VanillaMaterials.STONE, (short) 0, world);
						}
					}
				}
				for (byte py = 0; py < topHeightMap[16 * px + pz]; py++) {
					world.setBlockMaterial(px + x, py + y, pz + z, VanillaMaterials.AIR, (short) 0, world);
				}
				if (stonyTop) {
					for (byte py = 1; py < 5; py++) {
						if (isWallBlock(px, py, pz, topHeightMap)) {
							final Block block = world.getBlock(px + x, py + y - 1, pz + z, world);
							if (random.nextBoolean() && block.getMaterial().isOpaque()) {
								block.setMaterial(VanillaMaterials.STONE);
							}
						}
					}
				}
				if (sandy && columnHasWater) {
					int ty = topHeightMap[16 * px + pz] + y;
					if (world.getBlockMaterial(x + px, ty, z + pz).equals(VanillaMaterials.SAND)) {
						world.setBlockMaterial(x + px, ty, z + pz, VanillaMaterials.SANDSTONE, (short) 0, world);
					}
				}
			}
		}
		if (biomeAdaptedSurface) {
			finalizeSurface(world, x, y, z);
		}
	}

	private void finalizeSurface(World world, int x, int y, int z) {
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = -1; py < 4; py++) {
					final Block block = world.getBlock(x + px, y + py, z + pz, world);
					if (block.isAtSurface()) {
						final BlockMaterial material = block.getMaterial();
						if (material == VanillaMaterials.DIRT) {
							final BlockMaterial top;
							final Biome biome = block.getBiomeType();
							if (biome instanceof GrassyBiome) {
								top = ((GrassyBiome) biome).getGroundCover()[0].getMaterial(true);
							} else {
								top = VanillaMaterials.GRASS;
							}
							block.setMaterial(top);
						} else if (material == VanillaMaterials.STATIONARY_WATER
								&& block.translate(0, 1, 0).isMaterial(VanillaMaterials.AIR)) {
							if (block.getBiomeType() instanceof SnowyBiome) {
								block.setMaterial(VanillaMaterials.ICE);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public final void randomize() {
		holeHeightMap = new byte[256];
		topHeightMap = new byte[256];
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				holeHeightMap[16 * px + pz] = pond.getDepth(px, pz);
			}
		}
		pond.randomize();
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				topHeightMap[16 * px + pz] = pond.getDepth(px, pz);
			}
		}
	}

	private boolean isWallBlock(int x, int y, int z, byte[] blocks) {
		if (y <= blocks[16 * x + z]) { // Is a fluid block on x, y, z ?
			return false;
		}
		if (x < 15 && y <= blocks[16 * (x + 1) + z]) { //is a fluid block on x + 1, y, z
			return true;
		} else if (x > 0 && y <= blocks[16 * (x - 1) + z]) { //is a fluid block on x - 1, y, z
			return true;
		} else if (z < 15 && y <= blocks[16 * x + z + 1]) { //is a fluid block on x, y, z + 1
			return true;
		} else if (z > 0 && y <= blocks[16 * x + z - 1]) { //is a fluid block on x, y, z - 1
			return true;
		} else if (y > 1 && y <= blocks[16 * x + z] + 1) { //is a fluid block on x, y - 1, z
			return true;
		} else {
			return false;
		}
	}

	public void setLiquid(BlockMaterial liquid) {
		this.liquid = liquid;
	}

	public void biomeAdaptedSurface(boolean biomeAdaptedSurface) {
		this.biomeAdaptedSurface = biomeAdaptedSurface;
	}

	public void stoneWalls(boolean stoneWalls) {
		this.stoneWalls = stoneWalls;
	}

	public void stonyTop(boolean stonyTop) {
		this.stonyTop = stonyTop;
	}

	/*
	 * Represents a hole in the shape of a pond
	 */
	private class PondHole {
		private final SphericalNoise[] noise;

		private PondHole() {
			noise = new SphericalNoise[random.nextInt(4) + 4];
			randomize();
		}

		private void randomize() {
			for (byte i = 0; i < noise.length; i++) {
				noise[i] = new SphericalNoise(random.nextLong());
			}
		}

		private byte getDepth(int x, int z) {
			byte depth = 0;
			for (final SphericalNoise n : noise) {
				final byte d = (byte) n.getValue(x, z);
				depth = d > depth ? d : depth;
			}
			return depth;
		}

		/*
		 * Noise used to generate the pond hole
		 */
		private class SphericalNoise {
			private final Random random = new Random();
			private final float xOffset;
			private final float yOffset;
			private final float zOffset;
			private final float xScale;
			private final float yScale;
			private final float zScale;

			private SphericalNoise(long seed) {
				random.setSeed(seed);
				xScale = random.nextFloat() * 6 + 3;
				yScale = random.nextFloat() * 4 + 2;
				zScale = random.nextFloat() * 6 + 3;
				xOffset = random.nextFloat() * (16 - xScale - 2) + 1 + xScale / 2;
				yOffset = random.nextFloat() * (8 - yScale - 4) + 2 + yScale / 2;
				zOffset = random.nextFloat() * (16 - zScale - 2) + 1 + zScale / 2;
			}

			private float getValue(int x, int z) {
				final float sizeX = (x - xOffset) / (xScale / 2);
				final float sizeZ = (z - zOffset) / (zScale / 2);
				return (float) (Math.sqrt(1 - sizeX * sizeX - sizeZ * sizeZ) * (yScale / 2) + yOffset) / 2;
			}
		}
	}

	public static enum PondType {
		WATER(VanillaMaterials.STATIONARY_WATER, false, false, true),
		LAVA(VanillaMaterials.STATIONARY_LAVA, true, true, true);
		//
		private final BlockMaterial liquid;
		private final boolean stoneWalls;
		private final boolean stonyTop;
		private final boolean biomeSurface;

		private PondType(BlockMaterial liquid, boolean stoneWalls, boolean stonyTop, boolean biomeSurface) {
			this.liquid = liquid;
			this.stoneWalls = stoneWalls;
			this.stonyTop = stonyTop;
			this.biomeSurface = biomeSurface;
		}

		public boolean biomeAdaptedSurface() {
			return biomeSurface;
		}

		public BlockMaterial getLiquid() {
			return liquid;
		}

		public boolean stoneWalls() {
			return stoneWalls;
		}

		public boolean stonyTop() {
			return stonyTop;
		}
	}
}
