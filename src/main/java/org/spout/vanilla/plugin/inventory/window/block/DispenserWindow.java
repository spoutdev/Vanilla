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
package org.spout.vanilla.plugin.inventory.window.block;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.shape.Grid;
import org.spout.api.math.Vector2;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.inventory.block.DispenserInventory;
import org.spout.vanilla.plugin.inventory.player.PlayerMainInventory;
import org.spout.vanilla.plugin.inventory.player.PlayerQuickbar;
import org.spout.vanilla.plugin.inventory.util.GridInventoryConverter;
import org.spout.vanilla.plugin.inventory.util.InventoryConverter;
import org.spout.vanilla.plugin.inventory.window.Window;
import org.spout.vanilla.plugin.inventory.window.WindowType;

public class DispenserWindow extends Window {
	public DispenserWindow(Player owner, DispenserInventory inventory) {
		this(owner, inventory, "Dispenser");
	}

	public DispenserWindow(Player owner, DispenserInventory inventory, String title) {
		super(owner, WindowType.DISPENSER, title, 9);
		addInventoryConverter(new GridInventoryConverter(inventory, 3, Vector2.ZERO));
	}

	@Override
	public boolean onShiftClick(ItemStack stack, int slot, Inventory from) {
		if (Spout.getPlatform() == Platform.CLIENT) {
			throw new IllegalStateException("Shift click handling is handled server side.");
		}
		final PlayerInventory inventory = getPlayerInventory();

		// From main inventory/quickbar to the dispenser
		if (from instanceof PlayerMainInventory || from instanceof PlayerQuickbar) {
			for (InventoryConverter conv : this.getInventoryConverters()) {
				Inventory inv = conv.getInventory();
				if (inv instanceof DispenserInventory) {
					Grid grid = inv.grid(3);
					final int height = grid.getHeight();
					final int length = grid.getLength();
					for (int y = height - 1; y >= 0; y--) {
						int x1 = length * y;
						int x2 = x1 + length - 1;
						inv.add(x1, x2, stack);
						from.set(slot, stack);
						if (stack.isEmpty()) {
							return true;
						}
					}
				}
			}
		}

		// From chest to inventory/quickbar
		if (from instanceof DispenserInventory) {
			// To quickbar (reversed)
			final PlayerQuickbar qbar = inventory.getQuickbar();
			qbar.add(qbar.size() - 1, 0, stack);
			from.set(slot, stack);
			if (stack.isEmpty()) {
				return true;
			}

			// To main inventory (reversed)
			final PlayerMainInventory main = inventory.getMain();
			final int height = 3;
			final int length = 9;
			for (int y = 0; y < height; y++) {
				int x1 = length * y;
				int x2 = x1 + length - 1;
				main.add(x2, x1, stack);
				from.set(slot, stack);
				if (stack.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}
}
