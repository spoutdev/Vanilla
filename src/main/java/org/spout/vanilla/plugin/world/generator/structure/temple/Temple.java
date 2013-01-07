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
package org.spout.vanilla.plugin.world.generator.structure.temple;

import org.spout.api.generator.biome.Biome;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;

import org.spout.vanilla.plugin.world.generator.biome.VanillaBiomes;
import org.spout.vanilla.plugin.world.generator.structure.Structure;

public class Temple extends Structure {
	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return y >= 64;
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final Biome biome = w.getBiome(x, y, z);
		if (biome == VanillaBiomes.DESERT || biome == VanillaBiomes.DESERT_HILLS) {
			final DesertTemple desertTemple = new DesertTemple(this);
			desertTemple.setPosition(new Point(w, x, y, z));
			desertTemple.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
			if (desertTemple.canPlace()) {
				desertTemple.place();
			}
		} else if (biome == VanillaBiomes.JUNGLE || biome == VanillaBiomes.JUNGLE_HILLS) {
			final JungleTemple jungleTemple = new JungleTemple(this);
			jungleTemple.setPosition(new Point(w, x, y, z));
			jungleTemple.setRotation(new Quaternion(random.nextInt(4) * 90, 0, 1, 0));
			if (jungleTemple.canPlace()) {
				jungleTemple.place();
			}
		}
	}
}
