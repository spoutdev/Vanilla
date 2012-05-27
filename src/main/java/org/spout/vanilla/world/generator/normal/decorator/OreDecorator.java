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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.OreObject;

public class OreDecorator implements Decorator {
	public static OreObject[] objects;

	static {
		objects = new OreObject[7];
		objects[0] = new OreObject(VanillaMaterials.GRAVEL, 10, 32, 128);
		objects[1] = new OreObject(VanillaMaterials.COAL_ORE, 20, 16, 128);
		objects[2] = new OreObject(VanillaMaterials.IRON_ORE, 20, 8, 64);
		objects[3] = new OreObject(VanillaMaterials.GOLD_ORE, 2, 8, 32);
		objects[4] = new OreObject(VanillaMaterials.REDSTONE_ORE, 8, 7, 16);
		objects[5] = new OreObject(VanillaMaterials.DIAMOND_ORE, 1, 7, 16);
		objects[6] = new OreObject(VanillaMaterials.LAPIS_LAZULI_ORE, 1, 6, 32);
	}

	@Override
	public void populate(Chunk source, Random random) {
		World world = source.getWorld();
		for (OreObject object : objects) {
			for (int i = 0; i < object.getCount(); i++) {
				object.placeObject(world, source.getBlockX() + random.nextInt(16), random.nextInt(object.getMaxHeight()), source.getBlockZ() + random.nextInt(16), random);
			}
		}
	}
}
