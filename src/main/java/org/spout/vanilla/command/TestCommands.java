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
package org.spout.vanilla.command;

import java.util.List;

import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.command.annotated.CommandPermissions;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.components.basic.HealthComponent;
import org.spout.vanilla.components.player.VanillaPlayer;
import org.spout.vanilla.util.explosion.ExplosionModels;
import org.spout.vanilla.world.generator.object.RandomizableObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public class TestCommands {
	@SuppressWarnings("unused")
	private final VanillaPlugin plugin;

	public TestCommands(VanillaPlugin instance) {
		plugin = instance;
	}

	@Command(aliases = {"explode"}, usage = "<explode>", desc = "Create an explosion")
	@CommandPermissions("vanilla.command.debug")
	public void explode(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to cause an explosion");
		}

		Entity entity = (Player) source;
		Point position = entity.getTransform().getPosition();

		ExplosionModels.SPHERICAL.execute(position, 4.0f);
	}

	@Command(aliases = {"tpworld", "tpw"}, usage = "<world name>", desc = "Teleport to a world's spawn.", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void tpWorld(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to teleport");
		}
		final Player player = (Player) source;
		final World world = args.getWorld(0);
		if (world != null) {
			final Point loc = world.getSpawnPoint().getPosition();
			world.getChunkFromBlock(loc);
			player.getTransform().setPosition(loc);
			player.getNetworkSynchronizer().setPositionDirty();
		} else {
			throw new CommandException("Please enter a valid world");
		}
	}

	@Command(aliases = {"spawn"}, usage = "<spiral or disk> <number> <controller> ... <number> <controller>", desc = "Spawn up to 50 controllers!", min = 1, max = 10)
	@CommandPermissions("vanilla.command.debug")
	public void spawn(CommandContext args, CommandSource source) throws CommandException {

	}

	@Command(aliases = {"tppos"}, usage = "<name> <world> <x> <y> <z>", desc = "Teleport to coordinates!", min = 5, max = 5)
	@CommandPermissions("vanilla.command.debug")
	public void tppos(CommandContext args, CommandSource source) throws CommandException {
		Player player = Spout.getEngine().getPlayer(args.getString(0), true);
		if (!(source instanceof Player) && player == null) {
			throw new CommandException("Must specify a valid player to tppos from the console.");
		}

		World world = Spout.getEngine().getWorld(args.getString(1));
		//If the source of the command is a player and they do not provide a valid player...teleport the source instead.
		if (player == null) {
			player = (Player) source;
		}

		if (world != null) {
			Point loc = new Point(world, args.getInteger(2), args.getInteger(3), args.getInteger(4));
			//Make sure the chunk the player is teleported to is loaded.
			world.getChunkFromBlock(loc);
			player.getTransform().setPosition(loc);
			player.getNetworkSynchronizer().setPositionDirty();
		} else {
			throw new CommandException("Please enter a valid world");
		}
	}

	@Command(aliases = {"object", "obj"}, usage = "<name>", flags = "f", desc = "Spawn a WorldGeneratorObject at your location. Use -f to ignore canPlace check", min = 1, max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void generateObject(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("The source must be a player.");
		}
		final WorldGeneratorObject object = VanillaObjects.byName(args.getString(0));
		if (object == null) {
			throw new CommandException("Invalid object name.");
		}
		final Player player = (Player) source;
		final Point loc = player.getTransform().getPosition();
		final World world = loc.getWorld();
		final int x = loc.getBlockX();
		final int y = loc.getBlockY();
		final int z = loc.getBlockZ();
		final boolean force = args.hasFlag('f');
		if (!object.canPlaceObject(world, x, y, z)) {
			player.sendMessage("Couldn't place the object.");
			if (!force) {
				return;
			}
			player.sendMessage("Forcing placement.");
		}
		object.placeObject(world, x, y, z);
		if (object instanceof RandomizableObject) {
			((RandomizableObject) object).randomize();
		}
	}

	@Command(aliases = {"block"}, desc = "Checks if the block below you has an entity", min = 0, max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void checkBlock(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Source must be player");
		}

		Player player = (Player) source;
		Block block = player.getWorld().getBlock(player.getTransform().getPosition().subtract(0, 1, 0), player);
	}

	@Command(aliases = {"killall", "ka"}, desc = "Kill all non-player or world entities within a world", min = 0, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void killall(CommandContext args, CommandSource source) throws CommandException {
		World world = null;
		boolean isConsole = false;

		if (!(source instanceof Player)) {
			if (args.length() == 0) {
				throw new CommandException("Need to provide a world when executing from the console");
			}
			String name = args.getString(0);
			world = Spout.getEngine().getWorld(name);
			isConsole = true;
		}
		if (world == null && isConsole) {
			throw new CommandException("World specified is not loaded");
		}
		if (world == null) {
			world = ((Player) source).getWorld();
		}
		List<Entity> entities = world.getAll();
		int count = 0;
		for (Entity entity : entities) {
			if (entity.getController() instanceof PlayerController || (!(entity.getController() instanceof VanillaEntityController))) {
				continue;
			}
			count++;
			entity.get(HealthComponent.class).kill(HealthChangeReason.COMMAND);
			Spout.log(entity.getController().toString() + " was killed");
		}
		if (count > 0) {
			if (!isConsole) {
				source.sendMessage(count, " entity(es) have been killed. The console has a listing of what controllers were killed.");
			}
		} else {
			source.sendMessage("No valid entities found to kill");
		}
	}

	@Command(aliases = "rollcredits", desc = "Rolls the end credits for the game.", min = 0, max = 0)
	@CommandPermissions("vanilla.command.debug")
	public void rollCredits(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to view the credits.");
		}
		Player p = (Player) source;
		if (p.has(VanillaPlayer.class)) {
			p.get(VanillaPlayer.class).rollCredits();
		}
	}

	@Command(aliases = "speed", desc = "Applies speed", min = 2, max = 2)
	@CommandPermissions("vanilla.command.debug")
	public void speed(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to apply speed");
		}

		Controller controller = ((Player) source).getController();
		controller.addComponent(new Speed((VanillaPlayerController) controller, args.getInteger(0), args.getInteger(1)));
	}

	@Command(aliases = "npc", desc = "Spawns an npc at your location", min = 1, max = 1)
	@CommandPermissions("vanilla.command.debug")
	public void npc(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only a player may spawn an npc");
		}
	}
}
