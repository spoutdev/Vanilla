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
package org.spout.vanilla.inventory.window.block.chest;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.shape.Grid;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.component.inventory.PlayerInventoryComponent;
import org.spout.vanilla.component.substance.material.chest.AbstractChest;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.entity.QuickbarInventory;
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.inventory.util.InventoryConverter;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.inventory.window.WindowType;

public class AbstractChestWindow extends Window {
	private AbstractChest chest;

	public AbstractChestWindow(Player owner, AbstractChest chest, WindowType type, String title, int offset) {
		super(owner, type, title, offset);
		this.chest = chest;
	}

	public AbstractChestWindow(Player owner, WindowType type, String title, int offset) {
		super(owner, type, title, offset);
	}

	public AbstractChest getChest() {
		return chest;
	}

	@Override
	public boolean onShiftClick(ItemStack stack, int slot, Inventory from) {
		if (Spout.getPlatform() == Platform.CLIENT) {
			throw new IllegalStateException("Shift click handling is handled server side.");
		}
		final PlayerInventoryComponent inventory = getPlayerInventory();

		// From main inventory/quickbar to the chest
		if (from instanceof PlayerMainInventory || from instanceof PlayerQuickbar) {
			for (InventoryConverter conv : this.getInventoryConverters()) {
				Inventory inv = conv.getInventory();
				if (inv instanceof ChestInventory) {
					Grid grid = inv.grid(ChestInventory.LENGTH);
					for (int row = grid.getHeight() - 1; row >= 0; row--) {
						int startSlot = ChestInventory.LENGTH * row;
						int endSlot = startSlot + ChestInventory.LENGTH - 1;
						inv.add(startSlot, endSlot, stack);
						from.set(slot, stack);
						if (stack.isEmpty()) {
							return true;
						}
					}
				}
			}
		}

		// From chest to inventory/quickbar
		if (from instanceof ChestInventory) {
			// To quickbar (reversed)
			final QuickbarInventory qbar = inventory.getQuickbar();
			qbar.add(qbar.size() - 1, 0, stack);
			from.set(slot, stack);
			if (stack.isEmpty()) {
				return true;
			}

			// To main inventory (reversed)
			final Inventory main = inventory.getMain();
			for (int row = 0; row < PlayerMainInventory.HEIGHT; row++) {
				int startSlot = PlayerMainInventory.LENGTH * row;
				int endSlot = startSlot + PlayerMainInventory.LENGTH - 1;
				main.add(endSlot, startSlot, stack);
				from.set(slot, stack);
				if (stack.isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void close() {
		if (chest != null) {
			chest.close(getPlayer());
		}
		super.close();
	}
}
