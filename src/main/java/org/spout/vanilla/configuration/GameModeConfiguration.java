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

public class GameModeConfiguration extends ConfigurationHolderConfiguration {
	//survival
	public static final ConfigurationHolder SURVIVAL_HEALTH = new ConfigurationHolder(true, "gamemodes", "survival", "health");
	public static final ConfigurationHolder SURVIVAL_HUNGER = new ConfigurationHolder(true, "gamemodes", "survival", "hunger");
	public static final ConfigurationHolder SURIVAL_ITEM_DROPS = new ConfigurationHolder(true, "gamemodes", "survival", "itemdrops");
	public static final ConfigurationHolder SURIVIAL_INFINITE_ITEMS = new ConfigurationHolder(false, "gamemodes", "survival", "infiniteitems");
	public static final ConfigurationHolder SURVIVAL_INSTANT_BREAK = new ConfigurationHolder(false, "gamemodes", "survival", "instantbreak");
	public static final ConfigurationHolder SURVIVAL_DURABILITY = new ConfigurationHolder(true, "gamemodes", "survival", "tooldurablility");
	
	//creative
	public static final ConfigurationHolder CREATIVE_HEALTH = new ConfigurationHolder(false, "gamemodes", "creative", "health");
	public static final ConfigurationHolder CREATIVEL_HUNGER = new ConfigurationHolder(false, "gamemodes", "creative", "hunger");
	public static final ConfigurationHolder CREATIVE_ITEM_DROPS = new ConfigurationHolder(false, "gamemodes", "creative", "itemdrops");
	public static final ConfigurationHolder CREATIVE_INFINITE_ITEMS = new ConfigurationHolder(true, "gamemodes", "creative", "infiniteitems");
	public static final ConfigurationHolder CREATIVE_INSTANT_BREAK = new ConfigurationHolder(true, "gamemodes", "creative", "instantbreak");
	public static final ConfigurationHolder CREATIVE_DURABILITY = new ConfigurationHolder(false, "gamemodes", "creative", "tooldurablility");

	public GameModeConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "gamemodes.yml")));
	}
}
