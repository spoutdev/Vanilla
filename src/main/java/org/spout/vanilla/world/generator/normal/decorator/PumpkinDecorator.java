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

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.decorator.VariableAmountDecorator;

/**
 * Decorator that decorates a biome with pumpkins.
 */
public class PumpkinDecorator extends VariableAmountDecorator {
	private int odd = 120;

	public PumpkinDecorator() {
		super(1, 7);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		if (random.nextInt(odd) != 0) {
			return;
		}
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX(random);
		final int z = chunk.getBlockZ(random);
		for (int amount = getAmount(random); amount >= 0; amount--) {
			final int xx = x - 7 + random.nextInt(15);
			final int zz = z - 7 + random.nextInt(15);
			final int yy = getHighestWorkableBlock(world, xx, zz);
			if (yy != -1 && world.getBlockMaterial(xx, yy, zz) == VanillaMaterials.AIR) {
				world.setBlockMaterial(xx, yy, zz, VanillaMaterials.PUMPKIN_BLOCK, (short) random.nextInt(4), null);
			}
		}
	}

	private int getHighestWorkableBlock(World w, int x, int z) {
		int y = w.getSurfaceHeight(x, z);
		while (w.getBlockMaterial(x, y, z) != VanillaMaterials.GRASS) {
			if (--y <= 0) {
				return -1;
			}
		}
		return ++y;
	}

	public void setOdd(int odd) {
		this.odd = odd;
	}
}
