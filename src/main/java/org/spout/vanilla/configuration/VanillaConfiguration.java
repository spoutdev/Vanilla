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

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

import org.spout.vanilla.VanillaPlugin;

public class VanillaConfiguration extends ConfigurationHolderConfiguration {
	// General
	public static final ConfigurationHolder MOTD = new ConfigurationHolder("A Spout Server", "general", "motd");
	public static final ConfigurationHolder UPNP = new ConfigurationHolder(true, "general", "upnp");
	public static final ConfigurationHolder BONJOUR = new ConfigurationHolder(true, "general", "bonjour");
	public static final ConfigurationHolder LAN_DISCOVERY = new ConfigurationHolder(true, "general", "lan-discovery");
	public static final ConfigurationHolder ONLINE_MODE = new ConfigurationHolder(true, "general", "online-mode");
	public static final ConfigurationHolder ENABLE_END_CREDITS = new ConfigurationHolder(true, "general", "enable-ending-credits");
	public static final ConfigurationHolder SPAWN_RADIUS = new ConfigurationHolder(4, "general", "spawn-radius");
	public static final ConfigurationHolder SPAWN_PROTECTION_RADIUS = new ConfigurationHolder(10, "general", "spawn-protection-radius");
	public static final ConfigurationHolder CHUNK_INIT = new ConfigurationHolder("client", "general", "chunk-init");
	public static final ConfigurationHolder HARDCORE_MODE = new ConfigurationHolder(false, "general", "hardcore-mode");
	public static final ConfigurationHolder OUTDATED_SERVER_MESSAGE = new ConfigurationHolder("Outdated server!","general","outdated-server");
	public static final ConfigurationHolder OUTDATED_CLIENT_MESSAGE = new ConfigurationHolder("Outdated client!","general","outdated-client");
	// Physics
	public static final ConfigurationHolder GRAVEL_PHYSICS = new ConfigurationHolder(true, "physics", "gravel");
	public static final ConfigurationHolder FIRE_PHYSICS = new ConfigurationHolder(true, "physics", "fire");
	public static final ConfigurationHolder PISTON_PHYSICS = new ConfigurationHolder(true, "physics", "piston");
	public static final ConfigurationHolder REDSTONE_PHYSICS = new ConfigurationHolder(true, "physics", "redstone");
	public static final ConfigurationHolder SAND_PHYSICS = new ConfigurationHolder(true, "physics", "sand");
	public static final ConfigurationHolder CACTUS_PHYSICS = new ConfigurationHolder(true, "physics", "cactus");
	public static final ConfigurationHolder LAVA_PHYSICS = new ConfigurationHolder(true, "physics", "lava", "enabled");
	public static final ConfigurationHolder LAVA_DELAY = new ConfigurationHolder(1500, "physics", "lava", "delay");
	public static final ConfigurationHolder WATER_PHYSICS = new ConfigurationHolder(true, "physics", "water", "enabled");
	public static final ConfigurationHolder WATER_DELAY = new ConfigurationHolder(250, "physics", "water", "delay");
	// Player
	public static final ConfigurationHolder PLAYER_PVP_ENABLED = new ConfigurationHolder(true, "player", "pvp-enabled");
	public static final ConfigurationHolder PLAYER_SURVIVAL_ENABLE_HEALTH = new ConfigurationHolder(true, "player", "survival", "enable-health");
	public static final ConfigurationHolder PLAYER_SURVIVAL_ENABLE_HUNGER = new ConfigurationHolder(true, "player", "survival", "enable-hunger");
	public static final ConfigurationHolder PLAYER_SURVIVAL_ENABLE_XP = new ConfigurationHolder(true, "player", "survival", "enable-xp");
	public static final ConfigurationHolder PLAYER_TIMEOUT_MS = new ConfigurationHolder(5000, "player", "timeout-ms");
	public static final ConfigurationHolder PLAYER_SPEEDMINING_PREVENTION_ENABLED = new ConfigurationHolder(true, "player", "speedmining-prevention-enabled");
	public static final ConfigurationHolder PLAYER_SPEEDMINING_PREVENTION_ALLOWANCE = new ConfigurationHolder(5, "player", "speedmining-prevention-allowance");
	public static final ConfigurationHolder PLAYER_SPEEDMINING_PREVENTION_PERIOD = new ConfigurationHolder(50, "player", "speedmining-prevention-period");
	// Controller-specific
	public static final ConfigurationHolder ITEM_PICKUP_RANGE = new ConfigurationHolder(2, "controller", "item-pickup-range");
	// Redstone-specific
	public static final ConfigurationHolder REDSTONE_MIN_RANGE = new ConfigurationHolder(0, "redstone", "redstone-min-power-range");
	public static final ConfigurationHolder REDSTONE_MAX_RANGE = new ConfigurationHolder(15, "redstone", "redstone-max-power-range");
	public static final OpConfiguration OPS = new OpConfiguration(VanillaPlugin.getInstance().getDataFolder());
	public static final WorldConfiguration WORLDS = new WorldConfiguration(VanillaPlugin.getInstance().getDataFolder());
	public static final BiomeConfiguration BIOMES = new BiomeConfiguration(VanillaPlugin.getInstance().getDataFolder());
	// Encryption
	public static final ConfigurationHolder ENCRYPT_KEY_ALGORITHM = new ConfigurationHolder("RSA", "encrypt", "key-algorithm");
	public static final ConfigurationHolder ENCRYPT_KEY_SIZE = new ConfigurationHolder(1024, "encrypt", "key-size");
	public static final ConfigurationHolder ENCRYPT_KEY_PADDING = new ConfigurationHolder("PKCS1", "encrypt", "key-padding");
	public static final ConfigurationHolder ENCRYPT_STREAM_ALGORITHM = new ConfigurationHolder("AES", "encrypt", "stream-algorithm");
	public static final ConfigurationHolder ENCRYPT_STREAM_WRAPPER = new ConfigurationHolder("CFB8", "encrypt", "stream-wrapper");


	public VanillaConfiguration(File dataFolder) {
		super(new YamlConfiguration(new File(dataFolder, "config.yml")));
	}

	@Override
	public void load() throws ConfigurationException {
		BIOMES.load();
		BIOMES.save();
		OPS.load();
		OPS.save();
		WORLDS.load();
		WORLDS.save();
		super.load();
		super.save();
	}

	@Override
	public void save() throws ConfigurationException {
		BIOMES.save();
		OPS.save();
		WORLDS.save();
		super.save();
	}
}
