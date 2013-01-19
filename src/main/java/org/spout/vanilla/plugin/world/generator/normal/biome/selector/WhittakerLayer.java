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
import net.royawesome.jlibnoise.module.modifier.ScaleBias;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.biome.selector.BiomeSelectorLayer;
import org.spout.api.generator.biome.selector.LayeredBiomeSelectorElement;
import org.spout.api.math.Vector2;

public class WhittakerLayer implements BiomeSelectorLayer {
	private final Perlin temperaturePerlin = new Perlin();
	private final Clamp temperatureClamp = new Clamp();
	private final ScaleBias temperature = new ScaleBias();
	private final Perlin humidityPerlin = new Perlin();
	private final Clamp humidityClamp = new Clamp();
	private final ScaleBias humidity = new ScaleBias();
	private double minTemperature = -20;
	private double maxTemperature = 30;
	private double minHumidity = 0;
	private double maxHumidity = 400;
	private final int uniquenessValue;
	private final List<WhittakerElement> elements = new ArrayList<WhittakerElement>();

	public WhittakerLayer(int uniquenessValue) {
		this.uniquenessValue = uniquenessValue;
		temperatureClamp.SetSourceModule(0, temperaturePerlin);
		temperatureClamp.setLowerBound(-1);
		temperatureClamp.setUpperBound(1);
		temperature.SetSourceModule(0, temperatureClamp);
		temperature.setScale(0.5);
		temperature.setBias(0.5);
		humidityClamp.SetSourceModule(0, humidityPerlin);
		humidityClamp.setLowerBound(-1);
		humidityClamp.setUpperBound(1);
		humidity.SetSourceModule(0, humidityClamp);
		humidity.setScale(0.5);
		humidity.setBias(0.5);
	}

	@Override
	public LayeredBiomeSelectorElement pick(int x, int y, int z, long seed) {
		temperaturePerlin.setSeed((int) seed * uniquenessValue * uniquenessValue);
		humidityPerlin.setSeed((int) seed * uniquenessValue);
		final double temperatureValue = temperature.GetValue(x, y, z);
		final double humidityValue = humidity.GetValue(x, y, z);
		return findClosest(humidityValue * temperatureValue * (minTemperature - maxTemperature) + maxTemperature,
				(1 - humidityValue) * (maxHumidity - minHumidity) + minHumidity);
	}

	private LayeredBiomeSelectorElement findClosest(double temperature, double humidity) {
		final Vector2 position = new Vector2(temperature, humidity);
		WhittakerElement closestElement = null;
		float smallestDistance = 0;
		for (WhittakerElement element : elements) {
			final float distance = element.getPosition().subtract(position).lengthSquared();
			if (closestElement == null) {
				closestElement = element;
				smallestDistance = distance;
			} else {
				if (distance <= smallestDistance) {
					closestElement = element;
					smallestDistance = distance;
				}
			}
		}
		return closestElement == null ? null : closestElement.getElement();
	}

	public WhittakerLayer addElement(LayeredBiomeSelectorElement element, float temperature, float humidity) {
		return addElement(new WhittakerElement(element, temperature, humidity));
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

	public WhittakerLayer setMinTemperature(double minTemperature) {
		this.minTemperature = minTemperature;
		return this;
	}

	public WhittakerLayer setMaxTemperature(double maxTemperature) {
		this.maxTemperature = maxTemperature;
		return this;
	}

	public WhittakerLayer setMinHumidity(double minHumidity) {
		this.minHumidity = minHumidity;
		return this;
	}

	public WhittakerLayer setMaxHumidity(double maxHumidity) {
		this.maxHumidity = maxHumidity;
		return this;
	}

	public static class WhittakerElement {
		private final LayeredBiomeSelectorElement element;
		private float temperature;
		private float humidity;

		public WhittakerElement(LayeredBiomeSelectorElement element) {
			this.element = element;
		}

		public WhittakerElement(LayeredBiomeSelectorElement element, float temperature, float humidity) {
			this(element);
			this.temperature = temperature;
			this.humidity = humidity;
		}

		public LayeredBiomeSelectorElement getElement() {
			return element;
		}

		public float getTemperature() {
			return temperature;
		}

		public float getHumidity() {
			return humidity;
		}

		public Vector2 getPosition() {
			return new Vector2(temperature, humidity);
		}
	}
}
