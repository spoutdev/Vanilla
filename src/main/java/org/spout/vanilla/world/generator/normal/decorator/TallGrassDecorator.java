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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.plant.TallGrass;

public class TallGrassDecorator extends Decorator {
	private final TallGrassFactory factory;
	// Control how much grass to place
	private final byte amount;

	public TallGrassDecorator(TallGrassFactory factory) {
		this(factory, (byte) 1);
	}

	public TallGrassDecorator(TallGrassFactory factory, byte amount) {
		this.factory = factory;
		this.amount = amount;
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		for (byte count = 0; count < amount; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			for (byte size = 30; size > 0; size--) {
				final int xx = x - 7 + random.nextInt(15);
				final int zz = z - 7 + random.nextInt(15);
				final int yy = getHighestWorkableBlock(world, xx, zz);
				if (yy != -1 && world.getBlockMaterial(xx, yy, zz) == VanillaMaterials.AIR
						&& canTallGrassStay(world.getBlock(xx, yy, zz, world))) {
					final TallGrass grass = factory.make(random);
					world.setBlockMaterial(xx, yy, zz, grass, grass.getData(), world);
				}
			}
		}
	}

	// TODO: this needs to be added to tall grass itself (valid for flowers too)
	private boolean canTallGrassStay(Block block) {
		return (block.getLight() > 7 || block.isAtSurface())
				&& block.translate(BlockFace.BOTTOM).getMaterial().isMaterial(VanillaMaterials.GRASS, VanillaMaterials.DIRT, VanillaMaterials.FARMLAND);
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = world.getHeight();
		while (world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.AIR, VanillaMaterials.LEAVES)) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}

	public static interface TallGrassFactory {
		public TallGrass make(Random random);
	}
}
