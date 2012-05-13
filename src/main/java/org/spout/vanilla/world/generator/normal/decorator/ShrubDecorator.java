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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.BiomeDecorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.ShrubObject;

public class ShrubDecorator implements BiomeDecorator {

	// How many shrub decorations per chunk
	// this number is valid for jungles only
	private static final byte AMOUNT = 20;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final ShrubObject shrub = new ShrubObject(random);
		for (byte i = 0; i < AMOUNT; i++) {
			final int x = random.nextInt(16) + chunk.getX() * 16;
			final int z = random.nextInt(16) + chunk.getZ() * 16;
			final int y = getHighestWorkableBlock(world, x, z);
			if (y == -1) {
				continue;
			}
			if (shrub.canPlaceObject(world, x, y, z)) {
				shrub.placeObject(world, x, y, z);
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		byte y = 127;
		BlockMaterial material;
		while ((material = world.getBlockMaterial(x, y, z)) == VanillaMaterials.AIR || material == VanillaMaterials.LEAVES) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
