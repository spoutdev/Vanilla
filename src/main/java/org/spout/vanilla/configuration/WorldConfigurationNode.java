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

import java.util.Map;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.MapConfiguration;

public final class WorldConfigurationNode extends ConfigurationHolderConfiguration {
	public final ConfigurationHolder LOAD = new ConfigurationHolder(true, "load");
	public final ConfigurationHolder GENERATOR = new ConfigurationHolder("normal", "generator");
	public final ConfigurationHolder LOADED_SPAWN = new ConfigurationHolder(true, "keep-spawn-loaded");
	public final ConfigurationHolder GAMEMODE = new ConfigurationHolder("creative", "game-mode");
	public final ConfigurationHolder DIFFICULTY = new ConfigurationHolder("normal", "difficulty");
	public final ConfigurationHolder SKY_TYPE = new ConfigurationHolder("normal", "sky-type");
	public final ConfigurationHolder SPAWN_ANIMALS = new ConfigurationHolder(true, "spawn-animals");
	public final ConfigurationHolder SPAWN_MONSTERS = new ConfigurationHolder(true, "spawn-monster");
	public final ConfigurationHolder ALLOW_FLIGHT = new ConfigurationHolder(false, "allow-flight");
	private final String name;
	private final WorldConfiguration parent;

	public WorldConfigurationNode(WorldConfiguration parent, String worldname) {
		super(new MapConfiguration(parent.getNode("worlds", worldname).getValues()));
		this.parent = parent;
		this.name = worldname;
	}

	/**
	 * Gets the world name of this world configuration node
	 * @return the world name
	 */
	public String getWorldName() {
		return this.name;
	}

	/**
	 * Gets the parent configuration of this world configuration node
	 * @return the parent configuration
	 */
	public WorldConfiguration getParent() {
		return this.parent;
	}

	/**
	 * Sets some of the default values for this configuration node
	 * @param sky default
	 * @param generator default
	 * @return this node
	 */
	public WorldConfigurationNode setDefaults(String sky, String generator) {
		this.SKY_TYPE.setDefaultValue(sky);
		this.GENERATOR.setDefaultValue(generator);
		return this;
	}

	@Override
	public void load() throws ConfigurationException {
		this.setConfiguration(new MapConfiguration(this.getParent().getNode("worlds", this.getWorldName()).getValues()));
		super.load();
	}

	@Override
	public void save() throws ConfigurationException {
		super.save();
		ConfigurationNode node = this.getParent().getNode("worlds", this.getWorldName());
		for (Map.Entry<String, Object> entry : this.getValues().entrySet()) {
			node.getNode(entry.getKey()).setValue(entry.getValue());
		}
	}
}
