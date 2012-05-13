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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

public class VanillaConfiguration extends ConfigurationHolderConfiguration {
	// General
	public static final ConfigurationHolder MOTD = new ConfigurationHolder("A Spout Server", "general", "motd");
	public static final ConfigurationHolder ENABLE_END_CREDITS = new ConfigurationHolder(true, "general", "enable-ending-credits");
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
	public static final OpConfiguration OPS = null;
	// Controller-specific
	public static final ConfigurationHolder ITEM_PICKUP_RANGE = new ConfigurationHolder(3, "controller", "item-pickup-range");

	public VanillaConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "config.yml")));
		/**
		 * OPConfiguration is a public static final field which normally forces us to provide a getInstance method within the
		 * VanillaPlugin class file. This is a bad approach (should never need getInstance) but since we need the data folder for
		 * OPConfiguration, reflection is the only way. This is guaranteed to not fail due to how VanillaConfiguration is initialized
		 * (within onLoad) and since it is static final, the value will NEVER be re-created/changed (as onLoad is only called on
		 * plugin load and not within an onReload). An if check is also provided to make sure that the field isn't reflected again.
		 */
		if (OPS == null) {
			//Commence dark magic
			try {
				Field workaround = getClass().getDeclaredField("OPS");
				//Set the OPs field to accessable (bypass final).
				workaround.setAccessible(true);
				//Get the static modifier
				Field staticFinalModifier = workaround.getClass().getDeclaredField("modifiers");
				//Set the final aspect to modifiable
				staticFinalModifier.setAccessible(true);
				//Remove the final
				staticFinalModifier.setInt(workaround, workaround.getModifiers() & ~Modifier.FINAL);

				//OPS is a static final field and as such java ignores it for the first object parameter.
				workaround.set(null, new OpConfiguration(dataFolder));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void load() throws ConfigurationException {
		OPS.load();
		OPS.save();
		super.load();
		super.save();
	}

	@Override
	public void save() throws ConfigurationException {
		OPS.save();
		super.save();
	}
}
