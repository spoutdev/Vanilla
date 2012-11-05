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

public class FrozenOceanLayer implements BiomeSelectorLayer {
	private static final float FROZEN_OCEAN_START = 0.4f;
	private static final float FROZEN_OCEAN_END = 1;
	private final Perlin oceanBase = new Perlin();
	private final Turbulence ocean = new Turbulence();

	public FrozenOceanLayer(double scale) {
		oceanBase.setFrequency(0.01 / scale);
		oceanBase.setOctaveCount(1);
		ocean.SetSourceModule(0, oceanBase);
		ocean.setFrequency(0.03);
		ocean.setPower(20);
		ocean.setRoughness(1);
	}

	@Override
	public BiomeSelectorElement pick(int x, int y, int z, long seed) {
		oceanBase.setSeed((int) seed * 11);
		ocean.setSeed((int) seed * 13);
		final float value = (float) ocean.GetValue(x, 1000, z);
		if (value > FROZEN_OCEAN_START
				&& value <= FROZEN_OCEAN_END) {
			return VanillaBiomes.FROZEN_OCEAN;
		} else {
			return null;
		}
	}
}
