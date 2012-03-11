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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.spout.api.util.config.Configuration;
import org.spout.api.util.config.ConfigurationNode;

public class VanillaConfiguration extends Configuration {
	// General
	public static final ConfigurationNode MOTD = new ConfigurationNode("general.motd", "A Spout Server");
	public static final ConfigurationNode ENABLE_END_CREDITS = new ConfigurationNode("general.enable-ending-credits", true);
	// Physics
	public static final ConfigurationNode GRAVEL_PHYSICS = new ConfigurationNode("physics.gravel", true);
	public static final ConfigurationNode FIRE_PHYSICS = new ConfigurationNode("physics.fire", true);
	public static final ConfigurationNode LAVA_PHYSICS = new ConfigurationNode("physics.lava", true);
	public static final ConfigurationNode PISTON_PHYSICS = new ConfigurationNode("physics.piston", true);
	public static final ConfigurationNode REDSTONE_PHYSICS = new ConfigurationNode("physics.redstone", true);
	public static final ConfigurationNode SAND_PHYSICS = new ConfigurationNode("physics.sand", true);
	public static final ConfigurationNode WATER_PHYSICS = new ConfigurationNode("physics.water", true);
	public static final ConfigurationNode CACTUS_PHYSICS = new ConfigurationNode("physics.cactus", true);
	// Player
	public static final ConfigurationNode PLAYER_DEFAULT_GAMEMODE = new ConfigurationNode("player.default-gamemode", "survival");
	public static final ConfigurationNode PLAYER_SURVIVAL_ENABLE_HEALTH = new ConfigurationNode("player.survival.enable-health", true);
	public static final ConfigurationNode PLAYER_SURVIVAL_ENABLE_HUNGER = new ConfigurationNode("player.survival.enable-hunger", true);
	public static final ConfigurationNode PLAYER_SURVIVAL_ENABLE_XP = new ConfigurationNode("player.survival.enable-xp", true);
	public static final ConfigurationNode PLAYER_TIMEOUT_TICKS = new ConfigurationNode("player.timeout-ticks", 1200);
	public static final OpConfig OPS = new OpConfig();
	// Entity
	public static final ConfigurationNode ITEM_PICKUP_RANGE = new ConfigurationNode("entity.item-pickup-range", 3);

	public VanillaConfiguration() {
		super(new File("plugins/Vanilla/config.yml"));
	}

	@Override
	public void load() {
		super.load();
		for (Field field : VanillaConfiguration.class.getFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				try {
					Object f = field.get(null);
					if (f instanceof ConfigurationNode) {
						this.addNode((ConfigurationNode) f);
					}
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
			}
		}

		this.save();
		OPS.load();
	}
}
