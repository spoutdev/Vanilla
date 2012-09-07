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
package org.spout.vanilla.components.misc;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import org.spout.api.component.Component;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.event.window.WindowCloseEvent;
import org.spout.vanilla.event.window.WindowEvent;
import org.spout.vanilla.event.window.WindowOpenEvent;
import org.spout.vanilla.event.window.WindowSetSlotEvent;
import org.spout.vanilla.event.window.WindowSetSlotsEvent;
import org.spout.vanilla.util.InventoryUtil;
import org.spout.vanilla.util.intmap.SlotIndexCollection;
import org.spout.vanilla.window.WindowType;

public class WindowComponent extends Component implements InventoryViewer {
	protected final WindowType type;
	protected final int instanceId;
	protected String title;
	protected final TIntObjectHashMap<ItemStack> queuedInventoryUpdates = new TIntObjectHashMap<ItemStack>(); // items to update
	protected Map<InventoryBase, SlotIndexCollection> inventories = new HashMap<InventoryBase, SlotIndexCollection>();
	protected ItemStack itemOnCursor;
	protected boolean isOpen = false;
	protected Player[] windowOwners;

	public WindowComponent(WindowType type, String title, Player... windowOwners) {
		this.type = type;
		this.title = title;
		this.instanceId = InventoryUtil.nextWindowId();
		this.windowOwners = windowOwners;
	}

	@Override
	public void onDetached() {
		if (this.isOpen()) {
			this.close();
		}
		this.inventories.clear();
		super.onDetached();
	}

	@Override
	public void onAttached() {
		super.onAttached();
		if (!this.isOpen()) {
			this.open();
		}
	}

	/**
	 * Gets the amount of items contained within this Window
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
	 * @param protocolSlot to look at
	 * @return the Inventory and slot index at the slot, or null if not found
	 */
	public Entry<InventoryBase, Integer> getInventoryEntry(int protocolSlot) {
		// get the inventory at the slot
		int spoutSlot;
		for (Entry<InventoryBase, SlotIndexCollection> inventory : this.inventories.entrySet()) {
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
	 * @return inventory data
	 */
	public Set<Entry<InventoryBase, SlotIndexCollection>> getInventoryData() {
		return this.inventories.entrySet();
	}

	/**
	 * Adds a new Inventory to this Window, mapped to the slots specified
	 * @param inventory to add
	 * @param slots of the inventory (protocol)
	 * @return the inventory that got added
	 */
	public <T extends InventoryBase> T addInventory(T inventory, SlotIndexCollection slots) {
		this.inventories.put(inventory, slots);
		return inventory;
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
		return (Player) getHolder();
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
		for (Player owner : this.windowOwners) {
			owner.addViewer(this.getHolder(), this);
		}
		for (InventoryBase inventory : this.inventories.keySet()) {
			inventory.addViewer(this);
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowOpenEvent(this));
		this.refreshItems();
	}

	/**
	 * Called when this Window is closed
	 */
	public void close() {
		this.isOpen = false;
		for (Player owner : this.windowOwners) {
			owner.removeViewer(this.getHolder());
		}
		for (InventoryBase inventory : this.inventories.keySet()) {
			inventory.removeViewer(this);
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowCloseEvent(this));
	}

	public void refreshItems() {
		// send updated slots
		ItemStack[] items = new ItemStack[this.getInventorySize()];
		int i, mcSlot;
		for (Entry<InventoryBase, SlotIndexCollection> inventory : this.inventories.entrySet()) {
			for (i = 0; i < inventory.getKey().getSize(); i++) {
				mcSlot = inventory.getValue().getMinecraftSlot(i);
				if (mcSlot < 0 || mcSlot >= items.length) {
					continue;
				}
				items[mcSlot] = inventory.getKey().getItem(i);
			}
		}
		this.sendEvent(new WindowSetSlotsEvent(this, items));
	}

	@Override
	public boolean canTick() {
		return this.isOpen();
	}

	@Override
	public void onTick(float dt) {
		int queued = this.queuedInventoryUpdates.size();
		if (queued > 0) {
			if (queued > 12) {
				this.refreshItems();
			} else {
				for (TIntObjectIterator<ItemStack> i = queuedInventoryUpdates.iterator(); i.hasNext(); ) {
					i.advance();
					int globalSlot = i.key();
					Entry<InventoryBase, Integer> itemSlot = this.getInventoryEntry(globalSlot);
					if (itemSlot == null) {
						continue;
					}
					this.sendEvent(new WindowSetSlotEvent(this, itemSlot.getKey(), itemSlot.getValue(), globalSlot, i.value()));
				}
			}
			this.queuedInventoryUpdates.clear();
		}
	}

	/**
	 * Sends a Window Protocol event to the owner of this Window
	 * @param event to send
	 */
	public void sendEvent(WindowEvent event) {
		this.getPlayer().getNetworkSynchronizer().callProtocolEvent(event);
	}

	public boolean hasItemOnCursor() {
		return this.itemOnCursor != null;
	}

	public ItemStack getItemOnCursor() {
		return this.itemOnCursor;
	}

	public void setItemOnCursor(ItemStack item) {
		this.itemOnCursor = item == null ? null : item.clone();
	}

	/**
	 * Performs a creative click in this Window
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
	 * @param inventory that was clicked
	 * @param clickedSlot of the item that was clicked
	 * @param rightClick True when right clicked by the mouse, False if not
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
			this.getParent().dropItem(this.getItemOnCursor());
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
		SlotIndexCollection slots = this.inventories.get(inventory);
		if (slots != null) {
			slot = slots.getMinecraftSlot(slot);
			this.queuedInventoryUpdates.put(slot, item);
		}
	}
}