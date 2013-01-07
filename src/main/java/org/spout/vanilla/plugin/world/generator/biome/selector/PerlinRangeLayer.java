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
package org.spout.vanilla.plugin.world.generator.biome.selector;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

public class PerlinRangeLayer extends NoiseRangeLayer implements Cloneable {
	private final Perlin perlin = new Perlin();
	private final Turbulence turbulence = new Turbulence();
	private final int uniquenessValue;

	public PerlinRangeLayer(int uniquenessValue) {
		this.uniquenessValue = uniquenessValue;
		final Clamp clamp = new Clamp();
		clamp.setLowerBound(-1);
		clamp.setUpperBound(1);
		clamp.SetSourceModule(0, perlin);
		turbulence.SetSourceModule(0, clamp);
	}

	@Override
	protected float getNoiseValue(int x, int y, int z, int seed) {
		perlin.setSeed(seed * uniquenessValue);
		turbulence.setSeed(seed * uniquenessValue * uniquenessValue);
		return (float) turbulence.GetValue(x, y, z);
	}

	public PerlinRangeLayer setPerlinFrequency(double frequency) {
		perlin.setFrequency(frequency);
		return this;
	}

	public PerlinRangeLayer setPerlinLacunarity(double lacunarity) {
		perlin.setLacunarity(lacunarity);
		return this;
	}

	public PerlinRangeLayer setPerlinNoiseQuality(NoiseQuality quality) {
		perlin.setNoiseQuality(quality);
		return this;
	}

	public PerlinRangeLayer setPerlinOctaveCount(int octaveCount) {
		perlin.setOctaveCount(octaveCount);
		return this;
	}

	public PerlinRangeLayer setPerlinPersistence(double persistence) {
		perlin.setPersistence(persistence);
		return this;
	}

	public PerlinRangeLayer setTurbulenceFrequency(double frequency) {
		turbulence.setFrequency(frequency);
		return this;
	}

	public PerlinRangeLayer setTurbulencePower(double power) {
		turbulence.setPower(power);
		return this;
	}

	public PerlinRangeLayer setTurbulenceRoughness(int roughness) {
		turbulence.setRoughness(roughness);
		return this;
	}

	@Override
	public PerlinRangeLayer clone() {
		return (PerlinRangeLayer) new PerlinRangeLayer(uniquenessValue).
				setPerlinFrequency(perlin.getFrequency()).
				setPerlinLacunarity(perlin.getLacunarity()).
				setPerlinNoiseQuality(perlin.getNoiseQuality()).
				setPerlinOctaveCount(perlin.getOctaveCount()).
				setPerlinPersistence(perlin.getPersistence()).
				setTurbulenceFrequency(turbulence.getFrequency()).
				setTurbulencePower(turbulence.getPower()).
				setTurbulenceRoughness(turbulence.getRoughnessCount()).
				addElements(ranges);
	}
}
