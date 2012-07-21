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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.Spout;
import org.spout.api.chat.style.ChatStyle;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Entity;
import org.spout.api.entity.component.Controller;
import org.spout.api.entity.component.controller.BlockController;
import org.spout.api.entity.component.controller.PlayerController;
import org.spout.api.entity.component.controller.type.ControllerRegistry;
import org.spout.api.entity.component.controller.type.ControllerType;
import org.spout.api.entity.spawn.DiscSpawnArrangement;
import org.spout.api.entity.spawn.SpawnArrangement;
import org.spout.api.entity.spawn.SpiralSpawnArrangement;
import org.spout.api.exception.CommandException;
import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.Human;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.effect.type.Speed;
import org.spout.vanilla.util.explosion.ExplosionModels;
import org.spout.vanilla.world.generator.object.RandomObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public class TestCommands {
	private final Set<String> invisible = new HashSet<String>();
	@SuppressWarnings("unused")
	private final VanillaPlugin plugin;

	public TestCommands(VanillaPlugin instance) {
		plugin = instance;
	}

	@Command(aliases = {"explode"}, usage = "<explode>", desc = "Create an explosion")
	public void explode(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to cause an explosion");
		}

		Entity entity = ((Player) source).getEntity();
		Point position = entity.getPosition();
		if (entity.getController() instanceof VanillaPlayer) {
			position = position.add(((VanillaPlayer) entity.getController()).getLookingAt());
		}

		ExplosionModels.SPHERICAL.execute(position, 4.0f);
	}

	@Command(aliases = {"spawn"}, usage = "<spiral or disk> <number> <controller> ... <number> <controller>", desc = "Spawn up to 50 controllers!", min = 1, max = 10)
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to spawn a controller");
		}

		Player player = (Player) source;
		Point point = player.getEntity().getPosition();

		boolean disk = false;

		ArrayList<ControllerType> types = new ArrayList<ControllerType>();
		ArrayList<Integer> numbers = new ArrayList<Integer>();

		for (int i = 0; i < args.length(); i++) {
			try {
				int number = Integer.parseInt(args.getString(i));
				while (numbers.size() <= types.size()) {
					numbers.add(1);
				}
				numbers.set(numbers.size() - 1, number);
			} catch (NumberFormatException e) {
				boolean match = false;
				String lookupType = args.getString(i).replaceAll("[_\\- ]", "");
				for (ControllerType testType : ControllerRegistry.getAll()) {
					if (testType.getName().replaceAll("[_\\- ]", "").equalsIgnoreCase(lookupType) && testType.canCreateController()) {
						while (numbers.size() <= types.size()) {
							numbers.add(1);
						}
						types.add(testType);
						match = true;
						;
						break;
					}
				}
				if (match) {
					continue;
				} else if (args.getString(i).equals("disk") || args.getString(i).equals("disc")) {
					disk = true;
				} else if (args.getString(i).equals("spiral")) {
					disk = false;
				} else {
					throw new CommandException("Unable to parse command argument " + args.getString(i));
				}
			}
		}

		if (types.size() == 0) {
			throw new CommandException("Unable to find any types to spawn");
		}

		int toSpawn = 0;

		for (int i = 0; i < types.size(); i++) {
			if (numbers.get(i) < 0) {
				source.sendMessage(ChatStyle.RED, "Increasing number of ", types.get(i).getName(), "s spawned to 0");
				numbers.set(i, 0);
			}
			toSpawn += numbers.get(i);
		}

		ControllerType[] typeArray = types.toArray(new ControllerType[0]);

		if (types.size() == 1) {
			typeArray = new ControllerType[]{types.get(0)};
		} else {
			typeArray = new ControllerType[toSpawn];
			int k = 0;
			for (int i = 0; i < types.size(); i++) {
				if (numbers.get(i) == 1) {
					source.sendMessage(ChatStyle.YELLOW, "Spawning a ", types.get(i).getName());
				} else {
					source.sendMessage(ChatStyle.YELLOW, "Spawning ", numbers.get(i) + " ", types.get(i).getName() + "s");
				}
				for (int j = 0; j < numbers.get(i); j++) {
					typeArray[k++] = types.get(i);
				}
			}
		}

		SpawnArrangement arrangement;
		if (types.size() == 1) {
			if (disk) {
				arrangement = new DiscSpawnArrangement(point, typeArray[0], numbers.get(0), 1.5F);
			} else {
				arrangement = new SpiralSpawnArrangement(point, typeArray[0], numbers.get(0), 1.5F);
			}
		} else {
			if (disk) {
				arrangement = new DiscSpawnArrangement(point, typeArray, 1.5F);
			} else {
				arrangement = new SpiralSpawnArrangement(point, typeArray, 1.5F);
			}
		}

		point.getWorld().createAndSpawnEntity(arrangement);
	}

	@Command(aliases = {"tppos"}, usage = "<name> <world> <x> <y> <z>", desc = "Teleport to coordinates!", min = 5, max = 5)
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
			player.getEntity().setPosition(loc);
			player.getNetworkSynchronizer().setPositionDirty();
		} else {
			throw new CommandException("Please enter a valid world");
		}
	}

	@Command(aliases = {"object", "obj"}, usage = "<name>", flags = "f", desc = "Spawn a WorldGeneratorObject at your location. Use -f to ignore canPlace check", min = 1, max = 2)
	public void spawnObject(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("The source must be a player.");
		}
		final Player player = (Player) source;
		final WorldGeneratorObject object = VanillaObjects.byName(args.getString(0));
		if (object == null) {
			player.sendMessage("Invalid object name.");
			return;
		}
		final Point loc = player.getEntity().getPosition();
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
		if (object instanceof RandomObject) {
			((RandomObject) object).randomize();
		}
	}

	@Command(aliases = {"block"}, desc = "Checks if the block below you has an entity", min = 0, max = 0)
	public void checkBlock(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Source must be player");
		}

		Player player = (Player) source;
		Entity playerEntity = player.getEntity();
		Block block = playerEntity.getWorld().getBlock(playerEntity.getPosition().subtract(0, 1, 0), playerEntity);
		if (!block.hasController()) {
			player.sendMessage("Block has no entity!");
			return;
		}

		BlockController controller = block.getController();
		player.sendMessage("Material: ", controller.getMaterial().getName());
	}

	@Command(aliases = {"vanish", "v"}, desc = "Toggle your visibility", min = 0, max = 0)
	public void vanish(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Source must be player");
		}

		Controller controller = ((Player) source).getEntity().getController();
		if (!(controller instanceof VanillaPlayer)) {
			throw new CommandException("Invalid controller");
		}

		VanillaPlayer vanillaPlayer = (VanillaPlayer) controller;
		Player player = vanillaPlayer.getPlayer();
		String name = player.getName();
		if (invisible.contains(name)) {
			invisible.remove(name);
			vanillaPlayer.setVisible(true);
			player.sendMessage("You re-appear");
		} else {
			invisible.add(name);
			vanillaPlayer.setVisible(false);
			player.sendMessage("You vanish into thin air!");
		}
	}

	@Command(aliases = {"killall", "ka"}, desc = "Kill all non-player or world entities within a world", min = 0, max = 1)
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
			world = ((Player) source).getEntity().getWorld();
		}
		Set<Entity> entities = world.getAll();
		int count = 0;
		for (Entity entity : entities) {
			if (entity.getController() instanceof PlayerController || (!(entity.getController() instanceof VanillaActionController))) {
				continue;
			}
			count++;
			((VanillaActionController) entity.getController()).setHealth(0, HealthChangeReason.COMMAND);
			entity.kill();
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
	public void rollCredits(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to view the credits.");
		}

		Controller controller = ((Player) source).getEntity().getController();
		if (controller instanceof VanillaPlayer) {
			((VanillaPlayer) controller).rollCredits();
		}
	}

	@Command(aliases = "speed", desc = "Applies speed", min = 2, max = 2)
	public void speed(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to apply speed");
		}

		Controller controller = ((Player) source).getEntity().getController();
		controller.registerProcess(new Speed((VanillaPlayer) controller, args.getInteger(0), args.getInteger(1)));
	}

	@Command(aliases = "npc", desc = "Spawns an npc at your location", min = 1, max = 1)
	public void npc(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only a player may spawn an npc");
		}

		Player player = (Player) source;
		Entity entity = player.getEntity();
		entity.getWorld().createAndSpawnEntity(entity.getPosition(), new Human(args.getString(0)));
	}
}
