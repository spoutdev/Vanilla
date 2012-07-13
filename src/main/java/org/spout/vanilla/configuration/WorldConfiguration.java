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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class WorldConfiguration extends YamlConfiguration {
	private final Map<String, WorldConfigurationNode> worldNodes = new HashMap<String, WorldConfigurationNode>();
	public static WorldConfigurationNode NORMAL;
	public static WorldConfigurationNode FLAT;
	public static WorldConfigurationNode NETHER;
	public static WorldConfigurationNode END;

	public WorldConfiguration(File dataFolder) {
		super(new File(dataFolder, "worlds.yml"));
		//TODO: Allow the creation of sub-sections for configuration holders
		NORMAL = getOrCreate("world").setDefaults("normal", "normal");
		FLAT = getOrCreate("world_flat").setDefaults("normal", "flat");
		NETHER = getOrCreate("world_nether").setDefaults("nether", "nether");
		END = getOrCreate("world_the_end").setDefaults("the_end", "the_end");
	}

	public Collection<WorldConfigurationNode> getAll() {
		return worldNodes.values();
	}

	/**
	 * Gets the world configuration of a certain world<br>
	 * Creates a new one if it doesn't exist
	 * @param world of the configuration
	 * @return the World configuration node
	 */
	public WorldConfigurationNode getOrCreate(World world) {
		return getOrCreate(world.getName());
	}

	/**
	 * Gets the world configuration of a certain world<br>
	 * Creates a new one if it doesn't exist
	 * @param worldname of the configuration
	 * @return the World configuration node
	 */
	public WorldConfigurationNode getOrCreate(String worldname) {
		synchronized (worldNodes) {
			WorldConfigurationNode node = worldNodes.get(worldname);
			if (node == null) {
				node = new WorldConfigurationNode(this, worldname);
				worldNodes.put(worldname, node);
			}
			return node;
		}
	}

	@Override
	public void load() throws ConfigurationException {
		super.load();
		for (WorldConfigurationNode node : getAll()) {
			node.load();
		}
	}

	@Override
	public void save() throws ConfigurationException {
		for (WorldConfigurationNode node : getAll()) {
			node.save();
		}
		super.save();
	}
}
