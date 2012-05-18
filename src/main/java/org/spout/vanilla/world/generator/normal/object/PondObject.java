/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.liquid.Liquid;
import org.spout.vanilla.world.generator.VanillaBiomes;

public class PondObject extends WorldGeneratorObject {
	// rng
	private final Random random;
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

	public PondObject(Random random, PondType type) {
		this.random = random;
		pond = new PondHole();
		liquid = type.liquid;
		stoneWalls = type.stoneWalls;
		stonyTop = type.stonyTop;
		biomeAdaptedSurface = type.biomeSurface;
		generateHeightMaps();
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
						if (!material.isSolid() && material != liquid) {
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
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = (byte) -holeHeightMap[16 * px + pz]; py < 0; py++) {
					world.setBlockMaterial(px + x, py + y, pz + z, liquid, (short) 0, world);
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
							final Block block = world.getBlock(px + x, py + y - 1, pz + z);
							if (random.nextBoolean() && block.getMaterial().isOpaque()) {
								block.setMaterial(VanillaMaterials.STONE);
							}
						}
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
					if (world.getBlockSkyLight(x + px, y + py + 1, z + pz) > 0) {
						final Block block = world.getBlock(x + px, y + py, z + pz);
						final BlockMaterial material = block.getMaterial();
						if (material == VanillaMaterials.DIRT) {
							if (block.getBiomeType() == VanillaBiomes.MUSHROOM) {
								block.setMaterial(VanillaMaterials.MYCELIUM);
							} else {
								block.setMaterial(VanillaMaterials.GRASS);
							}
						} else if (material == VanillaMaterials.STATIONARY_WATER
								&& block.translate(0, 1, 0).getMaterial() == VanillaMaterials.AIR) {
							final Biome biome = block.getBiomeType();
							if (biome == VanillaBiomes.TAIGA || biome == VanillaBiomes.TUNDRA) {
								block.setMaterial(VanillaMaterials.ICE);
							}
						}
					}
				}
			}
		}
	}

	public final void generateHeightMaps() {
		holeHeightMap = new byte[256];
		topHeightMap = new byte[256];
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				holeHeightMap[16 * px + pz] = pond.getDepth(px, pz);
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

	/*
		  * Represents a hole in the shape of a pond
		  */
	private class PondHole {
		private final SphericalNoise[] noise;

		private PondHole() {
			noise = new SphericalNoise[random.nextInt(4) + 4];
			for (byte i = 0; i < noise.length; i++) {
				noise[i] = new SphericalNoise();
			}
		}

		private byte getDepth(int x, int z) {
			byte depth = Byte.MIN_VALUE;
			for (final SphericalNoise n : noise) {
				byte d = n.getValue(x, z);
				depth = d > depth ? d : depth;
			}
			return depth;
		}

		/*
				   * Noise used to generate the pond hole
				   */
		private class SphericalNoise {
			private final float radius;
			private final int xOffset;
			private final int yOffset;
			private final int zOffset;
			private final float xMultiplier;
			private final float yMultiplier;
			private final float zMultiplier;

			private SphericalNoise() {
				int yOff = random.nextInt(2);
				if (random.nextBoolean()) {
					yOff = -yOff;
				}
				yOffset = yOff;
				xOffset = random.nextInt(7) + 4;
				zOffset = random.nextInt(7) + 4;
				radius = random.nextInt(2) + 2;
				xMultiplier = random.nextFloat() * 0.2f + 0.95f;
				yMultiplier = random.nextFloat() + 0.9f;
				zMultiplier = random.nextFloat() * 0.2f + 0.95f;
			}

			private byte getValue(int x, int z) {
				final float xOffNoise = random.nextFloat() * 0.2f;
				final float zOffNoise = random.nextFloat() * 0.2f;
				final float radiusNoise = random.nextFloat() * 0.8f;
				final float multiXNoise = 1f - random.nextFloat() * 0.1f;
				final float multiZNoise = 1f - random.nextFloat() * 0.1f;
				final float value = (float) Math.sqrt(Math.pow(radius + radiusNoise, 2) - Math.pow((xMultiplier * multiXNoise) * x - xOffset - xOffNoise, 2) - Math.pow((zMultiplier * multiZNoise) * z - zOffset - zOffNoise, 2));
				return (byte) ((value + yOffset) / yMultiplier);
			}
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
