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

import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Command;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.World;
import org.spout.api.player.Player;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.controller.ControllerType;
import org.spout.vanilla.controller.living.creature.hostile.Ghast;
import org.spout.vanilla.controller.living.creature.neutral.Enderman;
import org.spout.vanilla.controller.living.creature.passive.Chicken;
import org.spout.vanilla.controller.living.creature.passive.Sheep;

public class TestCommands {
	private final VanillaPlugin plugin;

	public TestCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@Command(aliases = {"spawn"}, usage = "[controller]", desc = "Spawn a controller!", max = 1)
	public void spawn(CommandContext args, CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("You must be a player to spawn a controller");
		}

		Player player = (Player) source;
		Entity entity = player.getEntity();
		World world = entity.getWorld();
		Controller control;
		if (args.length() > 0) {
			switch(ControllerType.valueOf(args.getString(0).toUpperCase())) {
				case SHEEP: {
					control = new Sheep();
					break;
				}
				case ENDERMAN: {
					control = new Enderman();
					break;
				}
				case GHAST: {
					control = new Ghast();
					break;
				}
				default:
					control = null;
			}
			if (control != null) {
				world.createAndSpawnEntity(entity.getPosition(), control);
			} else {
				throw new CommandException(args.getString(0) + " is not a valid controller to spawn.");
			}
		}
	}
}
