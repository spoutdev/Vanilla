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
package org.spout.vanilla.plugin.world.generator.nether.decorator;

import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.nether.NetherGenerator;

public class FireDecorator extends Decorator {
	private static final byte BASE_AMOUNT = 1;
	private static final byte RAND_AMOUNT = 2;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		for (byte amount = (byte) (random.nextInt(RAND_AMOUNT) + BASE_AMOUNT); amount > 0; amount--) {
			final int x = chunk.getBlockX(random);
			final int y = random.nextInt(NetherGenerator.HEIGHT);
			final int z = chunk.getBlockZ(random);
			for (byte size = 8; size > 0; size--) {
				final int xx = x - 7 + random.nextInt(15);
				final int zz = z - 7 + random.nextInt(15);
				final int yy = getHighestWorkableBlock(world, xx, y, zz);
				if (yy != -1 && world.getBlockMaterial(xx, yy, zz).isMaterial(VanillaMaterials.AIR)) {
					world.setBlockMaterial(xx, yy, zz, VanillaMaterials.FIRE, (short) 0, null);
				}
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int y, int z) {
		while (!world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.NETHERRACK)) {
			y--;
			if (y <= 0) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
