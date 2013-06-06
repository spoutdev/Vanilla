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

import org.spout.api.Client;
import org.spout.api.command.CommandArguments;
import org.spout.api.command.CommandSource;
import org.spout.api.command.annotated.Binding;
import org.spout.api.command.annotated.Command;
import org.spout.api.component.entity.InteractComponent;
import org.spout.api.command.annotated.Filter;
import org.spout.api.command.filter.PlayerFilter;
import org.spout.api.entity.Player;
import org.spout.api.exception.CommandException;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.input.Keyboard;
import org.spout.api.input.Mouse;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.material.VanillaMaterials;

public class InputCommands  {
	private final VanillaPlugin plugin;
	private final Client client;
	private BlockMaterial selection;

	public InputCommands(VanillaPlugin plugin) {
		this.plugin = plugin;
		client = (Client) plugin.getEngine();  // TODO: This needs to be changed. This is unsafe. Although for now it works.
	}

	@Command(aliases = "toggle_inventory", desc = "Opens and closes your inventory.", min = 1, max = 1)
	@Binding(Keyboard.KEY_E)
	@Filter(PlayerFilter.class)
	public void toggleInventory(CommandSource source, CommandArguments args) throws CommandException {
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

	@Command(aliases = "break_block", desc = "Breaks a block.", min = 1, max = 1)
	@Binding(mouse = Mouse.BUTTON_LEFT)
	@Filter(PlayerFilter.class)
	public void breakBlock(CommandSource source, CommandArguments args) throws CommandException {
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}
		InteractComponent hit = ((Player) source).get(InteractComponent.class);
		if (hit != null) {
			final Block hitting = hit.getTargetBlock();
			if (hitting != null && !hitting.getMaterial().equals(VanillaMaterials.AIR)) {
				client.getScheduler().safeRun(VanillaPlugin.getInstance(), new Runnable() {
					@Override
					public void run() {
						hitting.setMaterial(VanillaMaterials.AIR);
					}
				});
				client.getLogger().info("Broke block: " + hitting.toString());
			}
		}
	}

	@Command(aliases = "select_block", desc = "Selects a block to place", min = 1, max = 1)
	@Binding(mouse = Mouse.BUTTON_MIDDLE)
	@Filter(PlayerFilter.class)
	public void selectBlock(CommandSource source, CommandArguments args) throws CommandException {
		if (!args.getString(0).equalsIgnoreCase("+")) {
			return;
		}
		InteractComponent hit = ((Player) source).get(InteractComponent.class);
		if (hit != null) {
			Block hitting = hit.getTargetBlock(true);
			if (hitting != null && !hitting.getMaterial().equals(VanillaMaterials.AIR)) {
				client.getLogger().info(hitting.getMaterial().getName());
				selection = hitting.getMaterial();
			}
		}
	}

	@Command(aliases = "place_block", desc = "Places a block.", min = 1, max = 1)
	@Binding(mouse = Mouse.BUTTON_RIGHT)
	@Filter(PlayerFilter.class)
	public void placeBlock(CommandSource source, CommandArguments args) throws CommandException {
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
				client.getLogger().info(clicked.name());
				client.getScheduler().safeRun(VanillaPlugin.getInstance(), new Runnable() {
					@Override
					public void run() {
						hitting.translate(clicked).setMaterial(selection);
					}
				});
			}
		}
	}
}
