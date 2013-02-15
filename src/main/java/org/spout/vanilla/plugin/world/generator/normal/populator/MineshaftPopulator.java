/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.plugin.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.plugin.world.generator.normal.structure.mineshaft.Mineshaft;

public class MineshaftPopulator extends Populator {
	private static final int DISTANCE = 256;
	private static final int VARIATION = 16;
	private static final int ODD = 3;
	private static final int BASE_Y = 35;
	private static final int RAND_Y = 11;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final int blockX = chunk.getBlockX();
		final int blockZ = chunk.getBlockZ();
		if (Math.abs(blockX % DISTANCE) >= Chunk.BLOCKS.SIZE
				|| Math.abs(blockZ % DISTANCE) >= Chunk.BLOCKS.SIZE) {
			return;
		}
		final World world = chunk.getWorld();
		if (random.nextInt(ODD) == 0) {
			final Mineshaft mineshaft = new Mineshaft(random);
			final int x = blockX + random.nextInt(VARIATION * 2 + 1) - VARIATION;
			final int y = random.nextInt(RAND_Y) + BASE_Y;
			final int z = blockZ + random.nextInt(VARIATION * 2 + 1) - VARIATION;
			if (mineshaft.canPlaceObject(world, x, y, z)) {
				mineshaft.placeObject(world, x, y, z);
			}
		}
	}
}
