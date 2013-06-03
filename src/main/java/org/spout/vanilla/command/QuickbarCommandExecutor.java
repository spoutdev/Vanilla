/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.command;

import org.spout.api.command.Command;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.Executor;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;

import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.component.entity.player.HUD;
import org.spout.vanilla.inventory.entity.QuickbarInventory;

public class QuickbarCommandExecutor implements Executor {
	@Override
	public void execute(CommandSource source, Command command, CommandArguments args) throws CommandException {
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}

		String name = command.getName();
		if (name.startsWith("quickbar_")) {
			Player player = (Player) source;
			PlayerInventory inventory = player.get(PlayerInventory.class);
			if (inventory != null) {
				QuickbarInventory quickbar = inventory.getQuickbar();
				HUD hud = player.get(HUD.class);
				int newSlot = quickbar.getSelectedSlot().getIndex();
				if (name.endsWith("left")) {
					newSlot++;
					if (newSlot > 8) {
						newSlot = 0;
					}
				} else if (name.endsWith("right")) {
					newSlot--;
					if (newSlot < 0) {
						newSlot = 8;
					}
				} else {
					newSlot = Integer.parseInt(name.substring(name.indexOf('_') + 1)) - 1;
				}

				quickbar.setSelectedSlot(newSlot);
				hud.getQuickbar().update();
			}
		}
	}
}
