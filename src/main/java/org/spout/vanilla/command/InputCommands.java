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

import java.util.logging.Level;

import org.spout.api.command.CommandContext;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Binding;
import org.spout.api.command.annotated.Command;
import org.spout.api.component.impl.InteractComponent;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.input.Keyboard;
import org.spout.api.input.Mouse;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.material.VanillaMaterials;

public class InputCommands {
	private BlockMaterial selection;

	public InputCommands(VanillaPlugin plugin) {
	}

	@Command(platform = Platform.CLIENT, aliases = "toggle_inventory", desc = "Opens and closes your inventory.", min = 1, max = 1)
	@Binding(keys = Keyboard.KEY_E)
	public void toggleInventory(CommandContext args, CommandSource source) throws CommandException {
		checkPlayer(source);
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}
		WindowHolder holder = ((Player) source).get(WindowHolder.class);
		Window window = holder.getActiveWindow();
		if (window.isOpened()) {
			holder.closeWindow();
		} else {
			holder.openWindow(holder.getDefaultWindow());
		}
	}

	@Command(platform = Platform.CLIENT, aliases = "break_block", desc = "Breaks a block.", min = 1, max = 1)
	@Binding(mouse = Mouse.MOUSE_BUTTON0)
	public void breakBlock(CommandContext args, CommandSource source) throws CommandException {
		checkPlayer(source);
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}
		InteractComponent hit = ((Player) source).get(InteractComponent.class);
		if (hit != null) {
			final Block hitting = hit.getTargetBlock();
			if (hitting != null && !hitting.getMaterial().equals(VanillaMaterials.AIR)) {
				VanillaPlugin.getInstance().getEngine().getScheduler().safeRun(VanillaPlugin.getInstance(), new Runnable() {
					@Override
					public void run() {
						hitting.setMaterial(VanillaMaterials.AIR);
					}
				});
				VanillaPlugin.getInstance().getLogger().info("Broke block: " + hitting.toString());
			}
		}
	}

	@Command(platform = Platform.CLIENT, aliases = "select_block", desc = "Selects a block to place", min = 1, max = 1)
	@Binding(mouse = Mouse.MOUSE_BUTTON2)
	public void selectBlock(CommandContext args, CommandSource source) throws CommandException {
		checkPlayer(source);
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}
		InteractComponent hit = ((Player) source).get(InteractComponent.class);
		if (hit != null) {
			Block hitting = hit.getTargetBlock(true);
			if (hitting != null && !hitting.getMaterial().equals(VanillaMaterials.AIR)) {
				VanillaPlugin.getInstance().getLogger().info(hitting.getMaterial().getName());
				selection = hitting.getMaterial();
			}
		}
	}

	@Command(platform = Platform.CLIENT, aliases = "place_block", desc = "Places a block.", min = 1, max = 1)
	@Binding(mouse = Mouse.MOUSE_BUTTON1)
	public void placeBlock(CommandContext args, CommandSource source) throws CommandException {
		checkPlayer(source);
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}
		InteractComponent hit = ((Player) source).get(InteractComponent.class);
		if (hit != null) {
			final Block hitting = hit.getTargetBlock();
			if (hitting != null && selection != null && !hitting.getMaterial().equals(VanillaMaterials.AIR)) {
				final BlockFace clicked = hit.getTargetFace();
				System.out.println(clicked);
				if (clicked == null) {
					return;
				}
				VanillaPlugin.getInstance().getLogger().info(clicked.name());
				VanillaPlugin.getInstance().getEngine().getScheduler().safeRun(VanillaPlugin.getInstance(), new Runnable() {
					@Override
					public void run() {
						hitting.translate(clicked).setMaterial(selection);
					}
				});
			}
		}
	}

	private void checkPlayer(CommandSource source) throws CommandException {
		if (!(source instanceof Player)) {
			throw new CommandException("Only players may open inventory windows.");
		}
	}
}
