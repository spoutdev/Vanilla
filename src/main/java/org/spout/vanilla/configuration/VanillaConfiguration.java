/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.yaml.YamlConfiguration;
import org.spout.vanilla.VanillaPlugin;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


public class VanillaConfiguration extends YamlConfiguration {
	// General
	public static final ConfigurationHolder MOTD = new ConfigurationHolder("A Spout Server", "general", "motd");
	public static final ConfigurationHolder ENABLE_END_CREDITS = new ConfigurationHolder(true, "general", "enable-ending-credits");
	public static final ConfigurationHolder FLATWORLD = new ConfigurationHolder(false, "general", "flatworld");
	// Physics
	public static final ConfigurationHolder GRAVEL_PHYSICS = new ConfigurationHolder(true, "physics", "gravel");
	public static final ConfigurationHolder FIRE_PHYSICS = new ConfigurationHolder(true, "physics", "fire");
	public static final ConfigurationHolder LAVA_PHYSICS = new ConfigurationHolder(true, "physics", "lava");
	public static final ConfigurationHolder PISTON_PHYSICS = new ConfigurationHolder(true, "physics", "piston");
	public static final ConfigurationHolder REDSTONE_PHYSICS = new ConfigurationHolder(true, "physics", "redstone");
	public static final ConfigurationHolder SAND_PHYSICS = new ConfigurationHolder(true, "physics", "sand");
	public static final ConfigurationHolder WATER_PHYSICS = new ConfigurationHolder(true, "physics", "water");
	public static final ConfigurationHolder CACTUS_PHYSICS = new ConfigurationHolder(true, "physics", "cactus");
	// Player
	public static final ConfigurationHolder PLAYER_PVP_ENABLED = new ConfigurationHolder(true, "player", "pvp-enabled");
	public static final ConfigurationHolder PLAYER_DEFAULT_GAMEMODE = new ConfigurationHolder("survival", "player", "default-gamemode");
	public static final ConfigurationHolder PLAYER_SURVIVAL_ENABLE_HEALTH = new ConfigurationHolder(true, "player", "survival", "enable-health");
	public static final ConfigurationHolder PLAYER_SURVIVAL_ENABLE_HUNGER = new ConfigurationHolder(true, "player", "survival", "enable-hunger");
	public static final ConfigurationHolder PLAYER_SURVIVAL_ENABLE_XP = new ConfigurationHolder(true, "player", "survival", "enable-xp");
	public static final ConfigurationHolder PLAYER_TIMEOUT_TICKS = new ConfigurationHolder(120, "player", "timeout-ticks");
	public static final OpConfig OPS = new OpConfig(VanillaPlugin.getInstance().getDataFolder());
	// Controller-specific
	public static final ConfigurationHolder ITEM_PICKUP_RANGE = new ConfigurationHolder(3, "controller", "item-pickup-range");

	public VanillaConfiguration(File dataFolder) {
		super(new File(dataFolder, "config.yml"));
		for (Field field : VanillaConfiguration.class.getFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				try {
					Object f = field.get(null);
					if (f instanceof ConfigurationHolder) {
						ConfigurationHolder node = (ConfigurationHolder) f;
						node.setConfiguration(this);
						node.getValue();
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}
	}

	@Override
	public void load() throws ConfigurationException {
		super.load();
		OPS.load();
	}
}
