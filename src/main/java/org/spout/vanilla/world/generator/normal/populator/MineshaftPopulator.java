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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.math.BitSize;

import org.spout.vanilla.world.generator.structure.mineshaft.Mineshaft;

public class MineshaftPopulator extends Populator {
	private static final BitSize SPACING = new BitSize(8);
	private static final int ODD = 3;
	private static final int BASE_Y = 35;
	private static final int RAND_Y = 11;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		if (chunk.getBlockX() % SPACING.SIZE != 0 || chunk.getBlockZ() % SPACING.SIZE != 0) {
			return;
		}
		final int spacingX = chunk.getBlockX() >> SPACING.BITS;
		final int spacingZ = chunk.getBlockZ() >> SPACING.BITS;
		final World world = chunk.getWorld();
		random = WorldGeneratorUtils.getRandom(world, spacingX, 0, spacingZ, 26471);
		final Mineshaft mineshaft = new Mineshaft();
		if (random.nextInt(ODD) == 0) {
			mineshaft.setRandom(random);
			final int x = chunk.getBlockX(random);
			final int y = random.nextInt(RAND_Y) + BASE_Y;
			final int z = chunk.getBlockZ(random);
			if (mineshaft.canPlaceObject(world, x, y, z)) {
				mineshaft.placeObject(world, x, y, z);
			}
		}
	}
}
