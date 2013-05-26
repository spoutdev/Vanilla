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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.world.generator.biome.VanillaBiomes;
import org.spout.vanilla.world.generator.normal.object.PondObject;

public class PondPopulator extends Populator {
	private int waterOdd = 4;
	private int lavaOdd = 8;
	private int lavaSurfaceOdd = 10;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		if (random.nextInt(waterOdd) == 0) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = random.nextInt(128);
			if (!LogicUtil.equalsAny(world.getBiome(x, y, z), VanillaBiomes.DESERT, VanillaBiomes.DESERT_HILLS)) {
				final PondObject pond = new PondObject(random, PondObject.PondType.WATER);
				if (pond.canPlaceObject(world, x, y, z)) {
					pond.placeObject(world, x, y, z);
				}
			}
		}
		if (random.nextInt(lavaOdd) == 0) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = random.nextInt(120) + 8;
			if (y >= 63 && random.nextInt(lavaSurfaceOdd) != 0) {
				return;
			}
			final PondObject pond = new PondObject(random, PondObject.PondType.LAVA);
			if (pond.canPlaceObject(world, x, y, z)) {
				pond.placeObject(world, x, y, z);
			}
		}
	}

	public void setWaterOdd(int waterOdd) {
		this.waterOdd = waterOdd;
	}

	public void setLavaOdd(int lavaOdd) {
		this.lavaOdd = lavaOdd;
	}

	public void setLavaSurfaceOdd(int lavaSurfaceOdd) {
		this.lavaSurfaceOdd = lavaSurfaceOdd;
	}
}
