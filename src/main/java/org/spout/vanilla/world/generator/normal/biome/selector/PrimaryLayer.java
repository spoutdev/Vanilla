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

public class PrimaryLayer implements BiomeSelectorLayer {
	private static final float MUSHROOM_START = -1;
	private static final float MUSHROOM_END = -0.3f;
	private static final float SHORE_START = -0.05f;
	private static final float SHORE_END = 0;
	private static final float LAND_START = SHORE_END;
	private static final float LAND_END = 0.675f;
	private static final float SMALL_MOUNTAINS_START = LAND_END;
	private static final float SMALL_MOUNTAINS_END = 0.71f;
	private static final float MOUNTAINS_START = SMALL_MOUNTAINS_END;
	private static final float MOUNTAINS_END = 1;
	private final Perlin primaryBase = new Perlin();
	private final Turbulence base = new Turbulence();

	public PrimaryLayer(double scale) {
		primaryBase.setFrequency(0.007 / scale);
		primaryBase.setOctaveCount(1);
		base.SetSourceModule(0, primaryBase);
		base.setFrequency(0.02);
		base.setPower(20);
		base.setRoughness(1);
	}

	@Override
	public BiomeSelectorElement pick(int x, int y, int z, long seed) {
		primaryBase.setSeed((int) seed);
		base.setSeed((int) seed * 2);
		final float value = (float) base.GetValue(x, 0, z);
		if (value > MUSHROOM_START
				&& value <= MUSHROOM_END) {
			return NormalBiomeSelector.MUSHROOM;
		} else if (value > SHORE_START
				&& value <= SHORE_END) {
			return NormalBiomeSelector.SHORE;
		} else if (value > LAND_START
				&& value <= LAND_END) {
			return NormalBiomeSelector.LAND;
		} else if (value > SMALL_MOUNTAINS_START
				&& value <= SMALL_MOUNTAINS_END) {
			return NormalBiomeSelector.SMALL_MOUNTAINS;
		} else if (value > MOUNTAINS_START
				&& value <= MOUNTAINS_END) {
			return NormalBiomeSelector.MOUNTAINS;
		} else {
			return VanillaBiomes.OCEAN;
		}
	}
}
