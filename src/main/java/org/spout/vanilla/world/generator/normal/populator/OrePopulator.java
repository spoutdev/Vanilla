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

import org.spout.vanilla.world.generator.normal.object.OreObject;
import org.spout.vanilla.world.generator.normal.object.OreObject.OreType;

public class OrePopulator extends Populator {
	private static final OreObject[] ORES;

	static {
		ORES = new OreObject[]{
				new OreObject(OreType.DIRT),
				new OreObject(OreType.GRAVEL),
				new OreObject(OreType.COAL),
				new OreObject(OreType.IRON),
				new OreObject(OreType.REDSTONE),
				new OreObject(OreType.LAPIS_LAZULI),
				new OreObject(OreType.GOLD),
				new OreObject(OreType.DIAMOND)
		};
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		for (OreObject object : ORES) {
			object.setRandom(random);
			for (byte i = 0; i < object.getAmount(); i++) {
				final int x = chunk.getBlockX(random);
				final int y = random.nextInt(object.getMaxHeight());
				final int z = chunk.getBlockZ(random);
				object.placeObject(world, x, y, z);
			}
		}
	}
}
