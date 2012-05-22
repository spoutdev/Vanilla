/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.util.InventoryUtil;

public abstract class WindowInventory extends Inventory implements VanillaInventory {
	protected final Window window;
	protected String title;

	public WindowInventory(Window window, int size, String title) {
		super(size);
		this.window = window;
		this.title = title;
	}

	/**
	 * Gets the window associated with the inventory
	 * @return window of inventory
	 */
	public Window getWindow() {
		return window;
	}

	/**
	 * Gets the title displayed on the window of the inventory
	 * @return inventory window title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title displayed on the window of the inventory
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Opens the inventory and window
	 * @param player to show the inventory
	 */
	public void open(VanillaPlayer player) {
		Inventory inventory = player.getPlayer().getEntity().getInventory();
		for (int slot = 0; slot < inventory.getSize() - 9; slot++) {
			setItem(slot, inventory.getItem(slot));
		}
		addViewer(player.getPlayer().getNetworkSynchronizer());
		player.setActiveInventory(this);
		player.openWindow(getWindow(), title, getSize());
	}

	/**
	 * Closes the inventory and window
	 * @param player to close the inventory on
	 */
	public void onClosed(VanillaPlayer player) {
		Inventory inventory = player.getPlayer().getEntity().getInventory();
		for (int slot = 0; slot < inventory.getSize() - 9; slot++) {
			setItem(slot, null);
		}
		removeViewer(player.getPlayer().getNetworkSynchronizer());
	}

	/**
	 * Handles a click of the player's cursor on the window.
	 * @param player
	 * @param clickedSlot
	 * @return true if click is permitted
	 */
	public boolean onClicked(VanillaPlayer player, int clickedSlot, ItemStack slotStack) {
		slotStack = InventoryUtil.nullIfEmpty(slotStack);
		setItem(clickedSlot, slotStack);
		return true;
	}

	/**
	 * Gets the native protocol slot index
	 * @param index of the slot
	 * @return the native index
	 */
	public abstract int getNativeSlotIndex(int index);

	/**
	 * Gets the slot index from a native slot index
	 * @param nativeIndex of the item
	 * @return the Spout item index
	 */
	public abstract int getSlotIndex(int nativeIndex);
}
