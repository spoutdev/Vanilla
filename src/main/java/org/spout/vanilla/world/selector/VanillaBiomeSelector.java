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

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Cylinders;
import net.royawesome.jlibnoise.module.source.Perlin;
import net.royawesome.jlibnoise.module.source.Voronoi;

import org.spout.api.generator.biome.Biome;
import org.spout.api.generator.biome.BiomeSelector;

import static org.spout.vanilla.world.generator.VanillaBiomes.BEACH;
import static org.spout.vanilla.world.generator.VanillaBiomes.DESERT;
import static org.spout.vanilla.world.generator.VanillaBiomes.DESERT_HILLS;
import static org.spout.vanilla.world.generator.VanillaBiomes.FOREST;
import static org.spout.vanilla.world.generator.VanillaBiomes.FOREST_HILLS;
import static org.spout.vanilla.world.generator.VanillaBiomes.FROZEN_OCEAN;
import static org.spout.vanilla.world.generator.VanillaBiomes.FROZEN_RIVER;
import static org.spout.vanilla.world.generator.VanillaBiomes.JUNGLE;
import static org.spout.vanilla.world.generator.VanillaBiomes.JUNGLE_HILLS;
import static org.spout.vanilla.world.generator.VanillaBiomes.MOUNTAINS;
import static org.spout.vanilla.world.generator.VanillaBiomes.MUSHROOM;
import static org.spout.vanilla.world.generator.VanillaBiomes.MUSHROOM_SHORE;
import static org.spout.vanilla.world.generator.VanillaBiomes.OCEAN;
import static org.spout.vanilla.world.generator.VanillaBiomes.PLAINS;
import static org.spout.vanilla.world.generator.VanillaBiomes.RIVER;
import static org.spout.vanilla.world.generator.VanillaBiomes.SMALL_MOUNTAINS;
import static org.spout.vanilla.world.generator.VanillaBiomes.SWAMP;
import static org.spout.vanilla.world.generator.VanillaBiomes.TAIGA;
import static org.spout.vanilla.world.generator.VanillaBiomes.TAIGA_HILLS;
import static org.spout.vanilla.world.generator.VanillaBiomes.TUNDRA;
import static org.spout.vanilla.world.generator.VanillaBiomes.TUNDRA_HILLS;

public class VanillaBiomeSelector extends BiomeSelector {
	private static final float DEFAULT_SCALE = 2.5f;
	//
	private static final float CONTINENTS_THRESHOLD = -0.1f;
	private static final float LAND_THRESHOLD = 0;
	private static final float SWAMP_THRESHOLD = 0;
	private static final float SMALL_MOUNTAIN_THRESHOLD = 0.75f;
	private static final float MOUNTAIN_THRESHOLD = 0.8f;
	private static final float RIVER_THRESHOLD = 0.7f;
	private static final float PLAINS_THRESHOLD = 2;
	private static final float FOREST_THRESHOLD = 1;
	private static final float FOREST_HILLS_THRESHOLD = 0.5f;
	private static final float JUNGLE_THRESHOLD = 0;
	private static final float JUNGLE_HILLS_THRESHOLD = 0.5f;
	private static final float DESERT_THRESHOLD = -1;
	private static final float DESERT_HILLS_THRESHOLD = 0.5f;
	private static final float TAIGA_THRESHOLD = -2;
	private static final float TAIGA_HILLS_THRESHOLD = 0.5f;
	private static final float TUNDRA_THRESHOLD = -3;
	private static final float TUNDRA_HILLS_THRESHOLD = 0.5f;
	private static final float FROZEN_OCEAN_THRESHOLD = 0.4f;
	private static final float MUSHROOM_THRESHOLD = 0.85f;
	private static final float MUSHROOM_SHORE_THRESHOLD = 0.8f;
	//
	private final Perlin continentsBase = new Perlin();
	private final Turbulence continents = new Turbulence();
	//
	private final Voronoi landBase = new Voronoi();
	private final Turbulence land = new Turbulence();
	//
	private final Turbulence rivers = new Turbulence();
	//
	private final Perlin detailBase = new Perlin();
	private final Turbulence detail = new Turbulence();

	public VanillaBiomeSelector() {
		this(DEFAULT_SCALE);
	}

	public VanillaBiomeSelector(float scale) {
		continentsBase.setFrequency(0.007 / scale);
		continentsBase.setOctaveCount(1);

		continents.SetSourceModule(0, continentsBase);
		continents.setFrequency(0.0125);
		continents.setPower(20);
		continents.setRoughness(1);
		//
		landBase.setFrequency(0.007 / scale);
		landBase.setDisplacement(3);

		land.SetSourceModule(0, landBase);
		land.setFrequency(0.0021875);
		land.setPower(70);
		//
		final Cylinders riversNoise = new Cylinders();
		riversNoise.setFrequency(0.0025);

		rivers.SetSourceModule(0, riversNoise);
		rivers.setFrequency(0.00625);
		rivers.setRoughness(1);
		rivers.setPower(100);
		//
		detailBase.setFrequency(0.01 / scale);
		detailBase.setOctaveCount(1);

		detail.SetSourceModule(0, detailBase);
		detail.setFrequency(0.0125);
		detail.setPower(20);
		detail.setRoughness(1);
	}

	@Override
	public Biome pickBiome(int x, int y, int z, long longSeed) {
		final int intSeed = (int) longSeed;
		continentsBase.setSeed(intSeed);
		continents.setSeed(intSeed * 2);
		landBase.setSeed(intSeed * 3);
		land.setSeed(intSeed * 5);
		rivers.setSeed(intSeed * 7);
		detailBase.setSeed(intSeed * 11);
		detail.setSeed(intSeed * 13);

		final float continentValue = (float) continents.GetValue(x, 0, z);
		if (continentValue > CONTINENTS_THRESHOLD) {
			if (continentValue > LAND_THRESHOLD) {
				if (continentValue > SMALL_MOUNTAIN_THRESHOLD) {
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return RIVER;
					}
					if (continentValue > MOUNTAIN_THRESHOLD) {
						return MOUNTAINS;
					}
					return SMALL_MOUNTAINS;
				}
				final float landValue = (float) land.GetValue(x, 0, z);
				if (landValue > PLAINS_THRESHOLD) {
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return RIVER;
					}
					return PLAINS;
				}
				final float hillValue = (float) detail.GetValue(x, 127, z);
				if (landValue > FOREST_THRESHOLD) {
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return RIVER;
					}
					if (hillValue > FOREST_HILLS_THRESHOLD) {
						return FOREST_HILLS;
					}
					return FOREST;
				}
				if (landValue > JUNGLE_THRESHOLD) {
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return RIVER;
					}
					if (hillValue > JUNGLE_HILLS_THRESHOLD) {
						return JUNGLE_HILLS;
					}
					return JUNGLE;
				}
				if (landValue > DESERT_THRESHOLD) {
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return RIVER;
					}
					if (hillValue > DESERT_HILLS_THRESHOLD) {
						return DESERT_HILLS;
					}
					return DESERT;
				}
				final float frozenOceanValue = (float) detail.GetValue(x, 255, z);
				if (landValue > TAIGA_THRESHOLD) {
					if (frozenOceanValue > FROZEN_OCEAN_THRESHOLD) {
						return FROZEN_OCEAN;
					}
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return FROZEN_RIVER;
					}
					if (hillValue > TAIGA_HILLS_THRESHOLD) {
						return TAIGA_HILLS;
					}
					return TAIGA;
				}
				if (landValue > TUNDRA_THRESHOLD) {
					if (frozenOceanValue > FROZEN_OCEAN_THRESHOLD) {
						return FROZEN_OCEAN;
					}
					if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
						return FROZEN_RIVER;
					}
					if (hillValue > TUNDRA_HILLS_THRESHOLD) {
						return TUNDRA_HILLS;
					}
					return TUNDRA;
				}
			}
			if (rivers.GetValue(x, 0, z) > RIVER_THRESHOLD) {
				return RIVER;
			}
			if (land.GetValue(x, 0, z) > SWAMP_THRESHOLD) {
				return SWAMP;
			}
			return BEACH;
		}
		final float mushroomValue = (float) detail.GetValue(x, 0, z);
		if (mushroomValue > MUSHROOM_THRESHOLD) {
			return MUSHROOM;
		}
		if (mushroomValue > MUSHROOM_SHORE_THRESHOLD) {
			return MUSHROOM_SHORE;
		}
		return OCEAN;
	}
}
