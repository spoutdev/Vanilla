/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
import net.royawesome.jlibnoise.module.source.Voronoi;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeGenerator;
import org.spout.api.generator.biome.BiomeSelector;

import org.spout.vanilla.world.generator.VanillaBiomes;

public class WhittakerNoiseSelector extends BiomeSelector {
	private static BiomeGenerator parentGenerator;
	private static double scale;
	private static Voronoi rainfall;
	private static Const uniformOcean, shoreline;
	private static Perlin temp;
	private static RidgedMulti mountain;
	private static Billow continentSelector, hills;
	private static Turbulence rainfallTurbulence, tempTurbulence;
	private static ScaleBias rainfallModifier, hillsModifier, mountainModifier, continentSelectorModifier, tempModifier;
	private static Clamp hillsClamp, elevationClamp, finalRainfall, finalTemp;
	private static Select oceanBeachDivide, mountainRangeSelector, finalElevationNoise;

	/**
	 * Selects biomes based on Whittaker's humidity-temperature model.
	 *
	 * @param scale		Make this bigger if you'd like bigger biomes.
	 */
	public WhittakerNoiseSelector(BiomeGenerator parentGenerator, double scale) {
		this.parentGenerator = parentGenerator;
		this.scale = scale;

		//Creates the noise for rainfall distribution
		rainfall = new Voronoi();
		rainfall.setDisplacement(1.0);
		rainfall.setFrequency(1.0);
		rainfallModifier = new ScaleBias();
		rainfallModifier.SetSourceModule(0, rainfall);
		rainfallModifier.setScale(0.5);
		rainfallModifier.setBias(0.5);
		rainfallTurbulence = new Turbulence();
		rainfallTurbulence.SetSourceModule(0, rainfallModifier);
		rainfallTurbulence.setFrequency(0.5);
		rainfallTurbulence.setPower(0.75);
		finalRainfall = new Clamp();
		finalRainfall.SetSourceModule(0, rainfallTurbulence);
		finalRainfall.setLowerBound(0);
		finalRainfall.setUpperBound(1.0);

		//Creates the noise module for temperature distribution
		temp = new Perlin();
		temp.setFrequency(0.4);
		temp.setOctaveCount(4);
		tempModifier = new ScaleBias();
		tempModifier.SetSourceModule(0, temp);
		tempModifier.setScale(0.5);
		tempModifier.setBias(0.5);
		tempTurbulence = new Turbulence();
		tempTurbulence.SetSourceModule(0, tempModifier);
		tempTurbulence.setFrequency(0.4);
		tempTurbulence.setPower(0.5);
		finalTemp = new Clamp();
		finalTemp.SetSourceModule(0, tempTurbulence);
		finalTemp.setLowerBound(0);
		finalTemp.setUpperBound(1.0);

		//Creates the noise modules for the elevation
		uniformOcean = new Const();
		uniformOcean.setValue(-1);
		shoreline = new Const();
		shoreline.setValue(0.1);
		mountain = new RidgedMulti();
		mountain.setFrequency(0.3);
		mountain.setLacunarity(3.5);
		mountain.setOctaveCount(4);
		mountainModifier = new ScaleBias();
		mountainModifier.SetSourceModule(0, mountain);
		mountainModifier.setBias(0.5);
		mountainModifier.setScale(0.5);
		hills = new Billow();
		hills.setFrequency(0.45);
		hills.setPersistence(0.5);
		hills.setLacunarity(2.5);
		hills.setOctaveCount(6);
		hillsModifier = new ScaleBias();
		hillsModifier.SetSourceModule(0, hills);
		hillsModifier.setBias(0.5);
		hillsModifier.setScale(0.5);
		hillsClamp = new Clamp();
		hillsClamp.SetSourceModule(0, hillsModifier);
		hillsClamp.setLowerBound(0);
		hillsClamp.setUpperBound(0.65);

		//Puts the elevation noises together
		mountainRangeSelector = new Select();
		mountainRangeSelector.SetSourceModule(1, mountainModifier);
		mountainRangeSelector.SetSourceModule(0, hillsClamp);
		mountainRangeSelector.setControlModule(mountainModifier);
		mountainRangeSelector.setBounds(100, 0.75);
		mountainRangeSelector.setEdgeFalloff(0.4);
		elevationClamp = new Clamp();
		elevationClamp.SetSourceModule(0, mountainRangeSelector);
		elevationClamp.setLowerBound(0.11); //just more than for beach biomes
		elevationClamp.setUpperBound(1.0);

		//Creates the noise module for separating continents & oceans
		continentSelector = new Billow();
		continentSelector.setFrequency(0.2);
		continentSelector.setOctaveCount(10);
		continentSelectorModifier = new ScaleBias();
		continentSelectorModifier.SetSourceModule(0, continentSelector);
		continentSelectorModifier.setScale(0.5);
		continentSelectorModifier.setBias(0.5);

		//Divides the ocean and continents, starting with the beach level
		oceanBeachDivide = new Select();
		oceanBeachDivide.SetSourceModule(0, uniformOcean);
		oceanBeachDivide.SetSourceModule(1, shoreline);
		oceanBeachDivide.setControlModule(continentSelectorModifier);
		oceanBeachDivide.setBounds(100, 0.52);

		//Creates the final elevation noise for biome selection
		finalElevationNoise = new Select();
		finalElevationNoise.SetSourceModule(0, oceanBeachDivide);
		finalElevationNoise.SetSourceModule(1, elevationClamp);
		finalElevationNoise.setControlModule(continentSelectorModifier);
		finalElevationNoise.setBounds(100, 0.55);
	}

	@Override
	public Biome pickBiome(int x, int y, int z, long seed) {
		continentSelector.setSeed((int) seed);
		rainfall.setSeed((int) seed * 2);
		temp.setSeed((int) seed * 7);
		mountain.setSeed((int) seed * 5);
		hills.setSeed((int) seed * 13);

		double elevation, rain, temp, divisor = scale * 128.0;

		elevation = finalElevationNoise.GetValue(x / divisor + 0.05, 0.05, z / divisor + 0.05);
		rain = finalRainfall.GetValue(x / divisor + 0.05, 0.05, z / divisor + 0.05);
		temp = finalTemp.GetValue(x / divisor + 0.05, 0.05, z / divisor + 0.05);

		//TODO: make these conditions use the values from the biome classes themselves
		return getWhittakerBiome(elevation, temp, rain);
	}

	private Biome getWhittakerBiome(double elevation, double temp, double rain) {
		if (elevation > 0.8) {
			if (parentGenerator.getBiomes().contains(VanillaBiomes.MOUNTAINS)) {
				return VanillaBiomes.MOUNTAINS; //mountains
			} else {
				return VanillaBiomes.OCEAN;
			}
		} else if (elevation > 0.65) {
			if (parentGenerator.getBiomes().contains(VanillaBiomes.SMALL_MOUNTAINS)) {
				return VanillaBiomes.SMALL_MOUNTAINS; //small mountains
			} else {
				return VanillaBiomes.OCEAN;
			}
		} else if (elevation == 0.1) {
			if (rain > 0.6) {
				if (parentGenerator.getBiomes().contains(VanillaBiomes.SWAMP)) {
					return VanillaBiomes.SWAMP; //swamp
				} else {
					return VanillaBiomes.OCEAN;
				}
			} else {
				if (parentGenerator.getBiomes().contains(VanillaBiomes.BEACH)) {
					return VanillaBiomes.BEACH; //beach
				} else {
					return VanillaBiomes.OCEAN;
				}
			}
		} else if (elevation == -1) {
			return VanillaBiomes.OCEAN; //ocean
		} else {
			if (temp < 0.2) {
				if (rain > 0.6) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.TAIGA)) {
						return VanillaBiomes.TAIGA; //taiga
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.TUNDRA)) {
						return VanillaBiomes.TUNDRA; //tundra
					} else {
						return VanillaBiomes.OCEAN;
					}
				}
			} else if (temp < 0.4) {
				if (rain > 0.6) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.FOREST)) {
						return VanillaBiomes.FOREST; //forest
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.TAIGA)) {
						return VanillaBiomes.TAIGA; //taiga
					} else {
						return VanillaBiomes.OCEAN;
					}
				}
			} else if (temp < 0.6) {
				if (rain > 0.8) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.JUNGLE)) {
						return VanillaBiomes.JUNGLE; //jungle
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else if (rain > 0.4) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.FOREST)) {
						return VanillaBiomes.FOREST; //forest
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.PLAIN)) {
						return VanillaBiomes.PLAIN; //plains
					} else {
						return VanillaBiomes.OCEAN;
					}
				}
			} else if (temp < 0.8) {
				if (rain > 0.8) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.JUNGLE)) {
						return VanillaBiomes.JUNGLE; //jungle
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else if (rain > 0.6) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.FOREST)) {
						return VanillaBiomes.FOREST; //forest
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else if (rain > 0.2) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.PLAIN)) {
						return VanillaBiomes.PLAIN; //plains
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.DESERT)) {
						return VanillaBiomes.DESERT; //desert
					} else {
						return VanillaBiomes.OCEAN;
					}
				}
			} else {
				if (rain > 0.8) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.JUNGLE)) {
						return VanillaBiomes.JUNGLE; //jungle
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else if (rain > 0.6) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.FOREST)) {
						return VanillaBiomes.FOREST; //forest
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else if (rain > 0.4) {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.PLAIN)) {
						return VanillaBiomes.PLAIN; //plains
					} else {
						return VanillaBiomes.OCEAN;
					}
				} else {
					if (parentGenerator.getBiomes().contains(VanillaBiomes.DESERT)) {
						return VanillaBiomes.DESERT; //desert
					} else {
						return VanillaBiomes.OCEAN;
					}
				}
			}
		}
	}
}
