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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.normal.object.HugeMushroomObject;
import org.spout.vanilla.world.generator.normal.object.HugeMushroomObject.HugeMushroomType;

public class MushroomDecorator implements Decorator {
	/*
		 * Stores the differant types of mushrooms.
		 */
	private static final List<BlockMaterial> MUSHROOMS = new ArrayList<BlockMaterial>();
	private static final List<HugeMushroomType> HUGE_MUSHROOMS = new ArrayList<HugeMushroomType>();

	static {
		MUSHROOMS.add(VanillaMaterials.RED_MUSHROOM);
		MUSHROOMS.add(VanillaMaterials.BROWN_MUSHROOM);
		HUGE_MUSHROOMS.add(HugeMushroomType.RED);
		HUGE_MUSHROOMS.add(HugeMushroomType.BROWN);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		// get some stuff we'll need for both sizes
		final Biome biome = chunk.getBiomeType(7, 7, 7);
		final World world = chunk.getWorld();
		// first up, normal mushrooms
		final byte mushroomAmount = getNumberOfMushrooms(random, biome);
		for (byte i = 0; i < mushroomAmount; i++) {
			final int worldX = chunk.getBlockX() + random.nextInt(16);
			final int worldZ = chunk.getBlockZ() + random.nextInt(16);
			final int worldY = getHighestWorkableBlock(world, worldX, worldZ);
			final BlockMaterial material = world.getBlockMaterial(worldX, worldY - 1, worldZ);
			if (material == VanillaMaterials.GRASS || material == VanillaMaterials.DIRT || material == VanillaMaterials.MYCELIUM) {
				world.setBlockMaterial(worldX, worldY, worldZ, getMushroom(random), (short) 0, world);
			}
		}
		// now we handle the huge ones
		final byte hugeMushroomAmount = getNumberOfHugeMushrooms(random, biome);
		for (byte i = 0; i < hugeMushroomAmount; i++) {
			final int worldX = chunk.getBlockX() + random.nextInt(16);
			final int worldZ = chunk.getBlockZ() + random.nextInt(16);
			final int worldY = getHighestWorkableBlock(world, worldX, worldZ);
			final HugeMushroomObject hugeMushroom = new HugeMushroomObject(random, getHugeMushroom(random));
			if (hugeMushroom.canPlaceObject(world, worldX, worldY, worldZ)) {
				hugeMushroom.placeObject(world, worldX, worldY, worldZ);
			}
		}
	}

	private int getHighestWorkableBlock(World w, int x, int z) {
		int y = 127;
		while (w.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}

	/* 
	 * Suggested numbers based on mc specs:
	 *  Mushroom biome:
	 *	  mushroom: 0 to 3
	 *	  huge mushroom: 0 to 1
	 *  Swamp biome:
	 *	  mushroom: 0 to 7
	 *	  huge mushroom: 0 to 0
	 *  All misc biomes:
	 *	  huge mushroom: 0 to 0
	 *	  mushroom: 0 to 3
	 */
	private byte getNumberOfMushrooms(Random random, Biome biome) {
		if (biome == VanillaBiomes.SWAMP) {
			return (byte) random.nextInt(7);
		} else {
			return (byte) random.nextInt(3);
		}
	}

	private byte getNumberOfHugeMushrooms(Random random, Biome biome) {
		if (biome == VanillaBiomes.MUSHROOM) {
			return (byte) random.nextInt(2);
		} else {
			return 0;
		}
	}

	private BlockMaterial getMushroom(Random random) {
		return MUSHROOMS.get(random.nextInt(MUSHROOMS.size()));
	}

	private HugeMushroomType getHugeMushroom(Random random) {
		return HUGE_MUSHROOMS.get(random.nextInt(HUGE_MUSHROOMS.size()));
	}
}
