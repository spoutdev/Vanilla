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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.ChatColor;
import org.spout.api.Spout;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.BlockController;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.entity.PlayerController;
import org.spout.api.entity.type.ControllerRegistry;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Matrix;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;
import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.VanillaActionController;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.HealthChangeReason;
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

	@Command(aliases = {"spawn"}, usage = "<controller> <number> <spiral or disk>", desc = "Spawn up to 50 controllers!", min = 1, max = 3)
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to spawn a controller");
		}

		Player player = (Player) source;
		Point point = player.getEntity().getPosition();

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

		int number = 1;
		boolean disk = false;
		
		for (int i = 1; i < args.length(); i++) {
			try {
				number = Integer.parseInt(args.getString(i));
			} catch (NumberFormatException e) {
				if (args.getString(i).equals("disk") || args.getString(i).equals("disc")) {
					disk = true;
				} else if (args.getString(i).equals("spiral")) {
					disk = false;
				} else {
					throw new CommandException("Unable to parse command argument " + args.getString(i));
				}
			}
		}
		
		
		if (number > 50) {
			source.sendMessage(ChatColor.RED + "Reducing number of " + type.getName() + "s spawed to 50");
			number = 50;
		} else if (number < 1) {
			source.sendMessage(ChatColor.RED + "Increasing number of " + type.getName() + "s spawed to 1");
			number = 1;
		}

		if (disk) {
			diskSpawn(point, type, number, 1.5F);
		} else {
			spiralSpawn(point, type, number, 1.5F);
		}
		
		if (number == 1) {
			source.sendMessage(ChatColor.YELLOW + "One " + type.getName() + " spawned!");
		} else {
			source.sendMessage(ChatColor.YELLOW.toString() + number + " " + type.getName() + "s spawned!");
		}
	}
	
	private void spiralSpawn(Point point, ControllerType type, int number, float scale) {
		
		float angle = 0;
		float distance = 1;
		
		point.getWorld().createAndSpawnEntity(point, type.createController());
		
		for (int i = 1; i < number; i++) {
			distance = (float)Math.sqrt((float)i);
			
			Vector3 offset = Point.FORWARD.transform(MathHelper.rotateY(angle));
			offset = offset.multiply(distance).multiply(scale);
			
			point.getWorld().createAndSpawnEntity(point.add(offset), type.createController());
			
			angle += 360.0 / (Math.PI * distance);
		}

	}
	
	private void diskSpawn(Point point, ControllerType type, int number, float scale) {
		
		ArrayList<Integer> shells = new ArrayList<Integer>();
		
		int remaining = number;
		int shell = 0;
		while (remaining > 0) {
			int toAdd;
			if (shell == 0) {
				if (number == 2 || number == 3 || number == 4) {
					toAdd = 0;
				} else {
					toAdd = 1;
				}
			} else {
				toAdd = shell * 3;
			}
			if (toAdd > remaining) {
				toAdd = remaining;
			}
			shells.add(toAdd);
			remaining -= toAdd;
			shell++;
		}
		
		if (shells.size() > 1) {
			int lastIndex = shells.size() - 1;
			int last = shells.get(lastIndex);
			int secondLast = shells.get(lastIndex - 1);
			if (last < secondLast) {
				if (last >= secondLast - 2 && last > 2) {
					shells.set(lastIndex, secondLast);
					shells.set(lastIndex - 1, last);
				} else
					shells.set(lastIndex, 0);
				int i = lastIndex - 1;
				while (last > 0) {
					shells.set(i, shells.get(i) + 1);
					last--;
					i = (i == 1) ? (lastIndex - 1) : (i - 1);
				}
			}
		}

		for (int i = 0; i < shells.size(); i++) {
			circleSpawn(point, type, shells.get(i), i * scale, (i & 1) == 0);
		}
	}
	
	private void circleSpawn(Point point, ControllerType type, int number, double radius, boolean halfRotate) {
		Vector3 offset = Point.FORWARD.multiply(radius);
		int angle = number == 0 ? 0 : (360 / number);
		Matrix rotate = MathHelper.rotateY(angle);
		if (halfRotate) {
			offset = offset.transform(MathHelper.rotateY(angle / 2));
		}
		for (int i = 0; i < number; i++) {
			Point target = point.add(offset);
			target.getWorld().createAndSpawnEntity(target, type.createController());
			offset = offset.transform(rotate);
		}
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
			world.getChunkFromBlock(loc, true);
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
}
