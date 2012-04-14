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
package org.spout.vanilla.command;

import java.util.Set;

import org.spout.api.ChatColor;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.entity.type.ControllerRegistry;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.source.HealthChangeReason;

public class TestCommands {
	private VanillaPlugin plugin;

	public TestCommands(VanillaPlugin instance) {
		this.plugin = instance;
	}

	@Command(aliases = {"sethealth"}, usage = "<health>", desc = "Set your health", min = 1, max = 1)
	public void setHealth(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to set your health");
		}
		
		Player player = (Player) source;
		player.getEntity().setHealth(args.getInteger(0), new HealthChangeReason(HealthChangeReason.Type.UNKNOWN));
	}

	@Command(aliases = {"spawn"}, usage = "<controller>", desc = "Spawn a controller!", min = 1, max = 1)
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

		point.getWorld().createAndSpawnEntity(point, type.createController());
		source.sendMessage(ChatColor.YELLOW + "One " + type.getName() + " spawned!");
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
}
