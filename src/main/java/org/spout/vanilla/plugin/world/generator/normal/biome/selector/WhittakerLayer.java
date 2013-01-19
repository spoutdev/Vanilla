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
package org.spout.vanilla.plugin.world.generator.normal.biome.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.selector.BiomeSelectorLayer;
import org.spout.api.generator.biome.selector.LayeredBiomeSelectorElement;

public class WhittakerLayer implements BiomeSelectorLayer {
	private final Perlin humidityPerlin = new Perlin();
	private final Clamp humidity = new Clamp();
	private final Perlin temperaturePerlin = new Perlin();
	private final Clamp temperature = new Clamp();
	private final int uniquenessValue;
	private final List<WhittakerElement> elements = new ArrayList<WhittakerElement>();

	public WhittakerLayer(int uniquenessValue) {
		this.uniquenessValue = uniquenessValue;
		humidity.SetSourceModule(0, humidityPerlin);
		humidity.setLowerBound(-1);
		humidity.setUpperBound(1);
		temperature.SetSourceModule(0, temperaturePerlin);
		temperature.setLowerBound(-1);
		temperature.setUpperBound(1);
	}

	@Override
	public LayeredBiomeSelectorElement pick(int x, int y, int z, long seed) {
		humidityPerlin.setSeed((int) seed * uniquenessValue);
		temperaturePerlin.setSeed((int) seed * uniquenessValue * uniquenessValue);
		final double humidityValue = humidity.GetValue(x, y, z);
		final double temperatureValue = temperature.GetValue(x, y, z);
		for (WhittakerElement element : elements) {
			if (humidityValue >= element.getMinHumidity() && humidityValue <= element.getMaxHumidity()
					&& temperatureValue >= element.getMinTemperature() && temperatureValue <= element.getMaxTemperature()) {
				return element.getElement();
			}
		}
		return null;
	}

	public WhittakerLayer addElement(LayeredBiomeSelectorElement element,
									 float minHumidity, float maxHumidity, float minTemperature, float maxTemperature) {
		return addElement(new WhittakerElement(element, minHumidity, maxHumidity,
				minTemperature, maxTemperature));
	}

	public WhittakerLayer addElement(WhittakerElement element) {
		this.elements.add(element);
		return this;
	}

	public WhittakerLayer addElements(WhittakerElement... elements) {
		return addElements(Arrays.asList(elements));
	}

	public WhittakerLayer addElements(Collection<WhittakerElement> elements) {
		this.elements.addAll(elements);
		return this;
	}

	public WhittakerLayer setHumidityFrequency(double frequency) {
		humidityPerlin.setFrequency(frequency);
		return this;
	}

	public WhittakerLayer setHumidityLacunarity(double lacunarity) {
		humidityPerlin.setLacunarity(lacunarity);
		return this;
	}

	public WhittakerLayer setHumidityNoiseQuality(NoiseQuality quality) {
		humidityPerlin.setNoiseQuality(quality);
		return this;
	}

	public WhittakerLayer setHumidityOctaveCount(int octaveCount) {
		humidityPerlin.setOctaveCount(octaveCount);
		return this;
	}

	public WhittakerLayer setHumidityPersistence(double persistence) {
		humidityPerlin.setPersistence(persistence);
		return this;
	}

	public WhittakerLayer setTemperatureFrequency(double frequency) {
		temperaturePerlin.setFrequency(frequency);
		return this;
	}

	public WhittakerLayer setTemperatureLacunarity(double lacunarity) {
		temperaturePerlin.setLacunarity(lacunarity);
		return this;
	}

	public WhittakerLayer setTemperatureNoiseQuality(NoiseQuality quality) {
		temperaturePerlin.setNoiseQuality(quality);
		return this;
	}

	public WhittakerLayer setTemperatureOctaveCount(int octaveCount) {
		temperaturePerlin.setOctaveCount(octaveCount);
		return this;
	}

	public WhittakerLayer setTemperaturePersistence(double persistence) {
		temperaturePerlin.setPersistence(persistence);
		return this;
	}

	public static class WhittakerElement {
		private final LayeredBiomeSelectorElement element;
		private float minHumidity;
		private float maxHumidity;
		private float minTemperature;
		private float maxTemperature;

		public WhittakerElement(LayeredBiomeSelectorElement element) {
			this.element = element;
		}

		public WhittakerElement(LayeredBiomeSelectorElement element,
								float minHumidity, float maxHumidity,
								float minTemperature, float maxTemperature) {
			this(element);
			this.minHumidity = minHumidity;
			this.maxHumidity = maxHumidity;
			this.minTemperature = minTemperature;
			this.maxTemperature = maxTemperature;
		}

		public WhittakerElement setHumidity(float min, float max) {
			this.minHumidity = min;
			this.maxHumidity = max;
			return this;
		}

		public WhittakerElement setTemperature(float min, float max) {
			this.minTemperature = min;
			this.maxTemperature = max;
			return this;
		}

		public LayeredBiomeSelectorElement getElement() {
			return element;
		}

		public float getMinHumidity() {
			return minHumidity;
		}

		public float getMaxHumidity() {
			return maxHumidity;
		}

		public float getMinTemperature() {
			return minTemperature;
		}

		public float getMaxTemperature() {
			return maxTemperature;
		}
	}
}
