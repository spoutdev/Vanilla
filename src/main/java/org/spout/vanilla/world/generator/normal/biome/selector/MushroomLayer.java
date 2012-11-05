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
package org.spout.vanilla.world.generator.normal.biome.selector;

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.vanilla.world.generator.biome.VanillaBiomes;
import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorElement;
import org.spout.vanilla.world.generator.biome.selector.BiomeSelectorLayer;

public class MushroomLayer implements BiomeSelectorLayer {
	private static final float MUSHROOM_SHORE_START = 0.78f;
	private static final float MUSHROOM_SHORE_END = 0.85f;
	private static final float MUSHROOM_START = MUSHROOM_SHORE_END;
	private static final float MUSHROOM_END = 1;
	private final Perlin mushroomBase = new Perlin();
	private final Turbulence mushroom = new Turbulence();

	public MushroomLayer(double scale) {
		mushroomBase.setFrequency(0.01 / scale);
		mushroomBase.setOctaveCount(1);
		mushroom.SetSourceModule(0, mushroomBase);
		mushroom.setFrequency(0.03);
		mushroom.setPower(20);
		mushroom.setRoughness(1);
	}

	@Override
	public BiomeSelectorElement pick(int x, int y, int z, long seed) {
		mushroomBase.setSeed((int) seed * 11);
		mushroom.setSeed((int) seed * 13);
		final float value = (float) mushroom.GetValue(x, 0, z);
		if (value > MUSHROOM_SHORE_START
				&& value <= MUSHROOM_SHORE_END) {
			return VanillaBiomes.MUSHROOM_SHORE;
		} else if (value > MUSHROOM_START
				&& value <= MUSHROOM_END) {
			return VanillaBiomes.MUSHROOM;
		} else {
			return VanillaBiomes.OCEAN;
		}
	}
}
