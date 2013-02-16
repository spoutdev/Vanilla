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

import java.util.Random;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;

import org.spout.vanilla.world.generator.normal.object.variableheight.tree.TreeObject;

public class TreeDecorator extends Decorator {
	private TreeWGOFactory factory;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final Biome decorating = chunk.getBiome(7, 7, 7);
		final byte amount = factory.amount(random);
		for (byte count = 0; count < amount; count++) {
			final TreeObject tree = factory.make(random);
			tree.setRandom(random);
			tree.randomize();
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			if (decorating != world.getBiome(x, 64, z)) {
				continue;
			}
			final int y = getHighestWorkableBlock(world, x, z);
			if (y == -1) {
				continue;
			}
			if (tree.canPlaceObject(world, x, y, z)) {
				tree.placeObject(world, x, y, z);
			}
		}
	}

	private int getHighestWorkableBlock(World w, int x, int z) {
		int y = NormalGenerator.HEIGHT;
		while (!w.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.DIRT, VanillaMaterials.GRASS)) {
			if (--y <= 0) {
				return -1;
			}
		}
		return ++y;
	}

	public void setFactory(TreeWGOFactory factory) {
		this.factory = factory;
	}

	public static interface TreeWGOFactory {
		public TreeObject make(Random random);

		public byte amount(Random random);
	}
}
