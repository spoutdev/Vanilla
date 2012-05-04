/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.generator.normal.decorator;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.item.generic.Liquid;

import java.util.Random;

public class PondDecorator implements BiomeDecorator {
	@Override
	public void populate(Chunk chunk, Random random) {
	}

	@SuppressWarnings("unused")
	private void generateWaterPond(World world, int x, int y, int z, Random random) {
		x -= 8;
		z -= 8;
		final PondHole hole = new PondHole(random);
		final byte[] waterBlocks = getNoiseMap(hole);
		final byte[] airBlocks = getNoiseMap(hole);
		if (!canBuildPondHole(world, x, y, z, waterBlocks, VanillaMaterials.STATIONARY_WATER) || !canBuildPondTop(world, x, y, z, airBlocks)) {
			return;
		}
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = (byte) -waterBlocks[16 * px + pz]; py < 0; py++) {
					world.getBlock(px + x, py + y, pz + z).setMaterial(VanillaMaterials.STATIONARY_WATER);
				}
				for (byte py = 0; py < airBlocks[16 * px + pz]; py++) {
					world.getBlock(px + x, py + y, pz + z).setMaterial(VanillaMaterials.AIR);
				}
			}
		}
		finalizeSurface(world, x, y, z);
	}

	@SuppressWarnings("unused")
	private void generateLavaPond(World world, int x, int y, int z, Random random) {
		x -= 8;
		z -= 8;
		final PondHole hole = new PondHole(random);
		final byte[] lavaBlocks = getNoiseMap(hole);
		final byte[] airBlocks = getNoiseMap(hole);
		if (!canBuildPondHole(world, x, y, z, lavaBlocks, VanillaMaterials.STATIONARY_LAVA) || !canBuildPondTop(world, x, y, z, airBlocks)) {
			return;
		}
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = (byte) -lavaBlocks[16 * px + pz]; py < 0; py++) {
					world.getBlock(px + x, py + y, pz + z).setMaterial(VanillaMaterials.STATIONARY_LAVA);
				}
				for (byte py = 1; py < 5; py++) {
					if (isWallBlock(px, py, pz, lavaBlocks)) {
						world.getBlock(x + px, y - py, z + pz).setMaterial(VanillaMaterials.STONE);
					}
				}
				for (byte py = 0; py < airBlocks[16 * px + pz]; py++) {
					world.getBlock(px + x, py + y, pz + z).setMaterial(VanillaMaterials.AIR);
				}
				for (byte py = 1; py < 5; py++) {
					if (isWallBlock(px, py, pz, airBlocks)) {
						final Block block = world.getBlock(px + x, py + y - 1, pz + z);
						if (block.getMaterial().isOpaque() && random.nextBoolean()) {
							block.setMaterial(VanillaMaterials.STONE).update(true);
						}
					}
				}
			}
		}
		finalizeSurface(world, x, y, z);
	}

	private void finalizeSurface(World world, int x, int y, int z) {
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = -1; py < 4; py++) {
					final Block block = world.getBlock(x + px, y + py, z + pz);
					if (block.getMaterial() == VanillaMaterials.DIRT && world.getBlockSkyLight(x + px, y + py + 1, z + pz) > 0) {
						/*Biome biome = block.getBiome(); Waiting for getBiome() method
						if (biome == Biome.MUSHROOM_ISLAND || biome == Biome.MUSHROOM_SHORE) {
						block.setMaterial(VanillaMaterials.MYCEL);
						} else {
						block.setMaterial(VanillaMaterials.GRASS);
						}*/
					}
					// TODO: freeze water blocks if biome temperature is bellow 0.15f and blocks can see the sky
				}
			}
		}
	}

	private byte[] getNoiseMap(PondHole hole) {
		final byte[] noiseMap = new byte[256];
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				noiseMap[16 * px + pz] = hole.getDepth(px, pz);
			}
		}
		return noiseMap;
	}

	private boolean canBuildPondHole(World world, int x, int y, int z, byte[] liquidBlocks, BlockMaterial liquid) {
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = 1; py < 5; py++) {
					if (isWallBlock(px, py, pz, liquidBlocks)) {
						final BlockMaterial material = world.getBlockMaterial(x + px, y - py, z + pz);
						if (!material.isOpaque() && material != liquid) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private boolean canBuildPondTop(World world, int x, int y, int z, byte[] airBlocks) {
		for (byte px = 0; px < 16; px++) {
			for (byte pz = 0; pz < 16; pz++) {
				for (byte py = 1; py < 5; py++) {
					if (isWallBlock(px, py, pz, airBlocks)) {
						final BlockMaterial material = world.getBlockMaterial(x + px, y + py, z + pz);
						if (material instanceof Liquid) {
							return false;
						}
					}
				}
			}
		}
		return true;
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

	@SuppressWarnings("unused")
	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}

	/*
	 * Represents a hole in the shape of a pond
	 */
	private static class PondHole {
		private final SphericalNoise[] noise;

		private PondHole(Random random) {
			noise = new SphericalNoise[random.nextInt(4) + 4];
			for (byte i = 0; i < noise.length; i++) {
				noise[i] = new SphericalNoise(random);
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
		private static class SphericalNoise {
			private final Random random;
			private final float radius;
			private final int xOffset;
			private final int yOffset;
			private final int zOffset;
			private final float xMultiplier;
			private final float yMultiplier;
			private final float zMultiplier;

			private SphericalNoise(Random random) {
				this.random = random;
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
}
