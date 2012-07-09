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
import java.util.ArrayList;
import java.util.logging.Level;

import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.entity.component.controller.basic.PointObserver;
import org.spout.api.entity.component.controller.type.ControllerType;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.Platform;
import org.spout.api.protocol.Protocol;
import org.spout.api.util.OutwardIterator;

import org.spout.vanilla.command.AdministrationCommands;
import org.spout.vanilla.command.TestCommands;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.configuration.WorldConfigurationNode;
import org.spout.vanilla.controller.world.VanillaSky;
import org.spout.vanilla.controller.world.sky.NetherSky;
import org.spout.vanilla.controller.world.sky.NormalSky;
import org.spout.vanilla.controller.world.sky.TheEndSky;
import org.spout.vanilla.data.Data;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.inventory.recipe.VanillaRecipes;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.bootstrap.VanillaBootstrapProtocol;
import org.spout.vanilla.resources.MapPalette;
import org.spout.vanilla.resources.RecipeYaml;
import org.spout.vanilla.resources.loader.MapPaletteLoader;
import org.spout.vanilla.resources.loader.RecipeLoader;
import org.spout.vanilla.world.generator.VanillaGenerator;
import org.spout.vanilla.world.generator.flat.FlatGenerator;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class VanillaPlugin extends CommonPlugin {
	public static final int MINECRAFT_PROTOCOL_ID = 29;
	public static final int VANILLA_PROTOCOL_ID = ControllerType.getProtocolId("org.spout.vanilla.protocol");
	private Engine engine;
	private VanillaConfiguration config;
	private static VanillaPlugin instance;

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
		engine.getRootCommand().addSubCommands(this, AdministrationCommands.class, commandRegFactory);
		if (engine.debugMode()) {
			engine.getRootCommand().addSubCommands(this, TestCommands.class, commandRegFactory);
		}

		//Configuration
		VanillaBlockMaterial.REDSTONE_POWER_MAX = (short) VanillaConfiguration.REDSTONE_MAX_RANGE.getInt();
		VanillaBlockMaterial.REDSTONE_POWER_MIN = (short) VanillaConfiguration.REDSTONE_MIN_RANGE.getInt();

		//Events
		engine.getEventManager().registerEvents(new VanillaListener(this), this);

		if (engine.getPlatform() == Platform.SERVER) {
			//Worlds
			setupWorlds();
		}

		getLogger().info("v" + getDescription().getVersion() + " enabled. Protocol: " + getDescription().getData("protocol").get());
	}

	@Override
	public void onLoad() {
		instance = this;
		engine = getEngine();
		config = new VanillaConfiguration(getDataFolder());
		Protocol.registerProtocol("VanillaProtocol", new VanillaProtocol());
		Spout.getFilesystem().registerLoader("mappalette", new MapPaletteLoader());
		Spout.getFilesystem().registerLoader("recipe", new RecipeLoader());

		if (engine.getPlatform() == Platform.SERVER || engine.getPlatform() == Platform.PROXY) {
			int port = 25565;
			String[] split = engine.getAddress().split(":");
			if (split.length > 1) {
				try {
					port = Integer.parseInt(split[1]);
				} catch (NumberFormatException e) {
					getLogger().warning(split[1] + " is not a valid port number! Defaulting to " + port + "!");
				}
			}

			((Server) engine).bind(new InetSocketAddress(split[0], port), new VanillaBootstrapProtocol());
		} else if (engine.getPlatform() == Platform.CLIENT) {
			//TODO if (engine instanceof Client) do stuff? | No, check engine.getPlatform()
		}

		VanillaMaterials.initialize();
		MapPalette.DEFAULT = (MapPalette) Spout.getFilesystem().getResource("mappalette://Vanilla/resources/map/mapColorPalette.dat");
		RecipeYaml.DEFAULT = (RecipeYaml) Spout.getFilesystem().getResource("recipe://Vanilla/resources/recipes.yml");
		VanillaRecipes.initialize();
		getLogger().info("loaded");
	}

	private void setupWorlds() {
		ArrayList<World> worlds = new ArrayList<World>();

		for (WorldConfigurationNode worldNode : VanillaConfiguration.WORLDS.getAll()) {
			if (worldNode.LOAD.getBoolean()) {
				// Obtain generator and start generating world
				String generatorName = worldNode.GENERATOR.getString();
				VanillaGenerator generator;
				if (generatorName.equalsIgnoreCase("normal")) {
					generator = new NormalGenerator();
				} else if (generatorName.equalsIgnoreCase("nether")) {
					generator = new NetherGenerator();
				} else if (generatorName.equalsIgnoreCase("flat")) {
					generator = new FlatGenerator(64);
				} else if (generatorName.equalsIgnoreCase("theend") || generatorName.equalsIgnoreCase("the_end")) {
					generator = new TheEndGenerator();
				} else {
					throw new IllegalArgumentException("Invalid generator name for world '" + worldNode.getWorldName() + "': " + generatorName);
				}
				World world = engine.loadWorld(worldNode.getWorldName(), generator);

				// Apply general settings
				world.getDataMap().put(Data.GAMEMODE, GameMode.valueOf(worldNode.GAMEMODE.getString().toUpperCase()));
				world.getDataMap().put(Data.DIFFICULTY, Difficulty.valueOf(worldNode.DIFFICULTY.getString().toUpperCase()));
				world.getDataMap().put(Data.DIMENSION, Dimension.valueOf(worldNode.SKY_TYPE.getString().toUpperCase()));

				// Grab safe spawn if newly created world.
				if (world.getAge() <= 0) {
					world.setSpawnPoint(new Transform(new Point(generator.getSafeSpawn(world)), Quaternion.IDENTITY, Vector3.ONE));
				}

				// Add to worlds
				worlds.add(world);
			}
		}

		final int radius = VanillaConfiguration.SPAWN_RADIUS.getInt();
		final int diameter = (radius << 1) + 1;
		final int total = (diameter * diameter * diameter) / 6;
		final int progressStep = total / 10;
		final OutwardIterator oi = new OutwardIterator();
		for (World world : worlds) {
			// Initialize the first chunks
			int progress = 0;
			Point point = world.getSpawnPoint().getPosition();
			int cx = point.getBlockX() >> Chunk.BLOCKS.BITS;
			int cy = point.getBlockY() >> Chunk.BLOCKS.BITS;
			int cz = point.getBlockZ() >> Chunk.BLOCKS.BITS;
			oi.reset(cx, cy, cz, radius);
			final String initChunkType = world.getAge() <= 0 ? "Generating" : "Loading";
			while (oi.hasNext()) {
				IntVector3 v = oi.next();
				progress++;
				if (progress % progressStep == 0) {
					Spout.getLogger().info(initChunkType + " [" + world.getName() + "], " + (progress / progressStep) * 10 + "% Complete");
				}
				world.getChunk(v.getX(), v.getY(), v.getZ());
			}

			WorldConfigurationNode worldConfig = VanillaConfiguration.WORLDS.getOrCreate(world);
			// Additional settings

			// Keep spawn loaded
			if (worldConfig.LOADED_SPAWN.getBoolean()) {
				world.createAndSpawnEntity(point, new PointObserver(radius));
			}

			//TODO Remove sky setting when Weather and Time are Region tasks.
			// Sky
			String skyType = worldConfig.SKY_TYPE.getString();
			VanillaSky sky;
			if (skyType.equalsIgnoreCase("normal")) {
				sky = new NormalSky();
			} else if (skyType.equalsIgnoreCase("nether")) {
				sky = new NetherSky();
			} else if (skyType.equalsIgnoreCase("the_end")) {
				sky = new TheEndSky();
			} else {
				throw new IllegalArgumentException("Invalid sky type for world '" + world.getName() + "': " + skyType);
			}
			world.createAndSpawnEntity(new Point(world, 0, 0, 0), sky);
		}
	}

	/**
	 * Gets the running instance of VanillaPlugin
	 * @return
	 */
	public static VanillaPlugin getInstance() {
		return instance;
	}
}
