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
package org.spout.vanilla;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.spout.api.Client;
import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.component.impl.NetworkComponent;
import org.spout.api.component.impl.ObserverComponent;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.input.InputManager;
import org.spout.api.input.Keyboard;
import org.spout.api.input.Mouse;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.model.Model;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.Platform;
import org.spout.api.plugin.ServiceManager;
import org.spout.api.plugin.services.ProtectionService;
import org.spout.api.protocol.Protocol;
import org.spout.api.util.OutwardIterator;

import org.spout.vanilla.command.AdministrationCommands;
import org.spout.vanilla.command.InputCommandExecutor;
import org.spout.vanilla.command.TestCommands;
import org.spout.vanilla.component.world.sky.NetherSky;
import org.spout.vanilla.component.world.sky.NormalSky;
import org.spout.vanilla.component.world.sky.TheEndSky;
import org.spout.vanilla.configuration.InputConfiguration;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.configuration.WorldConfigurationNode;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.Dimension;
import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.recipe.VanillaRecipes;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.LANThread;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.rcon.RemoteConnectionCore;
import org.spout.vanilla.protocol.rcon.RemoteConnectionServer;
import org.spout.vanilla.render.SkyRenderEffect;
import org.spout.vanilla.render.VanillaEffects;
import org.spout.vanilla.resources.MapPalette;
import org.spout.vanilla.resources.RecipeYaml;
import org.spout.vanilla.resources.loader.MapPaletteLoader;
import org.spout.vanilla.resources.loader.RecipeLoader;
import org.spout.vanilla.service.VanillaProtectionService;
import org.spout.vanilla.service.protection.SpawnProtection;
import org.spout.vanilla.thread.SpawnLoaderThread;
import org.spout.vanilla.world.generator.VanillaGenerator;
import org.spout.vanilla.world.generator.VanillaGenerators;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class VanillaPlugin extends CommonPlugin {
	private static final int LOADER_THREAD_COUNT = 16;
	public static final int MINECRAFT_PROTOCOL_ID = 51;
	public static final int VANILLA_PROTOCOL_ID = NetworkComponent.getProtocolId("org.spout.vanilla.protocol");
	private static VanillaPlugin instance;
	private Engine engine;
	private VanillaConfiguration config;
	private RemoteConnectionCore rcon;

	@Override
	public void onDisable() {
		instance = null;
		getLogger().info("disabled");
	}

	@Override
	public void onEnable() {

		//Config
		config.load();

		//Commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		engine.getRootCommand().addSubCommands(this, AdministrationCommands.class, commandRegFactory);
		if (engine.debugMode()) {
			engine.getRootCommand().addSubCommands(this, TestCommands.class, commandRegFactory);
		}
		getEngine().getEventManager().registerEvents(new VanillaListener(this), this);

		InputCommandExecutor exe = new InputCommandExecutor();
		engine.getRootCommand().addSubCommand(this, "+toggle_inventory").setArgBounds(0, 0).setHelp("Opens or closes the player's inventory.")
				.setExecutor(Platform.CLIENT, exe);
		engine.getRootCommand().addSubCommand(this, "+break_block").setArgBounds(0, 0).setHelp("Breaks a block!")
				.setExecutor(Platform.CLIENT, exe);
		engine.getRootCommand().addSubCommand(this, "+select_block").setArgBounds(0, 0).setHelp("Selects a block!")
				.setExecutor(Platform.CLIENT, exe);
		engine.getRootCommand().addSubCommand(this, "+place_block").setArgBounds(0, 0).setHelp("Places a block!")
				.setExecutor(Platform.CLIENT, exe);

		if (Spout.getPlatform() == Platform.CLIENT) {
			InputManager input = ((Client) Spout.getEngine()).getInputManager();
			input.bind(Keyboard.get(InputConfiguration.TOGGLE_INVENTORY.getString()), "toggle_inventory");
			input.bind(Mouse.MOUSE_BUTTON0, "break_block");
			input.bind(Mouse.MOUSE_BUTTON1, "place_block");
			input.bind(Mouse.MOUSE_BUTTON2, "select_block");
		}

		//Configuration
		VanillaBlockMaterial.REDSTONE_POWER_MAX = (short) VanillaConfiguration.REDSTONE_MAX_RANGE.getInt();
		VanillaBlockMaterial.REDSTONE_POWER_MIN = (short) VanillaConfiguration.REDSTONE_MIN_RANGE.getInt();

		if (engine.debugMode() || engine.getPlatform() == Platform.SERVER) {
			//Worlds
			setupWorlds();
		}
		if (Spout.getPlatform() == Platform.CLIENT) {
			System.out.println("Loading Skydome");
			Model m = (Model) Spout.getFilesystem().getResource("model://Vanilla/materials/sky/skydome.spm");
			m.getRenderMaterial().addRenderEffect(VanillaEffects.SKY);
			System.out.println("Loaded Skydome");
			Spout.getEngine().getWorld("world").getDataMap().put("Skydome", m);
		}

		if (engine instanceof Server && VanillaConfiguration.LAN_DISCOVERY.getBoolean()) {
			LANThread lanThread = new LANThread();
			lanThread.start();
		}

		getLogger().info("v" + getDescription().getVersion() + " enabled. Protocol: " + getDescription().getData("protocol"));
	}

	@Override
	public void onLoad() {
		instance = this;
		engine = getEngine();
		config = new VanillaConfiguration(getDataFolder());
		Spout.getFilesystem().registerLoader(new MapPaletteLoader());
		Spout.getFilesystem().registerLoader(new RecipeLoader());
		Protocol.registerProtocol(new VanillaProtocol());

		VanillaMaterials.initialize();
		MapPalette.DEFAULT = (MapPalette) Spout.getFilesystem().getResource("mappalette://Vanilla/map/mapColorPalette.dat");
		RecipeYaml.DEFAULT = (RecipeYaml) Spout.getFilesystem().getResource("recipe://Vanilla/recipes.yml");
		VanillaRecipes.initialize();

		getLogger().info("loaded");
	}

	private void setupRcon() {
		if (engine.getPlatform() == Platform.SERVER) {
			RemoteConnectionServer server = new RemoteConnectionServer(getLogger(), getDataFolder());
			server.bindDefaultPorts((Server) getEngine());
			rcon = server;
		}
	}

	private void closeRcon() {
		getEngine().getLogger().info("Shutting down rcon connections");
		if (rcon != null) {
			try {
				rcon.close();
			} catch (IOException e) {
				getLogger().log(Level.SEVERE, "Error closing RCON channels: ", e);
			}
		}
	}

	private void setupWorlds() {
		ArrayList<World> worlds = new ArrayList<World>();

		for (WorldConfigurationNode worldNode : VanillaConfiguration.WORLDS.getAll()) {
			if (worldNode.LOAD.getBoolean()) {
				// Obtain generator and start generating world
				String generatorName = worldNode.GENERATOR.getString();
				VanillaGenerator generator = VanillaGenerators.byName(generatorName);
				if (generator == null) {
					throw new IllegalArgumentException("Invalid generator name for world '" + worldNode.getWorldName() + "': " + generatorName);
				}
				World world = engine.loadWorld(worldNode.getWorldName(), generator);

				// Apply general settings
				world.getDataMap().put(VanillaData.GAMEMODE, GameMode.get(worldNode.GAMEMODE.getString()));
				world.getDataMap().put(VanillaData.DIFFICULTY, Difficulty.get(worldNode.DIFFICULTY.getString()));
				world.getDataMap().put(VanillaData.DIMENSION, Dimension.get(worldNode.SKY_TYPE.getString()));

				// Grab safe spawn if newly created world.
				if (world.getAge() <= 0) {
					world.setSpawnPoint(new Transform(new Point(generator.getSafeSpawn(world)), Quaternion.IDENTITY, Vector3.ONE));
				}

				// Add to worlds
				worlds.add(world);
			}
		}

		final int radius = VanillaConfiguration.SPAWN_RADIUS.getInt();
		final int protectionRadius = VanillaConfiguration.SPAWN_PROTECTION_RADIUS.getInt();
		final int diameter = (radius << 1) + 1;
		final int total = (diameter * diameter * diameter) / 6;
		final int progressStep = total / 10;
		final OutwardIterator oi = new OutwardIterator();
		SpawnLoaderThread[] loaderThreads = new SpawnLoaderThread[LOADER_THREAD_COUNT];

		if (worlds.isEmpty()) {
			return;
		}
		//Register protection service used for spawn protection.
		engine.getServiceManager().register(ProtectionService.class, new VanillaProtectionService(), this, ServiceManager.ServicePriority.Highest);

		for (World world : worlds) {
			// Keep spawn loaded
			WorldConfigurationNode worldConfig = VanillaConfiguration.WORLDS.get(world);
			if (worldConfig.LOADED_SPAWN.getBoolean()) {
				// Initialize the first chunks
				Point point = world.getSpawnPoint().getPosition();
				int cx = point.getBlockX() >> Chunk.BLOCKS.BITS;
				int cy = point.getBlockY() >> Chunk.BLOCKS.BITS;
				int cz = point.getBlockZ() >> Chunk.BLOCKS.BITS;

				((VanillaProtectionService) engine.getServiceManager().getRegistration(ProtectionService.class).getProvider()).addProtection(new SpawnProtection(world.getName() + " Spawn Protection", world, point, protectionRadius));

				final String initChunkType = world.getAge() <= 0 ? "Generating" : "Loading";

				for (int i = 0; i < LOADER_THREAD_COUNT; i++) {
					loaderThreads[i] = new SpawnLoaderThread(total, progressStep, initChunkType);
				}

				oi.reset(cx, cy, cz, radius);
				while (oi.hasNext()) {
					IntVector3 v = oi.next();
					SpawnLoaderThread.addChunk(world, v.getX(), v.getY(), v.getZ());
				}

				for (int i = 0; i < LOADER_THREAD_COUNT; i++) {
					loaderThreads[i].start();
				}

				for (int i = 0; i < LOADER_THREAD_COUNT; i++) {
					try {
						loaderThreads[i].join();
					} catch (InterruptedException ie) {
						getLogger().info("Interrupted when waiting for spawn area to load");
					}
				}
				world.createAndSpawnEntity(point, ObserverComponent.class, LoadOption.LOAD_GEN);
			}

			if (world.getGenerator() instanceof NetherGenerator) {
				world.getComponentHolder().add(NetherSky.class).setHasWeather(false);
			} else if (world.getGenerator() instanceof TheEndGenerator) {
				world.getComponentHolder().add(TheEndSky.class).setHasWeather(false);
			} else {
				world.getComponentHolder().add(NormalSky.class);
			}
		}
	}

	/**
	 * Gets the running instance of VanillaPlugin
	 * @return the running instance of VanillaPlugin
	 */
	public static VanillaPlugin getInstance() {
		return instance;
	}

	/**
	 * Gets the configuration instance for Vanilla
	 * @return the configuration instance
	 */
	public VanillaConfiguration getConfig() {
		return config;
	}
}
