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
package org.spout.vanilla;

import java.net.InetSocketAddress;
import java.util.logging.Level;

import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.exception.ConfigurationException;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.protocol.Protocol;

import org.spout.vanilla.command.AdministrationCommands;
import org.spout.vanilla.command.TestCommands;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.controller.world.PointObserver;
import org.spout.vanilla.controller.world.sky.NetherSky;
import org.spout.vanilla.controller.world.sky.NormalSky;
import org.spout.vanilla.controller.world.sky.TheEndSky;
import org.spout.vanilla.controller.world.sky.VanillaSky;
import org.spout.vanilla.inventory.recipe.VanillaRecipes;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.bootstrap.VanillaBootstrapProtocol;
import org.spout.vanilla.world.generator.flat.FlatGenerator;
import org.spout.vanilla.world.generator.nether.NetherGenerator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

/**
 * Vanilla - The Minecraft implementation for Spout.
 */
public class VanillaPlugin extends CommonPlugin {
	public static final int MINECRAFT_PROTOCOL_ID = 29;
	public static final int VANILLA_PROTOCOL_ID = ControllerType.getProtocolId("org.spout.vanilla.protocol");
	private Engine game;
	private VanillaConfiguration config;
	private VanillaRecipes recipes;
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
		game.getRootCommand().addSubCommands(this, AdministrationCommands.class, commandRegFactory);
		if (game.debugMode()) {
			game.getRootCommand().addSubCommands(this, TestCommands.class, commandRegFactory);
		}

		//Events
		game.getEventManager().registerEvents(new VanillaListener(this), this);

		//Worlds
		setupWorlds();

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
		recipes = new VanillaRecipes(this);
		getLogger().info("loaded");
	}

	/**
	 * Gets the Vanilla Plugin instance
	 */
	public static VanillaPlugin getInstance() {
		return instance;
	}

	private void setupWorlds() {
		//Initialize generators.
		NormalGenerator normGen = new NormalGenerator();
		FlatGenerator flatGen = new FlatGenerator(64);
		NetherGenerator netherGen = new NetherGenerator();
		TheEndGenerator endGen = new TheEndGenerator();

		//Load worlds
		World normal = game.loadWorld("world", normGen);
		World flat = game.loadWorld("world_flat", flatGen);
		World nether = game.loadWorld("world_nether", netherGen);
		World end = game.loadWorld("world_end", endGen);

		//Create the sky.
		NormalSky normSky = new NormalSky();
		NormalSky flatSky = new NormalSky();
		NetherSky netherSky = new NetherSky();
		TheEndSky endSky = new TheEndSky();

		//Register skies to the map
		VanillaSky.setSky(normal, normSky);
		VanillaSky.setSky(nether, netherSky);
		VanillaSky.setSky(end, endSky);
		VanillaSky.setSky(flat, flatSky);

		//Set the sky worlds
		normSky.setWorld(normal);
		flatSky.setWorld(flat);
		netherSky.setWorld(nether);
		endSky.setWorld(end);

		//Spawn points
		Point normalSpawn = normGen.getSafeSpawn(normal);
		Point flatSpawn = flatGen.getSafeSpawn(flat);
		Point netherSpawn = netherGen.getSafeSpawn(nether); //TODO Is this set based on the nether portal? Does the nether actually have a "spawn point"?
		Point endSpawn = endGen.getSafeSpawn(end); //TODO Needs to probably be set per end portal?

		final int diameter = PointObserver.CHUNK_VIEW_DISTANCE + PointObserver.CHUNK_VIEW_DISTANCE;
		final int total = diameter * diameter * diameter;
		final int progressStep = total / 10;

		World[] worlds = {normal, nether, end, flat};
		for (World world : worlds) {
			int progress = 0;
			for (int dx = -PointObserver.CHUNK_VIEW_DISTANCE; dx < PointObserver.CHUNK_VIEW_DISTANCE; dx++) {
				for (int dy = -PointObserver.CHUNK_VIEW_DISTANCE; dy < PointObserver.CHUNK_VIEW_DISTANCE; dy++) {
					for (int dz = -PointObserver.CHUNK_VIEW_DISTANCE; dz < PointObserver.CHUNK_VIEW_DISTANCE; dz++) {
						progress++;
						if (progress % progressStep == 0) {
							Spout.getLogger().info("Loading [" + world.getName() + "], " + (progress / progressStep) * 10 + "% Complete");
						}
						world.getChunk(dx, dy, dz, true);
					}
				}
			}
		}

		//Set world spawns, spawn the skies' entity, have spawn's observed by the point observer.
		normal.setSpawnPoint(new Transform(normalSpawn, Quaternion.IDENTITY, Vector3.ONE));
		normal.createAndSpawnEntity(new Point(normal, 0, 0, 0), normSky);
		normal.createAndSpawnEntity(normalSpawn, new PointObserver());

		flat.setSpawnPoint(new Transform(flatSpawn, Quaternion.IDENTITY, Vector3.ONE));
		flat.createAndSpawnEntity(new Point(flat, 0, 0, 0), flatSky);
		flat.createAndSpawnEntity(flatSpawn, new PointObserver());

		nether.setSpawnPoint(new Transform(netherSpawn, Quaternion.IDENTITY, Vector3.ONE));
		nether.createAndSpawnEntity(new Point(nether, 0, 0, 0), netherSky);
		nether.createAndSpawnEntity(netherSpawn, new PointObserver());

		end.setSpawnPoint(new Transform(endSpawn, Quaternion.IDENTITY, Vector3.ONE));
		end.createAndSpawnEntity(new Point(end, 0, 0, 0), endSky);
		end.createAndSpawnEntity(endSpawn, new PointObserver());
	}
}
