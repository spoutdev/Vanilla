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

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.api.player.Player;

import org.spout.vanilla.controller.WindowOwner;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.WindowMessage;
import org.spout.vanilla.util.InventoryUtil;
import org.spout.vanilla.util.SlotIndexMap;

public class Window implements InventoryViewer {
	protected final WindowType type;
	protected final int instanceId;
	protected String title;
	protected final VanillaPlayer owner;
	protected Map<InventoryBase, SlotIndexMap> inventories = new HashMap<InventoryBase, SlotIndexMap>();
	protected ItemStack itemOnCursor;
	protected boolean isOpen = false;
	protected WindowOwner[] windowOwners;

	public Window(WindowType type, String title, VanillaPlayer owner, WindowOwner... windowOwners) {
		this.type = type;
		this.title = title;
		this.owner = owner;
		this.instanceId = InventoryUtil.nextWindowId();
		this.windowOwners = windowOwners;
	}

	/**
	 * Gets the amount of items contained within this Window
	 * 
	 * @return inventory size
	 */
	public int getInventorySize() {
		int size = 0;
		for (InventoryBase inventory : this.inventories.keySet()) {
			size += inventory.getSize();
		}
		return size;
	}

	/**
	 * Gets the Inventory and slot index from a protocol slot
	 * 
	 * @param protocolSlot to look at
	 * @return the Inventory and slot index at the slot, or null if not found
	 */
	public Entry<InventoryBase, Integer> getInventoryEntry(int protocolSlot) {
		// get the inventory at the slot
		int spoutSlot;
		for (Entry<InventoryBase, SlotIndexMap> inventory : this.inventories.entrySet()) {
			spoutSlot = inventory.getValue().getSpoutSlot(protocolSlot);
			if (spoutSlot != -1) {
				// return inventory and converted slot
				return new SimpleEntry<InventoryBase, Integer>(inventory.getKey(), spoutSlot);
			}
		}
		return null;
	}

	/**
	 * Gets a set of entries containing the inventories and slot index maps contained within this Window
	 * 
	 * @return inventory data
	 */
	public Set<Entry<InventoryBase, SlotIndexMap>> getInventoryData() {
		return this.inventories.entrySet();
	}

	/**
	 * Adds a new Inventory to this Window, mapped to the slots specified
	 * 
	 * @param inventory to add
	 * @param slots of the inventory (protocol)
	 */
	public void addInventory(InventoryBase inventory, SlotIndexMap slots) {
		this.inventories.put(inventory, slots);
	}

	/**
	 * Gets the 'minecraft' or 'notchian' window type of this Window
	 * @return window type
	 */
	public WindowType getType() {
		return this.type;
	}

	/**
	 * Gets the unique Id of this Window instance
	 * @return window id
	 */
	public int getInstanceId() {
		return this.instanceId;
	}

	/**
	 * Gets the title of this Window shown
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title shown by this Window
	 * @param title to set to
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the player viewing this Window
	 * @return the player
	 */
	public Player getPlayer() {
		return this.owner.getPlayer();
	}

	/**
	 * Gets the Vanilla player controller owner of this Window
	 * @return the owner player controller
	 */
	public VanillaPlayer getOwner() {
		return this.owner;
	}

	/**
	 * Gets whether this window is opened to the player
	 * @return True if open, False if not
	 */
	public boolean isOpen() {
		return this.isOpen;
	}

	/**
	 * Called when this Window is opened
	 */
	public void open() {
		this.isOpen = true;
		for (WindowOwner owner : this.windowOwners) {
			owner.addViewer(this.getOwner(), this);
		}
		for (InventoryBase inventory : this.inventories.keySet()) {
			inventory.addViewer(this);
		}
		// send updated slots
		ItemStack[] items = new ItemStack[this.getInventorySize()];
		int i;
		for (Entry<InventoryBase, SlotIndexMap> inventory : this.inventories.entrySet()) {
			i = 0;
			for (ItemStack item : inventory.getKey()) {
				items[inventory.getValue().getMinecraftSlot(i)] = item;
				i++;
			}
		}
		this.getPlayer().getNetworkSynchronizer().updateAll(null, items);
	}

	/**
	 * Called when this Window is closed
	 */
	public void close() {
		this.isOpen = false;
		for (WindowOwner owner : this.windowOwners) {
			owner.removeViewer(this.getOwner());
		}
		for (InventoryBase inventory : this.inventories.keySet()) {
			inventory.removeViewer(this);
		}
	}

	/**
	 * Sends a Window Message to the owner of this Window
	 * @param message to send
	 */
	public void sendMessage(WindowMessage message) {
		this.getPlayer().getSession().send(false, message);
	}

	public boolean hasItemOnCursor() {
		return this.itemOnCursor != null;
	}

	public ItemStack getItemOnCursor() {
		return this.itemOnCursor;
	}

	public void setItemOnCursor(ItemStack item) {
		this.itemOnCursor = item;
	}

	/**
	 * Performs a creative click in this Window
	 * 
	 * @param inventory that got clicked
	 * @param clickedSlot of the item in the Inventory
	 * @param item the player set it to
	 */
	public void onCreativeClick(InventoryBase inventory, int clickedSlot, ItemStack item) {
		this.setItemOnCursor(null);
		inventory.setItem(clickedSlot, item);
	}

	/**
	 * Performs a click by the user
	 * 
	 * @param inventory that was clicked
	 * @param clickedSlot of the item that was clicked
	 * @param rightClick	True when right clicked by the mouse, False if not
	 * @param shift was down
	 * @return True if successful
	 */
	public boolean onClick(InventoryBase inventory, int clickedSlot, ClickArgs args) {
		if (args.isShiftDown()) {
			//TODO: Implement shift-transferring
			return false;
		} else if (args.isLeftClick()) {
			ItemStack clickedItem = inventory.getItem(clickedSlot);
			if (clickedItem == null) {
				if (this.hasItemOnCursor()) {
					// cursor > clicked item
					inventory.setItem(clickedSlot, this.getItemOnCursor());
					this.setItemOnCursor(null);
					return true;
				}

				return true;
			}

			if (!this.hasItemOnCursor()) {
				// clicked item > cursor
				this.setItemOnCursor(clickedItem);
				inventory.setItem(clickedSlot, null);
				return true;
			}

			// clicked item + cursor
			ItemStack cursorItem = this.getItemOnCursor();
			if (cursorItem.equalsIgnoreSize(clickedItem)) {
				// stack
				clickedItem.stack(cursorItem);
				inventory.setItem(clickedSlot, clickedItem);
				this.setItemOnCursor(cursorItem.getAmount() <= 0 ? null : cursorItem);
				return true;
			}

			// swap
			this.setItemOnCursor(clickedItem);
			inventory.setItem(clickedSlot, cursorItem);
			return true;
		} else if (args.isRightClick()) {
			ItemStack clickedItem = inventory.getItem(clickedSlot);
			if (clickedItem == null) {
				if (this.hasItemOnCursor()) {
					// cursor > clicked item
					ItemStack cursorItem = this.getItemOnCursor();
					clickedItem = cursorItem.clone();
					clickedItem.setAmount(1);
					cursorItem.setAmount(cursorItem.getAmount() - clickedItem.getAmount());
					inventory.setItem(clickedSlot, clickedItem);
					this.setItemOnCursor(cursorItem.getAmount() <= 0 ? null : cursorItem);
					return true;
				}

				return true;
			}
			if (this.hasItemOnCursor()) {
				// clicked item + cursor
				ItemStack cursorItem = this.getItemOnCursor();
				if (!cursorItem.equalsIgnoreSize(clickedItem)) {
					// swap
					this.setItemOnCursor(clickedItem);
					inventory.setItem(clickedSlot, cursorItem);
					return true;
				}

				if (clickedItem.getAmount() >= clickedItem.getMaxStackSize()) {
					return false;
				}

				// transfer one item
				clickedItem.setAmount(clickedItem.getAmount() + 1);
				cursorItem.setAmount(cursorItem.getAmount() - 1);
				inventory.setItem(clickedSlot, clickedItem);
				this.setItemOnCursor(cursorItem.getAmount() <= 0 ? null : cursorItem);
				return true;
			}

			// 1/2 clicked item > cursor
			ItemStack newItem = clickedItem.clone();
			newItem.setAmount(newItem.getAmount() / 2);
			clickedItem.setAmount(clickedItem.getAmount() - newItem.getAmount());
			inventory.setItem(clickedSlot, newItem.getAmount() <= 0 ? null : newItem);
			this.setItemOnCursor(clickedItem.getAmount() <= 0 ? null : clickedItem);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Drops the item that is currently on the player's cursor
	 */
	public void dropItemOnCursor() {
		if (this.hasItemOnCursor()) {
			this.getOwner().dropItem(this.getItemOnCursor());
			this.setItemOnCursor(null);
		}
	}

	/**
	 * Called when the player clicks outside the window
	 * @return True to notify that the operation was allowed
	 */
	public boolean onOutsideClick() {
		this.dropItemOnCursor();
		return true;
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		// convert to MC slot and update
		SlotIndexMap slots = this.inventories.get(inventory);
		if (slots != null) {
			this.getPlayer().getNetworkSynchronizer().onSlotSet(null, slots.getMinecraftSlot(slot), item);
		}
	}

	@Override
	public void updateAll(InventoryBase inventory, ItemStack[] slots) {
		for (int i = 0; i < slots.length; i++) {
			this.onSlotSet(inventory, i, slots[i]);
		}
	}
}
