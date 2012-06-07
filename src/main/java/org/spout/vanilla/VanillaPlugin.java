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
package org.spout.vanilla;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.logging.Level;

import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.entity.component.controller.ControllerType;
import org.spout.api.entity.component.controller.basic.PointObserver;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.protocol.Protocol;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.security.SecurityHandler;

import org.spout.vanilla.command.AdministrationCommands;
import org.spout.vanilla.command.TestCommands;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.configuration.WorldConfiguration;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.bootstrap.VanillaBootstrapProtocol;
import org.spout.vanilla.task.TimeTask;
import org.spout.vanilla.task.WeatherTask;
import org.spout.vanilla.world.generator.flat.FlatGenerator;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class VanillaPlugin extends CommonPlugin {
	public static final int MINECRAFT_PROTOCOL_ID = 29;
	public static final int VANILLA_PROTOCOL_ID = ControllerType.getProtocolId("org.spout.vanilla.protocol");
	private static VanillaPlugin instance;
	private Engine game;
	private VanillaConfiguration config;

	@Override
	public void onDisable() {
		try {
			config.save();
		} catch (ConfigurationException e) {
			getLogger().log(Level.WARNING, "Error saving Vanilla configuration: ", e);
		}
		instance = null;
		getLogger().info("disabled");
	}

	@Override
	public void onEnable() {
		//Config
		try {
			config.load();
		} catch (ConfigurationException e) {
			getLogger().log(Level.WARNING, "Error loading Vanilla configuration: ", e);
		}
		//Commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		game.getRootCommand().addSubCommands(this, AdministrationCommands.class, commandRegFactory);
		if (game.debugMode()) {
			game.getRootCommand().addSubCommands(this, TestCommands.class, commandRegFactory);
		}

		//Configuration
		VanillaBlockMaterial.REDSTONE_POWER_MAX = (short) VanillaConfiguration.REDSTONE_MAX_RANGE.getInt();
		VanillaBlockMaterial.REDSTONE_POWER_MIN = (short) VanillaConfiguration.REDSTONE_MIN_RANGE.getInt();

		//Events
		game.getEventManager().registerEvents(new VanillaListener(this), this);

		//Worlds
		setupWorlds();
		
		SecurityHandler.getInstance().getKeyPair(1024, "RSA");
		
		KeyPair keys = SecurityHandler.getInstance().getKeyPair(1024, "RSA");

		if (Spout.debugMode()) {
			Spout.multilineLog("Key pair generated");
			Spout.multilineLog("<<<<<<<<<<  Public   >>>>>>>>>>");
			Spout.multilineLog(keys.getPublic().toString());
		
			Spout.multilineLog("");
			Spout.multilineLog("<<<<<<<<<<  Private  >>>>>>>>>>");
			Spout.multilineLog(keys.getPrivate().toString());
		}

		getLogger().info("v" + getDescription().getVersion() + " enabled. Protocol: " + getDescription().getData("protocol").get());
	}

	@Override
	public void onLoad() {
		instance = this;
		game = getGame();
		config = new VanillaConfiguration(getDataFolder());
		Protocol.registerProtocol("VanillaProtocol", new VanillaProtocol());

		if (game instanceof Server) {
			int port = 25565;
			String[] split = game.getAddress().split(":");
			if (split.length > 1) {
				try {
					port = Integer.parseInt(split[1]);
				} catch (NumberFormatException e) {
					getLogger().warning(split[1] + " is not a valid port number! Defaulting to " + port + "!");
				}
			}

			((Server) game).bind(new InetSocketAddress(split[0], port), new VanillaBootstrapProtocol());
		}
		//TODO if (game instanceof Client) do stuff?

		VanillaMaterials.initialize();
		getLogger().info("loaded");
	}

	/**
	 * Gets the running instance of VanillaPlugin
	 * @return
	 */
	public static VanillaPlugin getInstance() {
		return instance;
	}

	private void setupWorlds() {
		ArrayList<World> worlds = new ArrayList<World>();

		if (WorldConfiguration.NORMAL_LOAD.getBoolean()) {
			NormalGenerator normGen = new NormalGenerator();
			World normal = game.loadWorld(WorldConfiguration.NORMAL_NAME.getString(), normGen);
			normal.getDataMap().put(VanillaData.GAMEMODE, GameMode.valueOf(WorldConfiguration.NORMAL_GAMEMODE.getString().toUpperCase()));
			normal.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.valueOf(WorldConfiguration.NORMAL_DIFFICULTY.getString().toUpperCase()));
			normal.getDataMap().put(VanillaData.DIMENSION, Dimension.valueOf(WorldConfiguration.NORMAL_SKY_TYPE.getString().toUpperCase()));
			if (normal.getAge() <= 0) {
				normal.setSpawnPoint(new Transform(new Point(normGen.getSafeSpawn(normal)), Quaternion.IDENTITY, Vector3.ONE));
			}
			if (WorldConfiguration.NORMAL_LOADED_SPAWN.getBoolean()) {
				normal.createAndSpawnEntity(normal.getSpawnPoint().getPosition(), new PointObserver());
			}
			Spout.getEngine().getScheduler().scheduleSyncRepeatingTask(this, new TimeTask(normal, normal.getDataMap().get(VanillaData.TIME), WorldConfiguration.NORMAL_TIME_MODIFIER.getInt()), 0, 0, TaskPriority.NORMAL);
			Spout.getEngine().getScheduler().scheduleSyncRepeatingTask(this, new WeatherTask(normal, normal.getDataMap().get(VanillaData.WEATHER), 5, 5), 0, 0, TaskPriority.NORMAL);
			worlds.add(normal);
		}

		if (WorldConfiguration.FLAT_LOAD.getBoolean()) {
			FlatGenerator flatGen = new FlatGenerator(64);
			World flat = game.loadWorld(WorldConfiguration.FLAT_NAME.getString(), flatGen);
			flat.getDataMap().put(VanillaData.GAMEMODE, GameMode.valueOf(WorldConfiguration.FLAT_GAMEMODE.getString().toUpperCase()));
			flat.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.valueOf(WorldConfiguration.FLAT_DIFFICULTY.getString().toUpperCase()));
			flat.getDataMap().put(VanillaData.DIMENSION, Dimension.valueOf(WorldConfiguration.FLAT_SKY_TYPE.getString().toUpperCase()));
			if (flat.getAge() <= 0) {
				flat.setSpawnPoint(new Transform(new Point(flatGen.getSafeSpawn(flat)), Quaternion.IDENTITY, Vector3.ONE));
			}
			if (WorldConfiguration.FLAT_LOADED_SPAWN.getBoolean()) {
				flat.createAndSpawnEntity(flat.getSpawnPoint().getPosition(), new PointObserver());
			}
			Spout.getEngine().getScheduler().scheduleSyncRepeatingTask(this, new TimeTask(flat, flat.getDataMap().get(VanillaData.TIME), WorldConfiguration.FLAT_TIME_MODIFIER.getInt()), 0, 0, TaskPriority.NORMAL);
			Spout.getEngine().getScheduler().scheduleSyncRepeatingTask(this, new WeatherTask(flat, flat.getDataMap().get(VanillaData.WEATHER), 5, 5), 0, 0, TaskPriority.NORMAL);
			worlds.add(flat);
		}

		if (WorldConfiguration.NETHER_LOAD.getBoolean()) {
			NetherGenerator netherGen = new NetherGenerator();
			World nether = game.loadWorld(WorldConfiguration.NETHER_NAME.getString(), netherGen);
			nether.getDataMap().put(VanillaData.GAMEMODE, GameMode.valueOf(WorldConfiguration.NETHER_GAMEMODE.getString().toUpperCase()));
			nether.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.valueOf(WorldConfiguration.NETHER_DIFFICULTY.getString().toUpperCase()));
			nether.getDataMap().put(VanillaData.DIMENSION, Dimension.valueOf(WorldConfiguration.NETHER_SKY_TYPE.getString().toUpperCase()));
			if (nether.getAge() <= 0) {
				nether.setSpawnPoint(new Transform(new Point(netherGen.getSafeSpawn(nether)), Quaternion.IDENTITY, Vector3.ONE));
			}
			if (WorldConfiguration.NETHER_LOADED_SPAWN.getBoolean()) {
				nether.createAndSpawnEntity(nether.getSpawnPoint().getPosition(), new PointObserver());
			}
			worlds.add(nether);
		}

		if (WorldConfiguration.END_LOAD.getBoolean()) {
			TheEndGenerator endGen = new TheEndGenerator();
			World end = game.loadWorld(WorldConfiguration.END_NAME.getString(), endGen);
			end.getDataMap().put(VanillaData.GAMEMODE, GameMode.valueOf(WorldConfiguration.END_GAMEMODE.getString().toUpperCase()));
			end.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.valueOf(WorldConfiguration.END_DIFFICULTY.getString().toUpperCase()));
			end.getDataMap().put(VanillaData.DIMENSION, Dimension.valueOf(WorldConfiguration.END_SKY_TYPE.getString().toUpperCase()));
			if (end.getAge() <= 0) {
				end.setSpawnPoint(new Transform(new Point(endGen.getSafeSpawn(end)), Quaternion.IDENTITY, Vector3.ONE));
			}
			if (WorldConfiguration.END_LOADED_SPAWN.getBoolean()) {
				end.createAndSpawnEntity(end.getSpawnPoint().getPosition(), new PointObserver());
			}
			worlds.add(end);
		}

		final int diameter = PointObserver.CHUNK_VIEW_DISTANCE + PointObserver.CHUNK_VIEW_DISTANCE + 1;
		final int total = diameter * diameter * diameter;
		final int progressStep = total / 10;
		for (World world : worlds) {
			int progress = 0;
			Point point = world.getSpawnPoint().getPosition();
			int cx = point.getBlockX() >> Chunk.BLOCKS.BITS;
			int cy = point.getBlockY() >> Chunk.BLOCKS.BITS;
			int cz = point.getBlockZ() >> Chunk.BLOCKS.BITS;
			for (int dx = -PointObserver.CHUNK_VIEW_DISTANCE; dx <= PointObserver.CHUNK_VIEW_DISTANCE; dx++) {
				for (int dy = -PointObserver.CHUNK_VIEW_DISTANCE; dy <= PointObserver.CHUNK_VIEW_DISTANCE; dy++) {
					for (int dz = -PointObserver.CHUNK_VIEW_DISTANCE; dz <= PointObserver.CHUNK_VIEW_DISTANCE; dz++) {
						progress++;
						if (progress % progressStep == 0) {
							Spout.getLogger().info("Loading [" + world.getName() + "], " + (progress / progressStep) * 10 + "% Complete");
						}
						world.getChunk(cx + dx, cy + dy, cz + dz);
					}
				}
			}
		}
	}
}
