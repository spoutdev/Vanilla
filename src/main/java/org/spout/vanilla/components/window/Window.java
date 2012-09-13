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
package org.spout.vanilla.components.window;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.inventory.InventoryBase;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.components.living.Human;
import org.spout.vanilla.event.window.WindowCloseEvent;
import org.spout.vanilla.event.window.WindowItemsEvent;
import org.spout.vanilla.event.window.WindowOpenEvent;
import org.spout.vanilla.event.window.WindowSlotEvent;
import org.spout.vanilla.util.InventoryUtil;
import org.spout.vanilla.util.intmap.SlotIndexCollection;
import org.spout.vanilla.window.ClickArguments;
import org.spout.vanilla.window.InventoryEntry;
import org.spout.vanilla.window.WindowType;

public abstract class Window extends EntityComponent implements InventoryViewer {
	private final TIntObjectMap<ItemStack> queuedInventoryUpdates = new TIntObjectHashMap<ItemStack>();
	private final Map<InventoryBase, SlotIndexCollection> inventories = new HashMap<InventoryBase, SlotIndexCollection>();
	private int instanceId = InventoryUtil.nextWindowId();
	private WindowType type = WindowType.DEFAULT;
	private boolean opened = false;
	private String title = null;
	private ItemStack cursorItem = null;

	@Override
	public void onAttached() {
		if (!(getHolder() instanceof Player)) {
			throw new IllegalStateException("A Window may only be attached to a player.");
		}
	}

	@Override
	public void onDetached() {
		queuedInventoryUpdates.clear();
		inventories.clear();
		instanceId = 0;
		type = WindowType.DEFAULT;
		title = null;
		cursorItem = null;
		super.onDetached();
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		// convert slots and update
		SlotIndexCollection slots = this.inventories.get(inventory);
		if (slots != null) {
			slot = slots.getMinecraftSlot(slot);
			this.queuedInventoryUpdates.put(slot, item);
		}
	}

	@Override
	public boolean canTick() {
		return opened;
	}

	@Override
	public void onTick(float dt) {
		if (queuedInventoryUpdates.size() < 1) {
			return;
		}
		TIntObjectIterator<ItemStack> iter = queuedInventoryUpdates.iterator();
		while (iter.hasNext()) {
			iter.advance();
			int slot = iter.key();
			InventoryEntry entry = getInventoryEntry(slot);
			if (entry == null) {
				continue;
			}
			getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowSlotEvent(this, entry.getInventory(), entry.getSlot(), slot, iter.value()));
		}
		queuedInventoryUpdates.clear();
	}

	public Window open() {
		opened = true;
		for (InventoryBase inventory : inventories.keySet()) {
			inventory.addViewer(this);
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowOpenEvent(this));
		reload();
		return this;
	}

	public Window close() {
		opened = false;
		for (InventoryBase inventory : inventories.keySet()) {
			inventory.removeViewer(this);
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowCloseEvent(this));
		return this;
	}

	public Window reload() {
		ItemStack[] items = new ItemStack[getInventorySize()];
		int nativeSlot;
		for (Entry<InventoryBase, SlotIndexCollection> entry : inventories.entrySet()) {
			InventoryBase inventory = entry.getKey();
			for (int i = 0; i < inventory.getSize(); i++) {
				nativeSlot = entry.getValue().getMinecraftSlot(i);
				if (nativeSlot < 0 || nativeSlot >= items.length) {
					continue;
				}
				items[nativeSlot] = inventory.getItem(i);
			}
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowItemsEvent(this, items));
		return this;
	}

	public boolean click(InventoryBase inventory, int slot, ClickArguments args) {
		ItemStack clicked = inventory.getItem(slot);
		if (args.isShiftClick()) {
			if (clicked != null) {
				// look for the first available slot
				for (int i = 0; i < inventory.getSize(); i++) {
					ItemStack index = inventory.getItem(i);
					if (index == null) {
						// put clicked item in empty slot
						inventory.setItem(i, clicked);
						return true;
					}
					if (!index.equalsIgnoreSize(clicked)) {
						// can't stack different materials
						continue;
					}
					// stack, return if the clicked item is empty
					index.stack(clicked);
					inventory.setItem(i, index);
					if (clicked.isEmpty()) {
						return true;
					}
				}
			}
		} else if (args.isRightClick()) {
			if (clicked == null) {
				if (cursorItem != null) {
					// slot is empty with a not empty cursor
					// add one
					clicked = cursorItem.clone();
					clicked.setAmount(1);
					inventory.setItem(slot, clicked);
					// remove from cursor
					cursorItem.setAmount(cursorItem.getAmount() - 1);
					if (cursorItem.isEmpty()) {
						cursorItem = null;
					}
					return true;
				}
			} else if (cursorItem != null) {
				// slot is not empty with not empty cursor
				if (cursorItem.equalsIgnoreSize(clicked)) {
					// only stack materials that are the same
					if (clicked.getMaxStackSize() > clicked.getAmount()) {
						// add one if can fit
						clicked.setAmount(clicked.getAmount() + 1);
						inventory.setItem(slot, clicked);
						cursorItem.setAmount(cursorItem.getAmount() - 1);
						if (cursorItem.isEmpty()) {
							cursorItem = null;
						}
						return true;
					}
				} else {
					// materials don't match
					// swap stacks
					ItemStack newCursor = clicked.clone();
					inventory.setItem(slot, cursorItem);
					cursorItem = newCursor;
					return true;
				}
			} else {
				// slot is not empty with an empty cursor
				// split the stack
				int x = clicked.getAmount();
				int y = x / 2;
				int z = x % 2;
				clicked.setAmount(y);
				inventory.setItem(slot, clicked);
				// cursor gets any remainder
				cursorItem.setAmount(y + z);
				return true;
			}
		} else {
			if (clicked == null) {
				if (cursorItem != null) {
					// slot is empty; cursor is not empty.
					// put whole stack down
					clicked = cursorItem.clone();
					inventory.setItem(slot, clicked);
					cursorItem = null;
					return true;
				}
			} else if (cursorItem != null) {
				// slot is not empty; cursor is not empty.
				// stack
				if (cursorItem.equalsIgnoreSize(clicked)) {
					clicked.stack(cursorItem);
					inventory.setItem(slot, clicked);
					if (cursorItem.isEmpty()) {
						cursorItem = null;
					}
					return true;
				} else {
					// materials don't match
					// swap stacks
					ItemStack newCursor = clicked.clone();
					inventory.setItem(slot, cursorItem);
					cursorItem = newCursor;
				}
			} else {
				// slot is not empty; cursor is empty.
				// pick up stack
				cursorItem = clicked.clone();
				inventory.setItem(slot, null);
				return true;
			}
		}
		return false;
	}

	public Window onCreativeClick(InventoryBase inventory, int clickedSlot, ItemStack item) {
		this.setCursorItem(null);
		inventory.setItem(clickedSlot, item);
		return this;
	}

	public boolean onOutsideClick() {
		dropCursorItem();
		return true;
	}


	public Window dropCursorItem() {
		if (cursorItem != null) {
			getHolder().get(Human.class).dropItem(cursorItem);
			cursorItem = null;
		}
		return this;
	}

	public int getInventorySize() {
		int size = 0;
		for (InventoryBase inventory : this.inventories.keySet()) {
			size += inventory.getSize();
		}
		return size;
	}

	public boolean isOpened() {
		return opened;
	}

	public InventoryEntry getInventoryEntry(int nativeSlot) {
		int slot;
		for (Entry<InventoryBase, SlotIndexCollection> entry : inventories.entrySet()) {
			slot = entry.getValue().getSpoutSlot(nativeSlot);
			if (slot != -1) {
				return new InventoryEntry(entry.getKey(), slot);
			}
		}
		return null;
	}

	public Player getPlayer() {
		return (Player) getHolder();
	}

	public Map<InventoryBase, SlotIndexCollection> getInventories() {
		return inventories;
	}

	public Window addInventory(InventoryBase inventory, SlotIndexCollection slots) {
		inventories.put(inventory, slots);
		return this;
	}

	public Window removeInventory(InventoryBase inventory) {
		inventories.remove(inventory);
		return this;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public WindowType getType() {
		return type;
	}

	public Window setType(WindowType type) {
		this.type = type;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Window setTitle(String title) {
		this.title = title;
		return this;
	}

	public boolean hasCursorItem() {
		return cursorItem != null;
	}

	public ItemStack getCursorItem() {
		return cursorItem;
	}

	public Window setCursorItem(ItemStack cursorItem) {
		this.cursorItem = cursorItem;
		return this;
	}
}
