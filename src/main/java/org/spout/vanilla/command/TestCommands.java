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

import org.spout.api.ChatColor;
import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.component.controller.BlockController;
import org.spout.api.entity.component.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.entity.component.controller.PlayerController;
import org.spout.api.entity.spawn.DiscSpawnArrangement;
import org.spout.api.entity.spawn.SpawnArrangement;
import org.spout.api.entity.spawn.SpiralSpawnArrangement;
import org.spout.api.entity.component.controller.type.ControllerRegistry;
import org.spout.api.entity.component.controller.type.ControllerType;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.Effect;
import org.spout.vanilla.util.explosion.ExplosionModels;

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

		VanillaPlayer player = (VanillaPlayer) source;

		Point position = player.getParent().getPosition().add(player.getLookingAt());
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
			if (toSpawn + numbers.get(i) > 50) {
				int newValue = 50 - toSpawn;
				source.sendMessage(ChatColor.RED + "Reducing number of " + types.get(i).getName() + "s spawed to " + newValue);
				numbers.set(i, newValue);
			}
			if (numbers.get(i) < 0) {
				source.sendMessage(ChatColor.RED + "Increasing number of " + types.get(i).getName() + "s spawed to " + 0);
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
					source.sendMessage(ChatColor.YELLOW + "Spawning a " + types.get(i).getName());
				} else {
					source.sendMessage(ChatColor.YELLOW + "Spawning " + numbers.get(i) + " " + types.get(i).getName() + "s");
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

	@Command(aliases = {"control"}, usage = "<controller>", desc = "Control a controller!", min = 1, max = 6)
	public void control(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to search for and control a controller");
		}

		String lookupType = args.getString(0).replaceAll("[_\\- ]", "");
		ControllerType type = null;
		for (ControllerType testType : ControllerRegistry.getAll()) {
			if (testType.getName().replaceAll("[_\\- ]", "").equalsIgnoreCase(lookupType)) {
				type = testType;
				break;
			}
		}

		if (type == null || !type.canCreateController()) {
			throw new CommandException("Invalid entity type '" + args.getString(0) + "'!");
		}

		/**
		 * Find a valid controller to control
		 */
		Point point = ((Player) source).getEntity().getPosition();
		Set<Entity> entities = point.getWorld().getAll(type.getControllerClass());
		Vector3 pos = null;
		Entity found = null;
		double oldDistance = Double.MAX_VALUE;
		for (Entity e : entities) {
			//Grab the entities' position
			pos = e.getPosition();
			//Find the distance between the player and this entity
			double distance = pos.distanceSquared(point);
			//If the distance is lower than the prior distance of another entity and this entity is within 5 blocks...
			if (distance < oldDistance && pos.compareTo(point) <= 5) {
				oldDistance = distance;
				found = e;
			}
		}

		/**
		 * Now check to see if we even have a controller to control.
		 */
		if (found == null) {
			throw new CommandException("Could not find any controllers within a 5 block radius to control!");
		} else {
			source.sendMessage(found.getController().toString() + "was found at " + found.getPosition().toString() + ". Assuming control!");
		}

		Controller control = found.getController();

		/**
		 * The fun part...lets control the controller!
		 */
		if (args.getString(1).equalsIgnoreCase("move")) {
			Vector3 movement = new Vector3(args.getInteger(2), args.getInteger(3), args.getInteger(4));
			control.getParent().translate(movement.divide(args.getDouble(5)));
		}
	}

	@Command(aliases = {"tppos"}, usage = "<name> <world> <x> <y> <z>", desc = "Teleport to coordinates!", min = 5, max = 5)
	public void tppos(CommandContext args, CommandSource source) throws CommandException {
		Player player = Spout.getEngine().getPlayer(args.getString(0), true);
		if (!(source instanceof Player) && player == null) {
			throw new CommandException("Must specifiy a valid player to tppos from the console.");
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

	@Command(aliases = {"block"}, desc = "Checks if the block below you has an entity", min = 0, max = 0)
	public void checkBlock(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Source must be player");
		}

		Player player = (Player) source;
		Entity playerEntity = player.getEntity();
		Block block = playerEntity.getWorld().getBlock(playerEntity.getPosition().subtract(0, 1, 0));
		if (!block.hasController()) {
			player.sendMessage("Block has no entity!");
			return;
		}

		BlockController controller = block.getController();
		player.sendMessage("Material: " + controller.getMaterial().getName());
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
				source.sendMessage(count + " entity(es) have been killed. The console has a listing of what controllers were killed.");
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

	@Command(aliases = "fx", desc = "Add an effect", min = 3, max = 3)
	public void addEffect(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only a player may add effects");
		}

		Controller controller = ((Player) source).getEntity().getController();
		if (controller instanceof VanillaPlayer) {
			((VanillaPlayer) controller).addEffect(new Effect(Effect.Type.valueOf(args.getString(0).toUpperCase()), (byte) args.getInteger(1), (short) args.getInteger(2)));
		}
	}
}
