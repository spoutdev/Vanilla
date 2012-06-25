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
package org.spout.vanilla.inventory.player;

import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventoryRange;
import org.spout.api.inventory.special.InventorySlot;

public class PlayerQuickbar extends InventoryRange {
	private static final long serialVersionUID = 1L;
	public static final int QUICKBAR_SIZE = 9;
	private final InventorySlot[] slots;
	private int currentSlot = 0;

	public PlayerQuickbar(InventoryBase main) {
		super(main, 0, QUICKBAR_SIZE);
		this.slots = new InventorySlot[QUICKBAR_SIZE];
		for (int i = 0; i < this.slots.length; i++) {
			this.slots[i] = new InventorySlot(this, i);
		}
	}

	/**
	 * Gets the inventory slot at a certain index
	 * @param slot to obtain
	 * @return the Inventory Slot
	 */
	public InventorySlot getSlotInventory(int slot) {
		return this.slots[slot];
	}
	
	/**
	 * Sets the quickbar slot index the player currently has selected
	 * @param slotIndex to set to
	 */
	public void setCurrentSlot(int slotIndex) {
		this.checkSlotRange(slotIndex);
		this.currentSlot = slotIndex;
	}
	
	/**
	 * Gets the quickbar slot index the player currently has selected
	 * @return slot index
	 */
	public int getCurrentSlot() {
		return this.currentSlot;
	}

	/**
	 * Sets the quickbar item the player currently has selected to the item
	 * @param item to set to
	 */
	public void setCurrentItem(ItemStack item) {
		this.setItem(this.getCurrentSlot(), item);
	}

	/**
	 * Gets the quickbar item the player currently has selected
	 * @return slot item
	 */
	public ItemStack getCurrentItem() {
		return this.getItem(this.getCurrentSlot());
	}

	/**
	 * Gets the currently selected Inventory slot
	 * @return the selected Inventory Slot
	 */
	public InventorySlot getCurrentSlotInventory() {
		return this.slots[this.getCurrentSlot()];
	}
}
