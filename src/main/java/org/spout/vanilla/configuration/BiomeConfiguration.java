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
package org.spout.vanilla.configuration;

import java.io.File;

import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class BiomeConfiguration extends ConfigurationHolderConfiguration {
	//Plains Biome
	public static final ConfigurationHolder PLAINS_X_SCALE = new ConfigurationHolder(0.077, "biomes", "plains", "x-scale");
	public static final ConfigurationHolder PLAINS_Y_SCALE = new ConfigurationHolder(0.04, "biomes", "plains", "y-scale");
	public static final ConfigurationHolder PLAINS_Z_SCALE = new ConfigurationHolder(0.077, "biomes", "plains", "z-scale");
	public static final ConfigurationHolder PLAINS_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "plains", "min-density-terrain-height");
	public static final ConfigurationHolder PLAINS_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(69, "biomes", "plains", "max-density-terrain-height");
	public static final ConfigurationHolder PLAINS_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(3, "biomes", "plains", "upper-height-map-scale");
	public static final ConfigurationHolder PLAINS_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.3, "biomes", "plains", "bottom-height-map-scale");
	//Tundra Biome
	public static final ConfigurationHolder TUNDRA_X_SCALE = new ConfigurationHolder(0.09, "biomes", "tundra", "x-scale");
	public static final ConfigurationHolder TUNDRA_Y_SCALE = new ConfigurationHolder(0.08, "biomes", "tundra", "y-scale");
	public static final ConfigurationHolder TUNDRA_Z_SCALE = new ConfigurationHolder(0.09, "biomes", "tundra", "z-scale");
	public static final ConfigurationHolder TUNDRA_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "tundra", "min-density-terrain-height");
	public static final ConfigurationHolder TUNDRA_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(69, "biomes", "tundra", "max-density-terrain-height");
	public static final ConfigurationHolder TUNDRA_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.3, "biomes", "tundra", "upper-height-map-scale");
	public static final ConfigurationHolder TUNDRA_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.7, "biomes", "tundra", "bottom-height-map-scale");
	//Taiga Biome
	public static final ConfigurationHolder TAIGA_X_SCALE = new ConfigurationHolder(0.09, "biomes", "taiga", "x-scale");
	public static final ConfigurationHolder TAIGA_Y_SCALE = new ConfigurationHolder(0.08, "biomes", "taiga", "y-scale");
	public static final ConfigurationHolder TAIGA_Z_SCALE = new ConfigurationHolder(0.09, "biomes", "taiga", "z-scale");
	public static final ConfigurationHolder TAIGA_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "taiga", "min-density-terrain-height");
	public static final ConfigurationHolder TAIGA_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(71, "biomes", "taiga", "max-density-terrain-height");
	public static final ConfigurationHolder TAIGA_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.3, "biomes", "taiga", "upper-height-map-scale");
	public static final ConfigurationHolder TAIGA_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.7, "biomes", "taiga", "bottom-height-map-scale");
	//Small Mountains Biome
	public static final ConfigurationHolder SMALL_MOUNTAINS_X_SCALE = new ConfigurationHolder(0.085, "biomes", "smallmountains", "x-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_Y_SCALE = new ConfigurationHolder(0.035, "biomes", "smallmountains", "y-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_Z_SCALE = new ConfigurationHolder(0.085, "biomes", "smallmountains", "z-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(5.5, "biomes", "smallmountains", "density-terrain-height-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(64, "biomes", "smallmountains", "min-density-terrain-height");
	public static final ConfigurationHolder SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(74, "biomes", "smallmountains", "max-density-terrain-height");
	public static final ConfigurationHolder SMALL_MOUNTAINS_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(5.5, "biomes", "smallmountains", "density-terrain-thickness-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(2, "biomes", "smallmountains", "min-density-terrain-thickness");
	public static final ConfigurationHolder SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(6, "biomes", "smallmountains", "max-density-terrain-thickness");
	public static final ConfigurationHolder SMALL_MOUNTAINS_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(5, "biomes", "smallmountains", "upper-height-map-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(6, "biomes", "smallmountains", "bottom-height-map-scale");

	public BiomeConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "biomes.yml")));
	}
}
