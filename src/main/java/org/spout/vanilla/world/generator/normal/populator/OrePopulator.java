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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.spout.api.generator.Populator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.world.generator.normal.object.OreObject;
import org.spout.vanilla.world.generator.normal.object.OreObject.OreType;

public class OrePopulator extends Populator {
	private final List<OreType> oreTypes = new ArrayList<OreType>();

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		for (OreType oreType : oreTypes) {
			final OreObject ore = new OreObject(oreType);
			ore.setRandom(random);
			for (byte i = 0; i < oreType.getClusterCount(); i++) {
				final int x = chunk.getBlockX(random);
				final int y = random.nextInt(oreType.getMaxHeight() - oreType.getMinHeight())
						+ oreType.getMinHeight();
				final int z = chunk.getBlockZ(random);
				if (ore.canPlaceObject(world, x, y, z)) {
					ore.placeObject(world, x, y, z);
				}
			}
		}
	}

	public void clearOreTypes() {
		oreTypes.clear();
	}

	public void setOreTypes(OreType... types) {
		clearOreTypes();
		addOreTypes(types);
	}

	public void addOreTypes(OreType... types) {
		oreTypes.addAll(Arrays.asList(types));
	}

	public void removeOreTypes(OreType... types) {
		oreTypes.removeAll(Arrays.asList(types));
	}

	public List<OreType> getOreTypes() {
		return oreTypes;
	}
}
