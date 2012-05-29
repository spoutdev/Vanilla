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
package org.spout.vanilla.configuration;

import java.io.File;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class WorldConfiguration extends ConfigurationHolderConfiguration {
	public static final ConfigurationHolder NORMAL_LOAD = new ConfigurationHolder(true, "worlds", "normal", "load");
	public static final ConfigurationHolder NORMAL_NAME = new ConfigurationHolder("world", "worlds", "normal", "name");
	public static final ConfigurationHolder NORMAL_LOADED_SPAWN = new ConfigurationHolder(true, "worlds", "normal", "keep-spawn-loaded");

	public static final ConfigurationHolder FLAT_LOAD = new ConfigurationHolder(true, "worlds", "flat", "load");
	public static final ConfigurationHolder FLAT_NAME = new ConfigurationHolder("world_flat", "worlds", "flat", "name");
	public static final ConfigurationHolder FLAT_LOADED_SPAWN = new ConfigurationHolder(true, "worlds", "flat", "keep-spawn-loaded");

	public static final ConfigurationHolder NETHER_LOAD = new ConfigurationHolder(true, "worlds", "nether", "load");
	public static final ConfigurationHolder NETHER_NAME = new ConfigurationHolder("world_nether", "worlds", "nether", "name");
	public static final ConfigurationHolder NETHER_LOADED_SPAWN = new ConfigurationHolder(true, "worlds", "nether", "keep-spawn-loaded");

	public static final ConfigurationHolder END_LOAD = new ConfigurationHolder(true, "worlds", "theend", "load");
	public static final ConfigurationHolder END_NAME = new ConfigurationHolder("world_the_end", "worlds", "theend", "name");
	public static final ConfigurationHolder END_LOADED_SPAWN = new ConfigurationHolder(true, "worlds", "theend", "keep-spawn-loaded");

	public WorldConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "worlds.yml")));
	}
}
