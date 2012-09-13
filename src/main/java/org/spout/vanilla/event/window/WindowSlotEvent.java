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
package org.spout.vanilla.event.window;

import org.spout.api.event.HandlerList;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.components.window.Window;

public class WindowSlotEvent extends WindowEvent {
	private static HandlerList handlers = new HandlerList();
	private int slot, globalSlot;
	private ItemStack item;
	private InventoryBase inventory;

	public WindowSlotEvent(Window window, InventoryBase inventory, int slot, int globalSlot, ItemStack item) {
		super(window);
		this.inventory = inventory;
		this.slot = slot;
		this.globalSlot = globalSlot;
		this.item = item;
	}

	/**
	 * Gets the Inventory within the item got changed
	 * @return the Inventory of the item
	 */
	public InventoryBase getInventory() {
		return this.inventory;
	}

	/**
	 * Gets the slot of the Inventory that got changed
	 * @return changed slot relative to the Inventory
	 */
	public int getSlot() {
		return this.slot;
	}

	/**
	 * Gets the global slot in the Window that got changed
	 * @return changed slot relative to the Window
	 */
	public int getGlobalSlot() {
		return this.globalSlot;
	}

	/**
	 * Gets the item the slot is set to
	 * @return the item
	 */
	public ItemStack getItem() {
		return this.item;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
