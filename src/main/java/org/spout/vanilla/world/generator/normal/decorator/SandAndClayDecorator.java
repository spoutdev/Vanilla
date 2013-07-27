/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
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
	private int firstSandAmount = 3;
	private int secondSandAmount = 1;
	private int clayAmount = 1;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final BlockPatchObject sand = new BlockPatchObject(VanillaMaterials.SAND);
		sand.setRandom(random);
		for (byte count = 0; count < firstSandAmount; count++) {
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
		clay.setRandom(random);
		for (byte count = 0; count < clayAmount; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			clay.randomize();
			if (y != -1 && clay.canPlaceObject(world, x, y, z)) {
				clay.placeObject(world, x, y, z);
			}
		}
		for (byte count = 0; count < secondSandAmount; count++) {
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
		int y = world.getSurfaceHeight(x, z);
		BlockMaterial material;
		while ((material = world.getBlockMaterial(x, y, z)) == VanillaMaterials.ICE
				|| !(material instanceof Solid)) {
			if (--y <= 0) {
				return -1;
			}
		}
		return ++y;
	}

	public void setFirstSandAmount(int firstSandAmount) {
		this.firstSandAmount = firstSandAmount;
	}

	public void setSecondSandAmount(int secondSandAmount) {
		this.secondSandAmount = secondSandAmount;
	}

	public void setClayAmount(int clayAmount) {
		this.clayAmount = clayAmount;
	}
}
