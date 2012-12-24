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

import java.util.concurrent.BrokenBarrierException;

import org.spout.api.Spout;
import org.spout.api.command.Command;
import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandExecutor;
import org.spout.api.command.CommandSource;
import org.spout.api.component.impl.HitBlockComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.util.map.concurrent.BlockLinkable;

import org.spout.vanilla.component.inventory.WindowHolder;
import org.spout.vanilla.event.cause.DamageCause;
import org.spout.vanilla.event.cause.NullDamageCause;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.material.VanillaMaterials;

public class InputCommandExecutor implements CommandExecutor {
	@Override
	public void processCommand(CommandSource source, Command command, CommandContext args) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only players may open inventory windows.");
		}

		String name = command.getPreferredName();
		if (name.equalsIgnoreCase("+toggle_inventory")) {
			WindowHolder holder = ((Player) source).get(WindowHolder.class);
			Window window = holder.getActiveWindow();
			if (window.isOpened()) {
				holder.closeWindow();
			} else {
				holder.openWindow(holder.getDefaultWindow());
			}
		} else if (name.equalsIgnoreCase("+break_block")) {
			HitBlockComponent hit = ((Entity) source).get(HitBlockComponent.class);
			if (hit != null) {
				Block hitting = hit.getTargetBlock();
				if (hitting != null && !hitting.getMaterial().equals(VanillaMaterials.AIR)) {
					hitting.setMaterial(VanillaMaterials.AIR, new NullDamageCause(DamageCause.DamageType.ATTACK)); //TODO Completely wrong, simply experimenting
				}
				Spout.log("Broke block: " + hitting.toString());
			}
		}
	}
}
