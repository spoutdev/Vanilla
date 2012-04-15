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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

public class MushroomDecorator implements BiomeDecorator {

	/*
	 * Stores the differant types of mushrooms.
	 */
	private static final List<BlockMaterial> MUSHROOMS = new ArrayList<BlockMaterial>();
	private static final List<BlockMaterial> HUGE_MUSHROOMS = new ArrayList<BlockMaterial>();
	/*
	 * These arguments control the size of the huge mushrooms.
	 */
	private static final byte BASE_HEIGHT = 4;
	private static final byte RED_CAP_RADIUS = 2;
	private static final byte BROWN_CAP_RADIUS = 3;
	private static final byte RED_CAP_THICKNESS = 4;
	private static final byte BROWN_CAP_THICKNESS = 1;
	/*
	 * These arguments control the radius to check for huge mushrooms.
	 */
	private static final byte INIT_CHECK_RADIUS = 0;
	private static final byte FIN_CHECK_RADIUS = 3;

	static {
		MUSHROOMS.add(VanillaMaterials.RED_MUSHROOM);
		MUSHROOMS.add(VanillaMaterials.BROWN_MUSHROOM);
		HUGE_MUSHROOMS.add(VanillaMaterials.HUGE_RED_MUSHROOM);
		HUGE_MUSHROOMS.add(VanillaMaterials.HUGE_BROWN_MUSHROOM);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		/* TODO: determine how many mushrooms and huge mushrooms per chunk, depending on biomes.
		 * Suggested numbers based on Vanilla specs:
		 *  Mushroom biome:
		 *	  mushroom: 0 to 3
		 *	  huge mushroom: 0 to 1
		 *  Swamp biome:
		 *	  mushroom: 0 to 7
		 *	  huge mushroom: 0 to 0
		 *  All other biomes:
		 *	  huge mushroom: 0 to 0
		 *	  mushroom: 0 to 0
		 */
	}

	@SuppressWarnings("unused")
	private void generateMushroom(Chunk c, Random ra, int cx, int cy, int cz) {
		c.setBlockMaterial(cx, cy, cz, getRandomMushroomType(ra, false), (short) 0, true, c.getWorld());
	}

	@SuppressWarnings("unused")
	private void generateHugeMushroom(World world, BlockMaterial type, Random ra, int x, int y, int z) {
		final int height = ra.nextInt(3) + BASE_HEIGHT;
		if (y + height > world.getHeight() - 2) {
			return;
		}
		if (!canBuildHugeMushroom(world, x, z, y, height)) {
			return;
		}
		if (!canPlaceMushroom(world, x, y - 1, z)) {
			return;
		}
		world.setBlockMaterial(x, y - 1, z, VanillaMaterials.DIRT, (short) 0, true, world);
		if (type == VanillaMaterials.HUGE_BROWN_MUSHROOM) {
			generateHugeBrownMushroom(world, x, z, y, height);
		} else if (type == VanillaMaterials.HUGE_RED_MUSHROOM) {
			generateHugeRedMushroom(world, x, z, y, height);
		}
	}

	private void generateHugeRedMushroom(World world, int x, int z, int y, int height) {
		final int capYStart = height + y + 1 - RED_CAP_THICKNESS;
		byte capSize = RED_CAP_RADIUS;
		for (int yy = capYStart; yy < y + 1 + height; yy++) { // generate cap
			if (yy == y + height) {
				capSize--;
			}
			for (int xx = x - capSize; xx < x + 1 + capSize; xx++) {
				for (int zz = z - capSize; zz < z + 1 + capSize; zz++) {
					if (yy != y + height) {
						int xDif = Math.abs(x - xx);
						int zDif = Math.abs(z - zz);
						if ((xDif < capSize && zDif < capSize)
								|| (xDif == capSize && zDif == capSize)) {
							continue;
						}
					}
					short data = getRedMushroomCapData(x, y, z, xx, yy, zz, height, capSize);
					world.setBlockMaterial(xx, yy, zz, VanillaMaterials.HUGE_RED_MUSHROOM, data, false, world);
				}
			}
		}
		for (int yy = y; yy < height + y; yy++) { // generate stem
			world.setBlockMaterial(x, yy, z, VanillaMaterials.HUGE_RED_MUSHROOM, (byte) 10, false, world);
		}
	}

	private void generateHugeBrownMushroom(World world, int x, int z, int y, int height) {

		final int capYStart = height + y + 1 - BROWN_CAP_THICKNESS;
		final byte capSize = BROWN_CAP_RADIUS;
		for (int yy = capYStart; yy < y + 1 + height; yy++) { // generate cap
			for (int xx = x - capSize; xx < x + 1 + capSize; xx++) {
				for (int zz = z - capSize; zz < z + 1 + capSize; zz++) {
					if (Math.abs(x - xx) == capSize
							&& Math.abs(z - zz) == capSize) {
						continue;
					}
					short data = getBrownMushroomCapData(x, y, z, xx, yy, zz, height, capSize);
					world.setBlockMaterial(xx, yy, zz, VanillaMaterials.HUGE_BROWN_MUSHROOM, data, false, world);
				}
			}
		}
		for (int yy = y; yy < capYStart; yy++) { // generate stem
			world.setBlockMaterial(x, yy, z, VanillaMaterials.HUGE_BROWN_MUSHROOM, (byte) 10, false, world);
		}
	}

	private boolean canBuildHugeMushroom(World world, int x, int z, int y, int height) {
		height += 2;
		byte radiusToCheck;
		for (int yy = y; yy < y + height; yy++) {
			if (yy == y) {
				radiusToCheck = INIT_CHECK_RADIUS;
			} else {
				radiusToCheck = FIN_CHECK_RADIUS;
			}
			for (int xx = x - radiusToCheck; xx < x + 1 + radiusToCheck; xx++) {
				for (int zz = z - radiusToCheck; zz < z + 1 + radiusToCheck; zz++) {
					BlockMaterial material = world.getBlockMaterial(xx, yy, zz);
					if (material != VanillaMaterials.AIR && material != VanillaMaterials.LEAVES) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean canPlaceMushroom(World world, int x, int y, int z) {
		BlockMaterial material = world.getBlockMaterial(x, y, z);
		return material == VanillaMaterials.DIRT
				|| material == VanillaMaterials.GRASS
				|| material == VanillaMaterials.MYCELIUM;
	}

	private short getRedMushroomCapData(int x, int y, int z, int xx,
			int yy, int zz, int height, int capSize) {
		if (yy == y + height) { // top blocks
			if (Math.abs(x - xx) < capSize && Math.abs(z - zz) < capSize) { // interior blocks
				return 5;
			} else if (x - xx == capSize) { // exterior west blocks
				if (z - zz == capSize) { // west & north
					return 1;
				} else if (z - zz == -capSize) { // west & south
					return 7;
				} else { // west only
					return 4;
				}
			} else if (x - xx == -capSize) { // exterior east blocks
				if (z - zz == capSize) { // east & north
					return 3;
				} else if (z - zz == -capSize) { // east & south
					return 9;
				} else { // east only
					return 6;
				}
			} else if (z - zz == capSize) { // exterior north only
				return 2;
			} else if (z - zz == -capSize) { // exterior south only
				return 8;
			} else { // unknown location
				return 0;
			}
		} else { // bottom blocks
			if (x - xx == capSize) { // exterior west blocks
				if (z - zz == capSize - 1) { // west & north
					return 1;
				} else if (z - zz == -capSize + 1) { // west & south
					return 7;
				} else { // west only
					return 4;
				}
			} else if (x - xx == -capSize) { // exterior east blocks
				if (z - zz == capSize - 1) { // east & north
					return 3;
				} else if (z - zz == -capSize + 1) { // east & south
					return 9;
				} else { // east only
					return 6;
				}
			} else if (z - zz == capSize) { // exterior north
				if (x - xx == capSize - 1) { // west & north
					return 1;
				} else if (x - xx == -capSize + 1) { // east & north
					return 3;
				} else { // north only
					return 2;
				}
			} else if (z - zz == -capSize) { // exterior south
				if (x - xx == capSize - 1) { // west & south
					return 7;
				} else if (x - xx == -capSize + 1) { // east & south
					return 9;
				} else { // north only
					return 8;
				}
			} else { // unknown location
				return 0;
			}
		}
	}

	private short getBrownMushroomCapData(int x, int y, int z, int xx,
			int yy, int zz, int height, int capSize) {
		if (Math.abs(x - xx) < capSize && Math.abs(z - zz) < capSize) { // interior blocks
			if (yy < y + height) {
				return 0;
			} else {
				return 5;
			}
		} else if (x - xx == capSize) { // exterior west blocks
			if (z - zz == capSize - 1) { // west & north
				return 1;
			} else if (z - zz == -capSize + 1) { // west & south
				return 7;
			} else { // west only
				return 4;
			}
		} else if (x - xx == -capSize) { // exterior east blocks
			if (z - zz == capSize - 1) { // east & north
				return 3;
			} else if (z - zz == -capSize + 1) { // east & south
				return 9;
			} else { // east only
				return 6;
			}
		} else if (z - zz == capSize) { // exterior north
			if (x - xx == capSize - 1) { // west & north
				return 1;
			} else if (x - xx == -capSize + 1) { // east & north
				return 3;
			} else { // north only
				return 2;
			}
		} else if (z - zz == -capSize) { // exterior south
			if (x - xx == capSize - 1) { // west & south
				return 7;
			} else if (x - xx == -capSize + 1) { // east & south
				return 9;
			} else { // north only
				return 8;
			}
		} else { // unknown location
			return 0;
		}
	}

	private BlockMaterial getRandomMushroomType(Random random, boolean huge) {
		if (huge) {
			return HUGE_MUSHROOMS.get(random.nextInt(HUGE_MUSHROOMS.size()));
		} else {
			return MUSHROOMS.get(random.nextInt(MUSHROOMS.size()));
		}
	}

	@SuppressWarnings("unused")
	private int getHighestWorkableBlock(Chunk c, int cx, int cz) {
		int y = 15;
		while (c.getBlockMaterial(cx, y, cz) != VanillaMaterials.GRASS) {
			y--;
			if (y == 0 || c.getBlockMaterial(cx, y, cz) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
