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
import org.spout.api.inventory.special.InventorySlot;

import org.spout.vanilla.component.WindowController;
import org.spout.vanilla.inventory.CraftingInventory;
import org.spout.vanilla.util.intmap.SlotIndexCollection;

public abstract class CraftingWindow extends TransactionWindow {
	protected CraftingInventory craftingGrid;

	public CraftingWindow(WindowType type, String title, int transactionSize, WindowController... windowOwners) {
		super(type, title, transactionSize, windowOwners);
	}

	public <T extends CraftingInventory> T setCraftingGrid(T craftingGrid, SlotIndexCollection slots) {
		this.craftingGrid = this.addInventory(craftingGrid, slots);
		return craftingGrid;
	}

	public CraftingInventory getCraftingGrid() {
		return craftingGrid;
	}

	@Override
	public void open() {
		super.open();
		this.craftingGrid.getGrid().addViewer(this);
	}

	@Override
	public void close() {
		super.close();
		this.craftingGrid.getGrid().removeViewer(this);
		// Drop any items remaining in the grid
		for (int i = 0; i < this.craftingGrid.getSize(); i++) {
			ItemStack item = this.craftingGrid.getItem(i);
			if (item != null) {
				this.craftingGrid.setItem(i, null);
				this.getParent().dropItem(item);
			}
		}
		// Drop item on cursor
		this.dropItemOnCursor();
	}

	@Override
	public boolean onClick(InventoryBase inventory, int clickedSlot, ClickArgs args) {
		if (inventory == this.getCraftingGrid() && clickedSlot == this.getCraftingGrid().getOutput().getOffset()) {
			ItemStack output = this.getCraftingGrid().getOutput().getItem();
			if (output == null) {
				return true;
			}
			if (args.isShiftDown()) {
				InventorySlot slot = this.craftingGrid.getOutput();
				ItemStack clickedItem = slot.getItem().clone();
				ItemStack[] before = this.craftingGrid.getGrid().getContents();
				ItemStack newItem = new ItemStack(clickedItem.getMaterial(), 0);
				while (clickedItem.equalsIgnoreSize(slot.getItem())) {
					newItem.setAmount(newItem.getAmount() + 1);
					this.craftingGrid.craft();
				}
				if (!getParent().getInventory().addItemFully(newItem)) {
					this.craftingGrid.setContents(before);
				}
				return true;
			} else if (args.isLeftClick()) {
				InventorySlot slot = this.craftingGrid.getOutput();
				ItemStack clickedItem = slot.getItem();
				if (clickedItem == null) {
					if (this.hasItemOnCursor()) {
						return false;
					}
					return true;
				}

				if (!this.hasItemOnCursor()) {
					// clicked item > cursor
					this.setItemOnCursor(clickedItem);
					slot.setItem(null);
					this.craftingGrid.craft();
					return true;
				}

				// clickedItem != null && this.hasItemOnCursor()
				// clicked item + cursor
				ItemStack cursorItem = this.getItemOnCursor();
				if (!cursorItem.equalsIgnoreSize(clickedItem)) {
					return false;
				}

				// stack
				cursorItem.stack(clickedItem);
				slot.setItem(clickedItem.getAmount() <= 0 ? null : clickedItem);
				this.setItemOnCursor(cursorItem);
				this.craftingGrid.craft();
				return true;
			} else if (args.isRightClick()) {
				InventorySlot slot = craftingGrid.getOutput();
				ItemStack clickedItem = slot.getItem();

				if (clickedItem == null) {
					if (this.hasItemOnCursor()) {
						return false;
					}
					return true;
				}

				if (this.hasItemOnCursor()) {
					// clicked item + cursor
					ItemStack cursorItem = this.getItemOnCursor();
					if (!cursorItem.equalsIgnoreSize(clickedItem)) {
						return false;
					}
					if (cursorItem.getAmount() + clickedItem.getAmount() > cursorItem.getMaxStackSize()) {
						return false;
					}
					cursorItem.stack(clickedItem);
					slot.setItem(null);
					this.setItemOnCursor(cursorItem);
				} else {
					this.setItemOnCursor(clickedItem);
					slot.setItem(null);
				}
				this.craftingGrid.craft();
				return true;
			}
		}
		return super.onClick(inventory, clickedSlot, args);
	}
}
