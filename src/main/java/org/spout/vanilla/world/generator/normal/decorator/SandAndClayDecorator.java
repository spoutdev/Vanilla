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

import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.world.generator.normal.object.BlockPatchObject;

public class SandAndClayDecorator extends Decorator {
	private static final byte FIRST_SAND_ROUND = 3;
	private static final byte SECOND_SAND_ROUND = 1;
	private static final byte CLAY_ROUND = 1;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final BlockPatchObject sand = new BlockPatchObject(VanillaMaterials.SAND);
		for (byte count = 0; count < FIRST_SAND_ROUND; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			sand.randomize();
			if (y != -1 && sand.canPlaceObject(world, x, y, z)) {
				sand.placeObject(world, x, y, z);
			}
		}
		final BlockPatchObject clay = new BlockPatchObject(VanillaMaterials.CLAY_BLOCK);
		clay.setHeightRadius((byte) 1);
		clay.getOverridableMaterials().clear();
		clay.getOverridableMaterials().add(VanillaMaterials.DIRT);
		sand.setRandom(random);
		clay.setRandom(random);
		for (byte count = 0; count < CLAY_ROUND; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			clay.randomize();
			if (y != -1 && clay.canPlaceObject(world, x, y, z)) {
				clay.placeObject(world, x, y, z);
			}
		}
		for (byte count = 0; count < SECOND_SAND_ROUND; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			sand.randomize();
			if (y != -1 && sand.canPlaceObject(world, x, y, z)) {
				sand.placeObject(world, x, y, z);
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = world.getHeight();
		BlockMaterial material;
		while ((material = world.getBlockMaterial(x, y, z)) == VanillaMaterials.ICE
				|| !(material instanceof Solid)) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
