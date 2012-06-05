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
package org.spout.vanilla.world.generator.normal.biome;

import net.royawesome.jlibnoise.module.modifier.ScalePoint;

import org.spout.vanilla.configuration.VanillaConfiguration;

public class SmallMountainsBiome extends VanillaNormalBiome {
	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, VanillaNormalBiome.MASTER);
		NOISE.setxScale(VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_X_SCALE.getDouble());
		NOISE.setyScale(VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_Y_SCALE.getDouble());
		NOISE.setzScale(VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_Z_SCALE.getDouble());
	}

	public SmallMountainsBiome(int biomeId) {
		super(biomeId, NOISE);
		minDensityTerrainHeight = ((Integer) VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_HEIGHT.getInt()).byteValue();
		maxDensityTerrainHeight = ((Integer) VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_HEIGHT.getInt()).byteValue();
		minDensityTerrainThickness = ((Integer) VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_THICKNESS.getInt()).byteValue();
		maxDensityTerrainThickness = ((Integer) VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_THICKNESS.getInt()).byteValue();
		upperHeightMapScale = VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_UPPER_HEIGHT_MAP_SCALE.getInt();
		bottomHeightMapScale = VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_BOTTOM_HEIGHT_MAP_SCALE.getInt();
		densityTerrainThicknessScale = VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_DENSITY_TERRAIN_THICKNESS_SCALE.getInt();
		densityTerrainHeightScale = VanillaConfiguration.BIOMES.SMALL_MOUNTAINS_DENSITY_TERRAIN_HEIGHT_SCALE.getInt();
	}

	@Override
	public String getName() {
		return "Small Mountains";
	}
}
