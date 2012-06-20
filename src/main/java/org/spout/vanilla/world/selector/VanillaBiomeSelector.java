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

import net.royawesome.jlibnoise.module.modifier.Exponent;
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
import static org.spout.vanilla.world.generator.VanillaBiomes.PLAIN;
import static org.spout.vanilla.world.generator.VanillaBiomes.RIVER;
import static org.spout.vanilla.world.generator.VanillaBiomes.SMALL_MOUNTAINS;
import static org.spout.vanilla.world.generator.VanillaBiomes.SWAMP;
import static org.spout.vanilla.world.generator.VanillaBiomes.TAIGA;
import static org.spout.vanilla.world.generator.VanillaBiomes.TAIGA_HILLS;
import static org.spout.vanilla.world.generator.VanillaBiomes.TUNDRA;
import static org.spout.vanilla.world.generator.VanillaBiomes.TUNDRA_HILLS;

public class VanillaBiomeSelector extends BiomeSelector {
	private final Voronoi primaryBase = new Voronoi();
	private final Turbulence primary = new Turbulence();
	//
	private final Voronoi secondaryBase = new Voronoi();
	private final Turbulence secondary = new Turbulence();
	//
	private final Voronoi shoreBase = new Voronoi();
	private final Turbulence shore = new Turbulence();
	//
	private final Voronoi shoreDivideBase = new Voronoi();
	private final Turbulence shoreDivide = new Turbulence();
	//
	private final Voronoi edgeBase = new Voronoi();
	private final Turbulence edge = new Turbulence();
	//
	private final Turbulence riversBase = new Turbulence();
	private final Exponent rivers = new Exponent();
	//
	private final Perlin hillsBase = new Perlin();
	private final Turbulence hills = new Turbulence();
	//
	private final Perlin mushroomBase = new Perlin();
	private final Turbulence mushroom = new Turbulence();

	public VanillaBiomeSelector(float biomeScale) {

		primaryBase.setFrequency(0.0175D / biomeScale);
		primaryBase.setDisplacement(0.5D);

		primary.SetSourceModule(0, primaryBase);
		primary.setFrequency(0.0021875D);
		primary.setPower(70);
		//
		secondaryBase.setFrequency(0.025D / biomeScale);
		secondaryBase.setDisplacement(0.5D);

		secondary.SetSourceModule(0, secondaryBase);
		secondary.setFrequency(0.003125D);
		secondary.setPower(70);
		//
		shoreBase.setFrequency(0.0175D / biomeScale);
		shoreBase.setDisplacement(0);
		shoreBase.setEnableDistance(true);

		shore.SetSourceModule(0, shoreBase);
		shore.setFrequency(0.0021875D);
		shore.setPower(70);
		//
		shoreDivideBase.setFrequency(0.01D / biomeScale);
		shoreDivideBase.setDisplacement(0.5D);

		shoreDivide.SetSourceModule(0, shoreDivideBase);
		shoreDivide.setFrequency(0.00125);
		shoreDivide.setPower(70);
		//
		edgeBase.setFrequency(0.0175D / biomeScale);
		edgeBase.setDisplacement(0);
		edgeBase.setEnableDistance(true);

		edge.SetSourceModule(0, edgeBase);
		edge.setFrequency(0.0021875);
		edge.setPower(70);
		//
		final Cylinders riversNoise = new Cylinders();
		riversNoise.setFrequency(0.0025);

		riversBase.SetSourceModule(0, riversNoise);
		riversBase.setFrequency(0.00625D);
		riversBase.setRoughness(2);
		riversBase.setPower(100);

		rivers.SetSourceModule(0, riversBase);
		rivers.setExponent(10);
		//
		hillsBase.setFrequency(0.016D);
		hillsBase.setOctaveCount(1);

		hills.SetSourceModule(0, hillsBase);
		hills.setFrequency(0.005D);
		hills.setPower(6);
		//
		mushroomBase.setFrequency(0.01D / biomeScale);
		mushroomBase.setOctaveCount(1);

		mushroom.SetSourceModule(0, mushroomBase);
		mushroom.setFrequency(0.0125D);
		mushroom.setPower(20);
	}

	@Override
	public Biome pickBiome(int x, int y, int z, long seed) {
		// mushroom biome @(250, 250) seed: -132366922
		primaryBase.setSeed((int) seed);
		primary.setSeed((int) seed);

		secondaryBase.setSeed((int) seed * 2);
		secondary.setSeed((int) seed * 2);

		shoreBase.setSeed((int) seed);
		shore.setSeed((int) seed);

		shoreDivideBase.setSeed((int) seed * 3);
		shoreDivide.setSeed((int) seed * 3);

		edgeBase.setSeed((int) seed);
		edge.setSeed((int) seed);

		riversBase.setSeed((int) seed * 5);

		hillsBase.setSeed((int) seed * 7);
		hills.setSeed((int) seed * 7);

		mushroomBase.setSeed((int) seed * 11);
		mushroom.setSeed((int) seed * 11);

		final int primaryValue = (int) (primary.GetValue(x, 0, z) * 5 + 2.5);

		// ocean, shores and islands
		if (primaryValue == 0) {
			final double shoreValue = shore.GetValue(x, 0, z);
			if ((int) (shoreDivide.GetValue(x, 0, z) + 1) == 0) {
				if (shoreValue > 0) {
					// rivers
					if (rivers.GetValue(x, 0, z) > 0.45) {
						return RIVER;
					}
					return SWAMP;
				}
			}
			if (shoreValue > 0.4) {
				// rivers
				if (rivers.GetValue(x, 0, z) > 0.45) {
					return RIVER;
				}
				return BEACH;
			}
			// mushroom islands
			final double mushroomValue = mushroom.GetValue(x, 0, z);
			if (mushroomValue > 0.9) {
				return MUSHROOM;
			}
			if (mushroomValue > 0.87) {
				return MUSHROOM_SHORE;
			}
			return OCEAN;
		}
		// frozen oceans
		if (primaryValue == 3) {
			if (edge.GetValue(x, 0, z) < -0.40) {
				return FROZEN_OCEAN;
			}
		}
		// rivers
		if (rivers.GetValue(x, 0, z) > 0.45) {
			if (primaryValue == 3) {
				if ((int) (secondary.GetValue(x, 0, z) + 1) == 1) {
					return FROZEN_RIVER;
				}
			}
			return RIVER;
		}
		// main biomes and their hills
		if (primaryValue == 1) {
			if ((int) (secondary.GetValue(x, 0, z) + 1) == 0) {
				if (hills.GetValue(x, 0, z) > 0.6) {
					return DESERT_HILLS;
				}
				return DESERT;
			}
			return PLAIN;
		}
		if (primaryValue == 2) {
			if ((int) (secondary.GetValue(x, 0, z) + 1) == 0) {
				if (hills.GetValue(x, 0, z) > 0.6) {
					return FOREST_HILLS;
				}
				return FOREST;
			}
			if (hills.GetValue(x, 0, z) > 0.6) {
				return JUNGLE_HILLS;
			}
			return JUNGLE;
		}
		if (primaryValue == 3) {
			if ((int) (secondary.GetValue(x, 0, z) + 1) == 0) {
				if (hills.GetValue(x, 0, z) > 0.6) {
					return TAIGA_HILLS;
				}
				return TAIGA;
			}
			if (hills.GetValue(x, 0, z) > 0.6) {
				return TUNDRA_HILLS;
			}
			return TUNDRA;
		}
		// mountains and mountain edges
		if (edge.GetValue(x, 0, z) < -0.25) {
			return MOUNTAINS;
		}
		return SMALL_MOUNTAINS;
	}
}
