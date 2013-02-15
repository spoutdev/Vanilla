/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.configuration;

import java.util.Map;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.ConfigurationNode;
import org.spout.api.util.config.MapConfiguration;

public final class WorldConfigurationNode extends ConfigurationHolderConfiguration {
	public final ConfigurationHolder LOAD = new ConfigurationHolder(true, "load");
	public final ConfigurationHolder GENERATOR = new ConfigurationHolder("normal", "generator");
	public final ConfigurationHolder LOADED_SPAWN = new ConfigurationHolder(false, "keep-spawn-loaded");
	public final ConfigurationHolder GAMEMODE = new ConfigurationHolder("creative", "game-mode");
	public final ConfigurationHolder DIFFICULTY = new ConfigurationHolder("normal", "difficulty");
	public final ConfigurationHolder SKY_TYPE = new ConfigurationHolder("normal", "sky-type");
	public final ConfigurationHolder SPAWN_ANIMALS = new ConfigurationHolder(true, "spawn-animals");
	public final ConfigurationHolder SPAWN_MONSTERS = new ConfigurationHolder(true, "spawn-monster");
	public final ConfigurationHolder ALLOW_FLIGHT = new ConfigurationHolder(false, "allow-flight");
	public final ConfigurationHolder MIN_Y = new ConfigurationHolder(32, "min-y");
	public final ConfigurationHolder MAX_Y = new ConfigurationHolder(224, "max-y");
	public final ConfigurationHolder STEP_Y = new ConfigurationHolder(160, "step-y");
	public final ConfigurationHolder FAR_VIEW_DISTANCE = new ConfigurationHolder(10, "view-distance", "far");
	public final ConfigurationHolder NORMAL_VIEW_DISTANCE = new ConfigurationHolder(7, "view-distance", "normal");
	public final ConfigurationHolder SHORT_VIEW_DISTANCE = new ConfigurationHolder(5, "view-distance", "short");
	public final ConfigurationHolder TINY_VIEW_DISTANCE = new ConfigurationHolder(3, "view-distance", "tiny");
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

	/**
	 * Changes the default value for the load key. 
	 * @param load True if the world should load by default, false if not.
	 * @return this node
	 */
	public WorldConfigurationNode shouldLoad(boolean load) {
		LOAD.setDefaultValue(load);
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
