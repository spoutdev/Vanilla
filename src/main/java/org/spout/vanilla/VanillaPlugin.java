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

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
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
import org.spout.api.component.components.NetworkComponent;
import org.spout.api.component.components.ObserverComponent;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.plugin.Platform;
import org.spout.api.plugin.Plugin;
import org.spout.api.plugin.ServiceManager;
import org.spout.api.plugin.services.ProtectionService;
import org.spout.api.protocol.PortBinding;
import org.spout.api.protocol.Protocol;
import org.spout.api.util.OutwardIterator;

import org.spout.vanilla.command.AdministrationCommands;
import org.spout.vanilla.command.TestCommands;
import org.spout.vanilla.component.world.VanillaSky;
import org.spout.vanilla.component.world.sky.NetherSky;
import org.spout.vanilla.component.world.sky.NormalSky;
import org.spout.vanilla.component.world.sky.TheEndSky;
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
	public static final int MINECRAFT_PROTOCOL_ID = 39;
	public static final int VANILLA_PROTOCOL_ID = NetworkComponent.getProtocolId("org.spout.vanilla.protocol");
	private static VanillaPlugin instance;
	private Engine engine;
	private VanillaConfiguration config;
	private JmDNS jmdns = null;
	private final Object jmdnsSync = new Object();
	private RemoteConnectionCore rcon;

	@Override
	public void onDisable() {
		closeBonjour();
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

		// Universal Plug and Play
		if (getEngine() instanceof Server) {
			for (PortBinding binding : ((Server) getEngine()).getBoundAddresses()) {
				if (binding.getProtocol() instanceof VanillaProtocol
						&& binding.getAddress() instanceof InetSocketAddress
						&& VanillaConfiguration.UPNP.getBoolean()) {
					((Server) getEngine()).mapUPnPPort(((InetSocketAddress) binding.getAddress()).getPort(), VanillaConfiguration.MOTD.getString());
				}
			}
		}

		//Commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(this), new SimpleAnnotatedCommandExecutorFactory());
		engine.getRootCommand().addSubCommands(this, AdministrationCommands.class, commandRegFactory);
		if (engine.debugMode()) {
			engine.getRootCommand().addSubCommands(this, TestCommands.class, commandRegFactory);
		}
		getEngine().getEventManager().registerEvents(new VanillaListener(this), this);

		//Configuration
		VanillaBlockMaterial.REDSTONE_POWER_MAX = (short) VanillaConfiguration.REDSTONE_MAX_RANGE.getInt();
		VanillaBlockMaterial.REDSTONE_POWER_MIN = (short) VanillaConfiguration.REDSTONE_MIN_RANGE.getInt();

		if (engine.debugMode() || engine.getPlatform() == Platform.SERVER) {
			//Worlds
			setupWorlds();
		}

		setupBonjour();

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
		MapPalette.DEFAULT = (MapPalette) Spout.getFilesystem().getResource("mappalette://Vanilla/resources/map/mapColorPalette.dat");
		RecipeYaml.DEFAULT = (RecipeYaml) Spout.getFilesystem().getResource("recipe://Vanilla/resources/recipes.yml");
		VanillaRecipes.initialize();

		getLogger().info("loaded");
	}

	private void setupBonjour() {
		if (getEngine() instanceof Server && VanillaConfiguration.BONJOUR.getBoolean()) {
			getEngine().getScheduler().scheduleAsyncTask(this, new Runnable() {
				public void run() {
					synchronized (jmdnsSync) {
						try {
							getEngine().getLogger().info("Starting Bonjour Service Discovery");
							jmdns = JmDNS.create();
							for (PortBinding binding : ((Server) getEngine()).getBoundAddresses()) {
								if (binding.getAddress() instanceof InetSocketAddress && binding.getProtocol() instanceof VanillaProtocol) {
									int port = ((InetSocketAddress) binding.getAddress()).getPort();
									ServiceInfo info = ServiceInfo.create("pipework._tcp.local.", "Spout Server", port, "");
									jmdns.registerService(info);
									getEngine().getLogger().info("Started Bonjour Service Discovery on port: " + port);
								}
							}
						} catch (IOException e) {
							getEngine().getLogger().log(Level.WARNING, "Failed to start Bonjour Service Discovery Library", e);
						}
					}
				}
			});
		}
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

	private void closeBonjour() {
		if (jmdns != null) {
			final JmDNS jmdns = this.jmdns;
			final Plugin thisPlugin = this;
			getEngine().getScheduler().scheduleAsyncTask(thisPlugin, new Runnable() {
				public void run() {
					synchronized (jmdnsSync) {
						getEngine().getLogger().info("Shutting down Bonjour Service Discovery");
						try {
							jmdns.close();
						} catch (IOException e) {
						}
					}
					getEngine().getLogger().info("Bonjour Service Discovery disabled");
				}
			});
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

			WorldConfigurationNode worldConfig = VanillaConfiguration.WORLDS.getOrCreate(world);

			// Keep spawn loaded
			if (worldConfig.LOADED_SPAWN.getBoolean()) {
				world.createAndSpawnEntity(point, ObserverComponent.class, LoadOption.LOAD_GEN);
			}

			if (world.getGenerator() instanceof NetherGenerator) {
				world.getComponentHolder().add(NetherSky.class).setHasWeather(false);
			} else if (world.getGenerator() instanceof TheEndGenerator) {
				world.getComponentHolder().add(TheEndSky.class).setHasWeather(false);
			} else {
				world.getComponentHolder().add(NormalSky.class);
			}
			VanillaSky sky = world.getComponentHolder().get(VanillaSky.class);
		}
	}

	/**
	 * Gets the running instance of VanillaPlugin
	 * @return the running instance of VanillaPlugin
	 */
	public static VanillaPlugin getInstance() {
		return instance;
	}
}
