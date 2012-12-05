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
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.world.generator.normal.object.PondObject;
import org.spout.vanilla.world.generator.normal.object.PondObject.PondType;

public class PondPopulator extends Populator {
	private static final byte WATER_ODD = 4;
	private static final byte LAVA_ODD = 8;
	private static final byte LAVA_SURFACE_ODD = 10;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		if (random.nextInt(WATER_ODD) == 0) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = random.nextInt(128);
			final PondObject pond = new PondObject(random, PondType.WATER);
			if (pond.canPlaceObject(world, x, y, z)) {
				pond.placeObject(world, x, y, z);
			}
		}
		if (random.nextInt(LAVA_ODD) == 0) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = random.nextInt(120) + 8;
			if (y >= 63 && random.nextInt(LAVA_SURFACE_ODD) != 0) {
				return;
			}
			final PondObject pond = new PondObject(random, PondType.LAVA);
			if (pond.canPlaceObject(world, x, y, z)) {
				pond.placeObject(world, x, y, z);
			}
		}
	}
}
