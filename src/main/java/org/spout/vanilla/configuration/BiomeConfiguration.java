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
	/*
	 * Plains Biome 
	 */
	public static final ConfigurationHolder PLAINS_X_SCALE = new ConfigurationHolder(0.077, "biomes", "plains", "x-scale");
	public static final ConfigurationHolder PLAINS_Y_SCALE = new ConfigurationHolder(0.04, "biomes", "plains", "y-scale");
	public static final ConfigurationHolder PLAINS_Z_SCALE = new ConfigurationHolder(0.077, "biomes", "plains", "z-scale");
	
	public static final ConfigurationHolder PLAINS_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "plains", "min-density-terrain-height");
	public static final ConfigurationHolder PLAINS_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(69, "biomes", "plains", "max-density-terrain-height");
	
	public static final ConfigurationHolder PLAINS_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(0, "biomes", "plains", "min-density-terrain-thickness");
	public static final ConfigurationHolder PLAINS_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(3, "biomes", "plains", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder PLAINS_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(3f, "biomes", "plains", "upper-height-map-scale");
	public static final ConfigurationHolder PLAINS_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.3f, "biomes", "plains", "bottom-height-map-scale");
	
	public static final ConfigurationHolder PLAINS_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "plains", "density-terrain-thickness-scale");
	public static final ConfigurationHolder PLAINS_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "plains", "density-terrain-height-scale");
	/*
	 * Tundra Biome
	 */
	public static final ConfigurationHolder TUNDRA_X_SCALE = new ConfigurationHolder(0.09, "biomes", "tundra", "x-scale");
	public static final ConfigurationHolder TUNDRA_Y_SCALE = new ConfigurationHolder(0.08, "biomes", "tundra", "y-scale");
	public static final ConfigurationHolder TUNDRA_Z_SCALE = new ConfigurationHolder(0.09, "biomes", "tundra", "z-scale");
	
	public static final ConfigurationHolder TUNDRA_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "tundra", "min-density-terrain-height");
	public static final ConfigurationHolder TUNDRA_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(69, "biomes", "tundra", "max-density-terrain-height");
	
	public static final ConfigurationHolder TUNDRA_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(0, "biomes", "tundra", "min-density-terrain-thickness");
	public static final ConfigurationHolder TUNDRA_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(3, "biomes", "tundra", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder TUNDRA_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.3f, "biomes", "tundra", "upper-height-map-scale");
	public static final ConfigurationHolder TUNDRA_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.7f, "biomes", "tundra", "bottom-height-map-scale");
	
	public static final ConfigurationHolder TUNDRA_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "tundra", "density-terrain-thickness-scale");
	public static final ConfigurationHolder TUNDRA_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "tundra", "density-terrain-height-scale");
	/*
	 * Taiga Biome
	 */
	public static final ConfigurationHolder TAIGA_X_SCALE = new ConfigurationHolder(0.09, "biomes", "taiga", "x-scale");
	public static final ConfigurationHolder TAIGA_Y_SCALE = new ConfigurationHolder(0.08, "biomes", "taiga", "y-scale");
	public static final ConfigurationHolder TAIGA_Z_SCALE = new ConfigurationHolder(0.09, "biomes", "taiga", "z-scale");
	
	public static final ConfigurationHolder TAIGA_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "taiga", "min-density-terrain-height");
	public static final ConfigurationHolder TAIGA_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(71, "biomes", "taiga", "max-density-terrain-height");
	
	public static final ConfigurationHolder TAIGA_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(0, "biomes", "taiga", "min-density-terrain-thickness");
	public static final ConfigurationHolder TAIGA_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(3, "biomes", "taiga", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder TAIGA_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.3f, "biomes", "taiga", "upper-height-map-scale");
	public static final ConfigurationHolder TAIGA_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3.7f, "biomes", "taiga", "bottom-height-map-scale");
	
	public static final ConfigurationHolder TAIGA_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "taiga", "density-terrain-thickness-scale");
	public static final ConfigurationHolder TAIGA_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "taiga", "density-terrain-height-scale");
	/*
	 * Small Mountains Biome
	 */
	public static final ConfigurationHolder SMALL_MOUNTAINS_X_SCALE = new ConfigurationHolder(0.085, "biomes", "smallmountains", "x-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_Y_SCALE = new ConfigurationHolder(0.035, "biomes", "smallmountains", "y-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_Z_SCALE = new ConfigurationHolder(0.085, "biomes", "smallmountains", "z-scale");
	
	public static final ConfigurationHolder SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(64, "biomes", "smallmountains", "min-density-terrain-height");
	public static final ConfigurationHolder SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(74, "biomes", "smallmountains", "max-density-terrain-height");
	
	public static final ConfigurationHolder SMALL_MOUNTAINS_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(2, "biomes", "smallmountains", "min-density-terrain-thickness");
	public static final ConfigurationHolder SMALL_MOUNTAINS_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(6, "biomes", "smallmountains", "max-density-terrain-thickness");	
	
	public static final ConfigurationHolder SMALL_MOUNTAINS_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(5f, "biomes", "smallmountains", "upper-height-map-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(6f, "biomes", "smallmountains", "bottom-height-map-scale");
	
	public static final ConfigurationHolder SMALL_MOUNTAINS_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(5.5f, "biomes", "smallmountains", "density-terrain-thickness-scale");
	public static final ConfigurationHolder SMALL_MOUNTAINS_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(5.5f, "biomes", "smallmountains", "density-terrain-height-scale");
	/*
	 * Beach Biome
	 */
	public static final ConfigurationHolder BEACH_X_SCALE = new ConfigurationHolder(0.072, "biomes", "beach", "x-scale");
	public static final ConfigurationHolder BEACH_Y_SCALE = new ConfigurationHolder(0.08, "biomes", "beach", "y-scale");
	public static final ConfigurationHolder BEACH_Z_SCALE = new ConfigurationHolder(0.072, "biomes", "beach", "z-scale");
	
	public static final ConfigurationHolder BEACH_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(62, "biomes", "beach", "min-density-terrain-height");
	public static final ConfigurationHolder BEACH_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(63, "biomes", "beach", "max-density-terrain-height");
	
	public static final ConfigurationHolder BEACH_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(1, "biomes", "beach", "min-density-terrain-thickness");
	public static final ConfigurationHolder BEACH_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(2, "biomes", "beach", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder BEACH_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(2.4f, "biomes", "beach", "upper-height-map-scale");
	public static final ConfigurationHolder BEACH_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3f, "biomes", "beach", "bottom-height-map-scale");
	
	public static final ConfigurationHolder BEACH_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "beach", "density-terrain-thickness-scale");
	public static final ConfigurationHolder BEACH_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "beach", "density-terrain-height-scale");
	/*
	 * Desert Biome
	 */
	public static final ConfigurationHolder DESERT_X_SCALE = new ConfigurationHolder(0.075, "biomes", "desert", "x-scale");
	public static final ConfigurationHolder DESERT_Y_SCALE = new ConfigurationHolder(0.08, "biomes", "desert", "y-scale");
	public static final ConfigurationHolder DESERT_Z_SCALE = new ConfigurationHolder(0.075, "biomes", "desert", "z-scale");
	
	public static final ConfigurationHolder DESERT_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(67, "biomes", "desert", "min-density-terrain-height");
	public static final ConfigurationHolder DESERT_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(68, "biomes", "desert", "max-density-terrain-height");
	
	public static final ConfigurationHolder DESERT_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(0, "biomes", "desert", "min-density-terrain-thickness");
	public static final ConfigurationHolder DESERT_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(3, "biomes", "desert", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder DESERT_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(2.5f, "biomes", "desert", "upper-height-map-scale");
	public static final ConfigurationHolder DESERT_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3f, "biomes", "desert", "bottom-height-map-scale");
	
	public static final ConfigurationHolder DESERT_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "desert", "density-terrain-thickness-scale");
	public static final ConfigurationHolder DESERT_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "desert", "density-terrain-height-scale");
	/*
	 * Forest Biome
	 */
	public static final ConfigurationHolder FOREST_X_SCALE = new ConfigurationHolder(0.09, "biomes", "forest", "x-scale");
	public static final ConfigurationHolder FOREST_Y_SCALE = new ConfigurationHolder(0.045, "biomes", "forest", "y-scale");
	public static final ConfigurationHolder FOREST_Z_SCALE = new ConfigurationHolder(0.09, "biomes", "forest", "z-scale");
	
	public static final ConfigurationHolder FOREST_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(71, "biomes", "forest", "min-density-terrain-height");
	public static final ConfigurationHolder FOREST_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(74, "biomes", "forest", "max-density-terrain-height");
	
	public static final ConfigurationHolder FOREST_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(0, "biomes", "forest", "min-density-terrain-thickness");
	public static final ConfigurationHolder FOREST_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(3, "biomes", "forest", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder FOREST_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(4f, "biomes", "forest", "upper-height-map-scale");
	public static final ConfigurationHolder FOREST_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(5f, "biomes", "forest", "bottom-height-map-scale");
	
	public static final ConfigurationHolder FOREST_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "forest", "density-terrain-thickness-scale");
	public static final ConfigurationHolder FOREST_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "forest", "density-terrain-height-scale");
	/*
	 * Jungle Biome
	 */
	public static final ConfigurationHolder JUNGLE_X_SCALE = new ConfigurationHolder(0.087, "biomes", "jungle", "x-scale");
	public static final ConfigurationHolder JUNGLE_Y_SCALE = new ConfigurationHolder(0.045, "biomes", "jungle", "y-scale");
	public static final ConfigurationHolder JUNGLE_Z_SCALE = new ConfigurationHolder(0.087, "biomes", "jungle", "z-scale");
	
	public static final ConfigurationHolder JUNGLE_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(71, "biomes", "jungle", "min-density-terrain-height");
	public static final ConfigurationHolder JUNGLE_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(75, "biomes", "jungle", "max-density-terrain-height");
	
	public static final ConfigurationHolder JUNGLE_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(0, "biomes", "jungle", "min-density-terrain-thickness");
	public static final ConfigurationHolder JUNGLE_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(3, "biomes", "jungle", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder JUNGLE_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(4f, "biomes", "jungle", "upper-height-map-scale");
	public static final ConfigurationHolder JUNGLE_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(5f, "biomes", "jungle", "bottom-height-map-scale");
	
	public static final ConfigurationHolder JUNGLE_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(6f, "biomes", "jungle", "density-terrain-thickness-scale");
	public static final ConfigurationHolder JUNGLE_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(4f, "biomes", "jungle", "density-terrain-height-scale");
	/*
	 * Mountains Biome
	 */
	public static final ConfigurationHolder MOUNTAINS_X_SCALE = new ConfigurationHolder(0.080, "biomes", "mountains", "x-scale");
	public static final ConfigurationHolder MOUNTAINS_Y_SCALE = new ConfigurationHolder(0.040, "biomes", "mountains", "y-scale");
	public static final ConfigurationHolder MOUNTAINS_Z_SCALE = new ConfigurationHolder(0.080, "biomes", "mountains", "z-scale");
	
	public static final ConfigurationHolder MOUNTAINS_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(64, "biomes", "mountains", "min-density-terrain-height");
	public static final ConfigurationHolder MOUNTAINS_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(86, "biomes", "mountains", "max-density-terrain-height");
	
	public static final ConfigurationHolder MOUNTAINS_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(6, "biomes", "mountains", "min-density-terrain-thickness");
	public static final ConfigurationHolder MOUNTAINS_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(16, "biomes", "mountains", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder MOUNTAINS_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(5.3f, "biomes", "mountains", "upper-height-map-scale");
	public static final ConfigurationHolder MOUNTAINS_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(5f, "biomes", "mountains", "bottom-height-map-scale");
	
	public static final ConfigurationHolder MOUNTAINS_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(30f, "biomes", "mountains", "density-terrain-thickness-scale");
	public static final ConfigurationHolder MOUNTAINS_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(10f, "biomes", "mountains", "density-terrain-height-scale");
	/*
	 * Ocean Biome
	 */
	public static final ConfigurationHolder OCEAN_X_SCALE = new ConfigurationHolder(0.077, "biomes", "ocean", "x-scale");
	public static final ConfigurationHolder OCEAN_Y_SCALE = new ConfigurationHolder(0.030, "biomes", "ocean", "y-scale");
	public static final ConfigurationHolder OCEAN_Z_SCALE = new ConfigurationHolder(0.077, "biomes", "ocean", "z-scale");
	
	public static final ConfigurationHolder OCEAN_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(44, "biomes", "ocean", "min-density-terrain-height");
	public static final ConfigurationHolder OCEAN_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(53, "biomes", "ocean", "max-density-terrain-height");
	
	public static final ConfigurationHolder OCEAN_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(2, "biomes", "ocean", "min-density-terrain-thickness");
	public static final ConfigurationHolder OCEAN_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(5, "biomes", "ocean", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder OCEAN_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(6.7f, "biomes", "ocean", "upper-height-map-scale");
	public static final ConfigurationHolder OCEAN_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(7.5f, "biomes", "ocean", "bottom-height-map-scale");
	
	public static final ConfigurationHolder OCEAN_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(5f, "biomes", "ocean", "density-terrain-thickness-scale");
	public static final ConfigurationHolder OCEAN_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(5f, "biomes", "ocean", "density-terrain-height-scale");
	/*
	 * Swamp Biome
	 */
	public static final ConfigurationHolder SWAMP_X_SCALE = new ConfigurationHolder(0.067, "biomes", "swamp", "x-scale");
	public static final ConfigurationHolder SWAMP_Y_SCALE = new ConfigurationHolder(0.06, "biomes", "swamp", "y-scale");
	public static final ConfigurationHolder SWAMP_Z_SCALE = new ConfigurationHolder(0.067, "biomes", "swamp", "z-scale");
	
	public static final ConfigurationHolder SWAMP_MIN_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(60, "biomes", "swamp", "min-density-terrain-height");
	public static final ConfigurationHolder SWAMP_MAX_DENSITY_TERRAIN_HEIGHT = new ConfigurationHolder(65, "biomes", "swamp", "max-density-terrain-height");
	
	public static final ConfigurationHolder SWAMP_MIN_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(1, "biomes", "swamp", "min-density-terrain-thickness");
	public static final ConfigurationHolder SWAMP_MAX_DENSITY_TERRAIN_THICKNESS = new ConfigurationHolder(2, "biomes", "swamp", "max-density-terrain-thickness");
	
	public static final ConfigurationHolder SWAMP_UPPER_HEIGHT_MAP_SCALE = new ConfigurationHolder(2f, "biomes", "swamp", "upper-height-map-scale");
	public static final ConfigurationHolder SWAMP_BOTTOM_HEIGHT_MAP_SCALE = new ConfigurationHolder(3f, "biomes", "swamp", "bottom-height-map-scale");
	
	public static final ConfigurationHolder SWAMP_DENSITY_TERRAIN_THICKNESS_SCALE = new ConfigurationHolder(2f, "biomes", "swamp", "density-terrain-thickness-scale");
	public static final ConfigurationHolder SWAMP_DENSITY_TERRAIN_HEIGHT_SCALE = new ConfigurationHolder(3f, "biomes", "swamp", "density-terrain-height-scale");
	
	public BiomeConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "biomes.yml")));
	}
}
