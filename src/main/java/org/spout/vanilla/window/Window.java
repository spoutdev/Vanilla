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
package org.spout.vanilla.window;

import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.special.InventoryBundle;
import org.spout.api.player.Player;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.protocol.msg.CloseWindowMessage;
import org.spout.vanilla.protocol.msg.OpenWindowMessage;
import org.spout.vanilla.util.InventoryUtil;

import static org.spout.vanilla.protocol.VanillaNetworkSynchronizer.sendPacket;

public class Window implements InventoryViewer {
	private final int id;
	private final int instanceId;
	private String title;
	private final VanillaPlayer owner;
	private InventoryBundle inventory;
	private ItemStack itemOnCursor;
	private int[] slotConversionArray;

	public Window(int id, String title, VanillaPlayer owner) {
		this.id = id;
		this.title = title;
		this.owner = owner;
		this.instanceId = InventoryUtil.nextWindowId();
	}

	public void setInventory(InventoryBase... inventories) {
		if (this.inventory != null) {
			this.inventory.stopWatching();
			this.inventory.removeViewer(this);
		}
		this.inventory = new InventoryBundle(inventories);
		this.inventory.startWatching();
		this.inventory.addViewer(this);
	}

	public InventoryBundle getInventory() {
		return this.inventory;
	}

	public int getId() {
		return this.id;
	}

	public boolean hasCloseMessage() {
		return true;
	}

	public int getInstanceId() {
		return this.instanceId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Player getPlayer() {
		return this.owner.getPlayer();
	}

	public VanillaPlayer getOwner() {
		return this.owner;
	}

	/**
	 * Opens this window
	 */
	public void open() {
		sendPacket(this.getPlayer(), new OpenWindowMessage(this.getInstanceId(), this.getId(), this.getTitle(), this.inventory.getSize()));
		this.inventory.notifyViewers(this.inventory.getContents());
		this.onOpened();
	}

	/**
	 * Closes this window
	 */
	public void close() {
		if (this.hasCloseMessage()) {
			sendPacket(this.getPlayer(), new CloseWindowMessage(this.getInstanceId()));
		}
		this.inventory.removeViewer(this);
		this.inventory.stopWatching();
		this.onClosed();
	}

	/**
	 * Called after this inventory has been successfully opened
	 */
	public void onOpened() {

	}

	/**
	 * Called after this inventory has been successfully closed
	 */
	public void onClosed() {

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

	public void setSlotConversionArray(int[] conversionArray) {
		this.slotConversionArray = conversionArray;
	}

	public int getMCSlotIndex(int spoutSlotIndex) {
		if (this.slotConversionArray == null || spoutSlotIndex < 0 || spoutSlotIndex >= slotConversionArray.length) {
			return -1;
		} else {
			return this.slotConversionArray[spoutSlotIndex];
		}
	}

	public int getSpoutSlotIndex(int mcSlotIndex) {
		if (this.slotConversionArray != null) {
			for (int i = 0; i < this.slotConversionArray.length; i++) {
				if (this.slotConversionArray[i] == mcSlotIndex) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * Called when the player clicks outside the window
	 * @return True to notify that the operation was allowed
	 */
	public boolean onOutSideClick() {
		if (this.hasItemOnCursor()) {
			//TODO: Drop the item at the cursor
			this.setItemOnCursor(null);
		}
		return true;
	}

	/**
	 * Called when the player left or right clicks on an item while holding shift
	 * @param clickedSlot
	 * @return True to notify that the operation was allowed
	 */
	public boolean onShiftClick(int clickedSlot) {
		return false; //TODO: Implement shift-transferring
	}

	/**
	 * Called when the player left-clicks on a slot without holding shift
	 * @param clickedSlot
	 * @return True to notify that the operation was allowed
	 */
	public boolean onLeftClick(int clickedSlot) {
		ItemStack clickedItem = this.inventory.getItem(clickedSlot);
		if (this.hasItemOnCursor()) {
			if (clickedItem == null) {
				//cursor > clicked item
				this.inventory.setItem(clickedSlot, this.getItemOnCursor());
				this.setItemOnCursor(null);
				return true;
			} else {
				//clicked item + cursor
				ItemStack cursorItem = this.getItemOnCursor();
				if (cursorItem.equalsIgnoreSize(clickedItem)) {
					//stack
					clickedItem.stack(cursorItem);
					this.inventory.setItem(clickedSlot, clickedItem);
					this.setItemOnCursor(cursorItem.getAmount() <= 0 ? null : cursorItem);
					return true;
				} else {
					//swap
					this.setItemOnCursor(clickedItem);
					this.inventory.setItem(clickedSlot, cursorItem);
					return true;
				}
			}
		} else if (clickedItem != null) {
			//clicked item > cursor
			this.setItemOnCursor(clickedItem);
			this.inventory.setItem(clickedSlot, null);
			return true;
		}
		return true; //both null - do something here?
	}

	/**
	 * Called when the player right-clicks on a slot without holding shift
	 * @param clickedSlot
	 * @return True to notify that the operation was allowed
	 */
	public boolean onRightClick(int clickedSlot) {
		ItemStack clickedItem = this.inventory.getItem(clickedSlot);
		if (this.hasItemOnCursor()) {
			if (clickedItem == null) {
				//cursor > clicked item
				ItemStack cursorItem = this.getItemOnCursor();
				clickedItem = cursorItem.clone();
				clickedItem.setAmount(1);
				cursorItem.setAmount(cursorItem.getAmount() - clickedItem.getAmount());
				this.inventory.setItem(clickedSlot, clickedItem);
				this.setItemOnCursor(cursorItem.getAmount() <= 0 ? null : cursorItem);
				return true;
			} else {
				//clicked item + cursor
				ItemStack cursorItem = this.getItemOnCursor();
				if (cursorItem.equalsIgnoreSize(clickedItem)) {
					//transfer one item
					if (clickedItem.getAmount() < clickedItem.getMaxStackSize()) {
						clickedItem.setAmount(clickedItem.getAmount() + 1);
						cursorItem.setAmount(cursorItem.getAmount() - 1);
						this.inventory.setItem(clickedSlot, clickedItem);
						this.setItemOnCursor(cursorItem.getAmount() <= 0 ? null : cursorItem);
						return true;
					} else {
						return false;
					}
				} else {
					//swap
					this.setItemOnCursor(clickedItem);
					this.inventory.setItem(clickedSlot, cursorItem);
					return true;
				}
			}
		} else if (clickedItem != null) {
			//1/2 clicked item > cursor
			ItemStack newItem = clickedItem.clone();
			newItem.setAmount(newItem.getAmount() / 2);
			clickedItem.setAmount(clickedItem.getAmount() - newItem.getAmount());
			this.inventory.setItem(clickedSlot, newItem.getAmount() <= 0 ? null : newItem);
			this.setItemOnCursor(clickedItem.getAmount() <= 0 ? null : clickedItem);
			return true;
		}
		return true; //both null - do something here?
	}

	/**
	 * Called when the player right-clicks on a slot in this window
	 * @param slotIndex
	 * @param shift     whether shift was pressed
	 * @return True to notify that the operation was allowed
	 */
	public boolean onRightClick(int clickedSlot, boolean shift) {
		if (shift) {
			return this.onShiftClick(clickedSlot);
		} else {
			return this.onRightClick(clickedSlot);
		}
	}

	/**
	 * Called when the player left-clicks on a slot in this window
	 * @param slotIndex
	 * @param shift     whether shift was pressed
	 * @return True to notify that the operation was allowed
	 */
	public boolean onLeftClick(int clickedSlot, boolean shift) {
		if (shift) {
			return this.onShiftClick(clickedSlot);
		} else {
			return this.onLeftClick(clickedSlot);
		}
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		this.getPlayer().getNetworkSynchronizer().onSlotSet(inventory, slot, item);
	}

	@Override
	public void updateAll(InventoryBase inventory, ItemStack[] slots) {
		this.getPlayer().getNetworkSynchronizer().updateAll(inventory, slots);
	}
}
