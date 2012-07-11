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

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.WellObject;

/**
 * Decorator that decorates a biome with a well structure.
 */
public class WellDecorator extends Decorator {
	// a well object for generation
	// the object isn't random, so we can use a static instance
	private static final WellObject WELL = new WellObject();
	// generation odd, 'ODD' chunk per chunk
	private static final short ODD = 1000;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		if (random.nextInt(ODD) != 0) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX(random);
		final int z = chunk.getBlockZ(random);
		final int y = getHighestWorkableBlock(world, x, z);
		if (y == -1) {
			return;
		}
		if (WELL.canPlaceObject(world, x, y, z)) {
			WELL.placeObject(world, x, y, z);
		}
	}

	private int getHighestWorkableBlock(World w, int x, int z) {
		int y = w.getHeight();
		while (w.getBlockMaterial(x, y, z) != VanillaMaterials.SAND) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
