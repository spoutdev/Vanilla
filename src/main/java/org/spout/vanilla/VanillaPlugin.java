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
package org.spout.vanilla;

import java.net.InetSocketAddress;
import java.util.HashMap;
import org.spout.api.Game;
import org.spout.api.Server;
import org.spout.api.command.CommandRegistrationsFactory;
import org.spout.api.command.annotated.AnnotatedCommandRegistrationFactory;
import org.spout.api.command.annotated.SimpleAnnotatedCommandExecutorFactory;
import org.spout.api.command.annotated.SimpleInjector;
import org.spout.api.entity.Controller;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;
import org.spout.api.plugin.CommonPlugin;
import org.spout.api.protocol.Protocol;
import org.spout.vanilla.command.AdministrationCommands;
import org.spout.vanilla.command.TestCommands;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.entity.sky.NetherSky;
import org.spout.vanilla.entity.sky.NormalSky;
import org.spout.vanilla.entity.sky.Sky;
import org.spout.vanilla.entity.sky.TheEndSky;
import org.spout.vanilla.generator.nether.NetherGenerator;
import org.spout.vanilla.generator.normal.NormalGenerator;
import org.spout.vanilla.generator.theend.TheEndGenerator;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.bootstrap.VanillaBootstrapProtocol;

public class VanillaPlugin extends CommonPlugin {
	public static final GameMode defaultGamemode = GameMode.SURVIVAL;
	public static final int minecraftProtocolId = 28;
	public static int vanillaProtocolId;
	public static World spawnWorld;

	private final VanillaConfiguration config;
	private final HashMap<World, Sky> skys = new HashMap<World, Sky>();
	private static VanillaPlugin instance;

	public VanillaPlugin() {
		instance = this;
		config = new VanillaConfiguration();
	}

	@Override
	public void onLoad() {
		// TODO - do we need a protocol manager ?
		// getGame().getProtocolManager().register ...
		Protocol.registerProtocol(0, new VanillaProtocol());

		Game game = getGame();

		if (game instanceof Server) {
			int port = 25565;
			String[] split = game.getAddress().split(":");
			if (split.length > 1) {
				try {
					port = Integer.parseInt(split[1]);
				} catch (NumberFormatException e) {
					System.out.println(split[1] + " is not a valid port number! Defaulting to " + port + "!");
				}
			}

			((Server) game).bind(new InetSocketAddress(split[0], port), new VanillaBootstrapProtocol());
		}

		VanillaMaterials.initialize();
		getLogger().info("loaded");
	}

	@Override
	public void onDisable() {
		config.save();
		getLogger().info("disabled");
	}

	@Override
	public void onEnable() {
		Game game = getGame();

		// IO
		config.load();

		//Register commands
		CommandRegistrationsFactory<Class<?>> commandRegFactory = new AnnotatedCommandRegistrationFactory(new SimpleInjector(), new SimpleAnnotatedCommandExecutorFactory());

		game.getRootCommand().addSubCommands(this, AdministrationCommands.class, commandRegFactory);
		game.getRootCommand().addSubCommands(this, TestCommands.class, commandRegFactory);

		//Register events
		game.getEventManager().registerEvents(new VanillaEventListener(this), this);

		vanillaProtocolId = Controller.getProtocolId("org.spout.vanilla.protocol");

		spawnWorld = game.loadWorld("world", new NormalGenerator());

		// TODO - Should probably be auto-set by generator and worlds should be defined in configuration.
		NormalSky normSky = new NormalSky(spawnWorld);
		skys.put(spawnWorld, normSky);
		spawnWorld.setSpawnPoint(new Transform(new Point(spawnWorld, 0.5F, 64.5F, 0.5F), Quaternion.identity, Vector3.ONE));
		spawnWorld.createAndSpawnEntity(new Point(spawnWorld, 0.f, 0.f, 0.f), normSky);

		World nether = getGame().loadWorld("world_nether", new NetherGenerator());
		nether.createAndSpawnEntity(new Point(nether, 0.f, 0.f, 0.f), new NetherSky());

		World end = getGame().loadWorld("world_end", new TheEndGenerator());
		end.createAndSpawnEntity(new Point(end, 0.f, 0.f, 0.f), new TheEndSky());

		getLogger().info("b" + this.getDescription().getVersion() + " enabled. Protocol: " + getDescription().getProtocol());
	}

	public static VanillaPlugin getInstance() {
		return instance;
	}

	public VanillaConfiguration getConfig() {
		return config;
	}

	public Sky getSky(World world) {
		return skys.get(world);
	}
}
