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
package org.spout.vanilla.world.generator.normal.biome.basic;

import net.royawesome.jlibnoise.module.modifier.ScalePoint;

import org.spout.vanilla.configuration.BiomeConfiguration;
import org.spout.vanilla.world.generator.normal.biome.GrassyBiome;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;

public class SmallMountainsBiome extends GrassyBiome {
	private final static ScalePoint NOISE = new ScalePoint();

	static {
		NOISE.SetSourceModule(0, NormalBiome.MASTER);
		NOISE.setxScale(BiomeConfiguration.SMALL_MOUNTAINS_X_SCALE.getDouble());
		NOISE.setyScale(BiomeConfiguration.SMALL_MOUNTAINS_Y_SCALE.getDouble());
		NOISE.setzScale(BiomeConfiguration.SMALL_MOUNTAINS_Z_SCALE.getDouble());
	}

	public SmallMountainsBiome(int biomeId) {
		super(biomeId, NOISE);

		minDensityTerrainHeight = BiomeConfiguration.SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_HEIGHT.getByte();
		maxDensityTerrainHeight = BiomeConfiguration.SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_HEIGHT.getByte();

		minDensityTerrainThickness = BiomeConfiguration.SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_THICKNESS.getByte();
		maxDensityTerrainThickness = BiomeConfiguration.SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_THICKNESS.getByte();

		upperHeightMapScale = BiomeConfiguration.SMALL_MOUNTAINS_UPPER_HEIGHT_MAP_SCALE.getFloat();
		bottomHeightMapScale = BiomeConfiguration.SMALL_MOUNTAINS_BOTTOM_HEIGHT_MAP_SCALE.getFloat();

		densityTerrainThicknessScale = BiomeConfiguration.SMALL_MOUNTAINS_DENSITY_TERRAIN_THICKNESS_SCALE.getFloat();
		densityTerrainHeightScale = BiomeConfiguration.SMALL_MOUNTAINS_DENSITY_TERRAIN_HEIGHT_SCALE.getFloat();
	}

	@Override
	public String getName() {
		return "Small Mountains";
	}
}
