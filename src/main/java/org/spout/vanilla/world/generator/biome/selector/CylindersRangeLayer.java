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
package org.spout.vanilla.world.generator.biome.selector;

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Cylinders;

public class CylindersRangeLayer extends NoiseRangeLayer implements Cloneable {
	private final Cylinders cylinders = new Cylinders();
	private final Turbulence turbulence = new Turbulence();
	private final int uniquenessValue;

	public CylindersRangeLayer(int uniquenessValue) {
		this.uniquenessValue = uniquenessValue;
		turbulence.SetSourceModule(0, cylinders);
	}

	@Override
	protected float getNoiseValue(int x, int y, int z, int seed) {
		turbulence.setSeed(seed * uniquenessValue);
		return (float) turbulence.GetValue(x, y, z);
	}

	public CylindersRangeLayer setCylindersFrequency(double frequency) {
		cylinders.setFrequency(frequency);
		return this;
	}

	public CylindersRangeLayer setTurbulenceFrequency(double frequency) {
		turbulence.setFrequency(frequency);
		return this;
	}

	public CylindersRangeLayer setTurbulencePower(double power) {
		turbulence.setPower(power);
		return this;
	}

	public CylindersRangeLayer setTurbulenceRoughness(int roughness) {
		turbulence.setRoughness(roughness);
		return this;
	}

	@Override
	public CylindersRangeLayer clone() {
		return (CylindersRangeLayer) new CylindersRangeLayer(uniquenessValue).
				setCylindersFrequency(cylinders.getFrequency()).
				setTurbulenceFrequency(turbulence.getFrequency()).
				setTurbulencePower(turbulence.getPower()).
				setTurbulenceRoughness(turbulence.getRoughnessCount()).
				addElements(ranges);
	}
}
