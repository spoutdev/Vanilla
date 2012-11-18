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

import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import org.spout.vanilla.component.inventory.WindowHolder;
import org.spout.vanilla.inventory.window.Window;

public class InputCommandExecutor implements CommandExecutor {
	@Override
	public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only players may open inventory windows.");
		}

		String name = command.getPreferredName();
		if (name.equalsIgnoreCase("+toggle_inventory")) {
			Window window = ((Player) source).get(WindowHolder.class).getActiveWindow();
			if (window.isOpened()) {
				((Player) source).get(WindowHolder.class).close();
			} else {
				window.open();
			}
		}
	}
}
