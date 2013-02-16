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
package org.spout.vanilla.world.generator.normal.decorator;

import org.spout.vanilla.world.generator.decorator.VariableAmountDecorator;
import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.normal.object.variableheight.CactusStackObject;

public class CactusDecorator extends VariableAmountDecorator {
	public CactusDecorator() {
		super(2, 1);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final CactusStackObject cactus = new CactusStackObject();
		cactus.setRandom(random);
		final int amount = getAmount(random);
		for (byte count = 0; count < amount; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			cactus.randomize();
			if (y != -1 && cactus.canPlaceObject(world, x, y, z)) {
				cactus.placeObject(world, x, y, z);
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = NormalGenerator.HEIGHT;
		while (world.getBlockMaterial(x, y, z) != VanillaMaterials.SAND) {
			if (--y <= 0) {
				return -1;
			}
		}
		return ++y;
	}
}
