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
package org.spout.vanilla.world.selector;

import net.royawesome.jlibnoise.module.combiner.Select;
import net.royawesome.jlibnoise.module.modifier.Clamp;
import net.royawesome.jlibnoise.module.modifier.ScaleBias;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Billow;
import net.royawesome.jlibnoise.module.source.Const;
import net.royawesome.jlibnoise.module.source.Perlin;
import net.royawesome.jlibnoise.module.source.RidgedMulti;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeSelector;

import org.spout.vanilla.world.generator.VanillaBiomes;

public class WhittakerBiomeSelector extends BiomeSelector {
	private final double scale;
	private final Perlin temperature, rainfall;
	private final RidgedMulti mountain;
	private final Billow continentSelector;
	private final Billow hills;
	private final Clamp finalRainfall;
	private final Clamp finalTemp;
	private final Select oceanBeachDivide, finalElevationNoise;

	/**
	 * Selects biomes based on Whittaker's humidity-temperature diagram.
	 */
	public WhittakerBiomeSelector(double scale, double biomeScale, double oceanBoundary, double beachBoundary, boolean hasMountains) {
		this.scale = scale;
		//Creates the noise for rainfall distribution
		rainfall = new Perlin();
		rainfall.setFrequency(0.4 / biomeScale);
		rainfall.setOctaveCount(4);
		final ScaleBias rainfallModifier = new ScaleBias();
		rainfallModifier.SetSourceModule(0, rainfall);
		rainfallModifier.setScale(0.5);
		rainfallModifier.setBias(0.5);
		final Turbulence rainfallTurbulence = new Turbulence();
		rainfallTurbulence.SetSourceModule(0, rainfallModifier);
		rainfallTurbulence.setFrequency(0.4);
		rainfallTurbulence.setPower(0.6);
		finalRainfall = new Clamp();
		finalRainfall.SetSourceModule(0, rainfallTurbulence);
		finalRainfall.setLowerBound(0);
		finalRainfall.setUpperBound(1.0);
		//Creates the noise module for temperature distribution
		temperature = new Perlin();
		temperature.setFrequency(0.4 / biomeScale);
		temperature.setOctaveCount(4);
		final ScaleBias tempModifier = new ScaleBias();
		tempModifier.SetSourceModule(0, temperature);
		tempModifier.setScale(0.5);
		tempModifier.setBias(0.5);
		final Turbulence tempTurbulence = new Turbulence();
		tempTurbulence.SetSourceModule(0, tempModifier);
		tempTurbulence.setFrequency(0.4);
		tempTurbulence.setPower(0.5);
		finalTemp = new Clamp();
		finalTemp.SetSourceModule(0, tempTurbulence);
		finalTemp.setLowerBound(0);
		finalTemp.setUpperBound(1.0);
		//Creates the noise modules for the elevation
		final Const uniformOcean = new Const();
		uniformOcean.setValue(-1);
		final Const shoreline = new Const();
		shoreline.setValue(0.1);
		mountain = new RidgedMulti();
		mountain.setFrequency(0.3);
		mountain.setLacunarity(3.5);
		mountain.setOctaveCount(4);
		final ScaleBias mountainModifier = new ScaleBias();
		mountainModifier.SetSourceModule(0, mountain);
		mountainModifier.setBias(0.5);
		mountainModifier.setScale(0.5);
		hills = new Billow();
		hills.setFrequency(0.45);
		hills.setPersistence(0.5);
		hills.setLacunarity(2.5);
		hills.setOctaveCount(6);
		final ScaleBias hillsModifier = new ScaleBias();
		hillsModifier.SetSourceModule(0, hills);
		hillsModifier.setBias(0.5);
		hillsModifier.setScale(0.5);
		final Clamp hillsClamp = new Clamp();
		hillsClamp.SetSourceModule(0, hillsModifier);
		hillsClamp.setLowerBound(0);
		hillsClamp.setUpperBound(0.65);
		//Puts the elevation noises together
		final Clamp elevationClamp = new Clamp();
		if (hasMountains) {
			final Select mountainRangeSelector = new Select();
			mountainRangeSelector.SetSourceModule(1, mountainModifier);
			mountainRangeSelector.SetSourceModule(0, hillsClamp);
			mountainRangeSelector.setControlModule(mountainModifier);
			mountainRangeSelector.setBounds(100, 0.75);
			mountainRangeSelector.setEdgeFalloff(0.4);
			elevationClamp.SetSourceModule(0, mountainRangeSelector);
			elevationClamp.setLowerBound(0.11); //just more than for beach biomes
			elevationClamp.setUpperBound(1.0);
		} else {
			elevationClamp.SetSourceModule(0, hillsClamp);
		}
		//Creates the noise module for separating continents & oceans
		continentSelector = new Billow();
		continentSelector.setFrequency(0.2);
		continentSelector.setOctaveCount(10);
		final ScaleBias continentSelectorModifier = new ScaleBias();
		continentSelectorModifier.SetSourceModule(0, continentSelector);
		continentSelectorModifier.setScale(0.5);
		continentSelectorModifier.setBias(0.5);
		//Divides the ocean and continents, starting with the beach level
		oceanBeachDivide = new Select();
		oceanBeachDivide.SetSourceModule(0, uniformOcean);
		oceanBeachDivide.SetSourceModule(1, shoreline);
		oceanBeachDivide.setControlModule(continentSelectorModifier);
		oceanBeachDivide.setBounds(100, oceanBoundary);
		//Creates the final elevation noise for biome selection
		finalElevationNoise = new Select();
		finalElevationNoise.SetSourceModule(0, oceanBeachDivide);
		finalElevationNoise.SetSourceModule(1, elevationClamp);
		finalElevationNoise.setControlModule(continentSelectorModifier);
		finalElevationNoise.setBounds(100, oceanBoundary + beachBoundary);
	}

	@Override
	public Biome pickBiome(int x, int y, int z, long seed) {
		continentSelector.setSeed((int) seed);
		rainfall.setSeed((int) seed * 2);
		temperature.setSeed((int) seed * 7);
		mountain.setSeed((int) seed * 5);
		hills.setSeed((int) seed * 13);

		final double elevation = getElevation(x, z);
		final double rain = getAvgRainfall(x, z);
		final double temp = getAvgTemperature(x, z);

		if (elevation > 0.8) {
			return VanillaBiomes.MOUNTAINS; //mountains
		} else if (elevation > 0.65) {
			return VanillaBiomes.SMALL_MOUNTAINS; //small mountains
		} else if (elevation == 0.1) {
			if (rain > 0.6) {
				return VanillaBiomes.SWAMP; //swamp
			} else {
				return VanillaBiomes.BEACH; //beach
			}
		} else if (elevation == -1) {
			return VanillaBiomes.OCEAN; //ocean
		} else {
			if (temp < 0.2) {
				if (rain > 0.6) {
					return VanillaBiomes.TAIGA; //taiga
				} else {
					return VanillaBiomes.TUNDRA; //tundra
				}
			} else if (temp < 0.4) {
				if (rain > 0.6) {
					return VanillaBiomes.FOREST; //forest
				} else {
					return VanillaBiomes.TAIGA; //taiga
				}
			} else if (temp < 0.6) {
				if (rain > 0.8) {
					return VanillaBiomes.JUNGLE; //jungle
				} else if (rain > 0.4) {
					return VanillaBiomes.FOREST; //forest
				} else {
					return VanillaBiomes.PLAIN; //plains
				}
			} else if (temp < 0.8) {
				if (rain > 0.8) {
					return VanillaBiomes.JUNGLE; //jungle
				} else if (rain > 0.6) {
					return VanillaBiomes.FOREST; //forest
				} else if (rain > 0.2) {
					return VanillaBiomes.PLAIN; //plains
				} else {
					return VanillaBiomes.DESERT; //desert
				}
			} else {
				if (rain > 0.8) {
					return VanillaBiomes.JUNGLE; //jungle
				} else if (rain > 0.6) {
					return VanillaBiomes.FOREST; //forest
				} else if (rain > 0.4) {
					return VanillaBiomes.PLAIN; //plains
				} else {
					return VanillaBiomes.DESERT; //desert
				}
			}
		}
	}

	/**
	 * Returns the raw elevation noise value [0-1] for a given location.
	 * @param x The x coordinate of the location.
	 * @param z The z coordinate of the location.
	 */
	public double getElevation(int x, int z) {
		return finalElevationNoise.GetValue(x / (scale * 128.0) + 0.05, 0.05, z / (scale * 128.0) + 0.05);
	}

	/**
	 * Returns the raw rainfall noise value [0-1] for a given location.
	 * @param x The x coordinate of the location.
	 * @param z The z coordinate of the location.
	 */
	public double getAvgRainfall(int x, int z) {
		return finalRainfall.GetValue(x / (scale * 128.0) + 0.05, 0.05, z / (scale * 128.0) + 0.05);
	}

	/**
	 * Returns the raw temperature noise value [0-1] for a given location.
	 * @param x The x coordinate of the location.
	 * @param z The z coordinate of the location.
	 */
	public double getAvgTemperature(int x, int z) {
		return finalTemp.GetValue(x / (scale * 128.0) + 0.05, 0.05, z / (scale * 128.0) + 0.05);
	}
}
