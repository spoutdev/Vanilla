/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.world.generator.nether;

import java.util.Random;

import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.generator.biome.selector.PerBlockBiomeSelector;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.liquid.Liquid;
import org.spout.vanilla.world.generator.VanillaBiomes;
import org.spout.vanilla.world.generator.VanillaGenerator;

public class NetherGenerator extends BiomeGenerator implements VanillaGenerator {
	@Override
	public void registerBiomes() {
		setSelector(new PerBlockBiomeSelector(VanillaBiomes.NETHERRACK));
		register(VanillaBiomes.NETHERRACK);
	}

	@Override
	public String getName() {
		return "VanillaNether";
	}

	@Override
	public Point getSafeSpawn(World world) {
		final Random random = new Random();
		for (byte attempts = 0; attempts < 10; attempts++) {
			final int x = random.nextBoolean() ? -random.nextInt(16) : random.nextInt(16);
			final int z = random.nextBoolean() ? -random.nextInt(16) : random.nextInt(16);
			final int y = getHighestSolidBlock(world, x, z);
			if (y != -1) {
				return new Point(world, x, y, z);
			}
		}
		return new Point(world, 0, 80, 0);
	}

	private int getHighestSolidBlock(World world, int x, int z) {
		int y = world.getHeight() - 1;
		while (world.getBlockMaterial(x, y, z) == VanillaMaterials.AIR) {
			y--;
			if (y == 0 || world.getBlockMaterial(x, y, z) instanceof Liquid) {
				return -1;
			}
		}
		return y + 2;
	}
}
