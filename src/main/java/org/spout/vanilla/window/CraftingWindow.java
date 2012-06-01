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
package org.spout.vanilla.window;

import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.CraftingGrid;
import org.spout.vanilla.window.Window;

public abstract class CraftingWindow extends Window {
	protected final CraftingGrid craftingGrid;

	public CraftingWindow(int id, String title, VanillaPlayer owner, CraftingGrid craftingGrid) {
		super(id, title, owner);
		this.craftingGrid = craftingGrid;
	}

	public CraftingGrid getCraftingGrid() {
		return craftingGrid;
	}

	@Override
	public void onClosed() {
		super.onClosed();
		// TODO: Drop items in grid
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		super.onSlotSet(inventory, slot, item);
		this.updateOutput(false);
	}

	@Override
	public boolean onClick(int clickedSlot, boolean rightClick, boolean shift) {
		if (clickedSlot == craftingGrid.getOutputSlot()) {
			if (shift) {
				//TODO: Handle shift-click massive crafting (is NOT set on cursor item!)
			} else {
				ItemStack output = this.getInventory().getItem(clickedSlot);
				if (output != null) {
					if (this.hasItemOnCursor()) {
						if (this.getItemOnCursor().equalsIgnoreSize(output)) {
							//add
							ItemStack newItem = this.getItemOnCursor().clone();
							newItem.setAmount(newItem.getAmount() + output.getAmount());
							if (newItem.getAmount() <= newItem.getMaxStackSize()) {
								this.setItemOnCursor(output);
								this.updateOutput(true);
								return true;
							}
						} else {
							this.setItemOnCursor(output);
							this.updateOutput(true);
							return true;
						}
					}
				}
			}
		} else if (super.onClick(clickedSlot, rightClick, shift)) {
			this.updateOutput(false);
			return true;
		}
		return false;
	}

	private void updateOutput(boolean craft) {
		//TODO: Recipes
		if (craft) {
			//TODO: Subtract items and check if it still the same recipe
		}
	}
}
