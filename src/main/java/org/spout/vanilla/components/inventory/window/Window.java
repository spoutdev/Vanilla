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
package org.spout.vanilla.components.inventory.window;

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
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.components.inventory.PlayerInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.util.InventoryUtil;
import org.spout.vanilla.inventory.util.SlotIndexCollection;
import org.spout.vanilla.inventory.window.ClickArguments;
import org.spout.vanilla.inventory.window.InventoryEntry;
import org.spout.vanilla.inventory.window.WindowType;
import org.spout.vanilla.inventory.util.SlotIndexGrid;

public abstract class Window extends EntityComponent implements InventoryViewer {
	private static final SlotIndexGrid MAIN = new SlotIndexGrid(9, 3);
	private static final SlotIndexGrid QUICK_BAR = new SlotIndexGrid(9, 1);
	protected final TIntObjectMap<ItemStack> queuedInventoryUpdates = new TIntObjectHashMap<ItemStack>();
	protected final Map<InventoryBase, SlotIndexCollection> inventories = new HashMap<InventoryBase, SlotIndexCollection>();
	protected final int instanceId = InventoryUtil.nextWindowId();
	protected int offset;
	protected WindowType type;
	protected boolean opened;
	protected String title;
	protected ItemStack cursorItem;

	@Override
	public void onAttached() {
		if (!(getHolder() instanceof Player)) {
			throw new IllegalStateException("A Window may only be attached to a player.");
		}
		PlayerInventory inventory = getHuman().getInventory();
		inventories.put(inventory.getMain(), MAIN.translate(offset));
		inventories.put(inventory.getQuickbar(), QUICK_BAR.translate(offset + MAIN.getGrid().getSize()));
	}

	@Override
	public void onDetached() {
		close();
		getHolder().add(DefaultWindow.class);
	}

	@Override
	public void onSlotSet(InventoryBase inventory, int slot, ItemStack item) {
		// convert slots and update
		SlotIndexCollection slots = this.inventories.get(inventory);
		if (slots != null) {
			slot = slots.getNativeSlot(slot);
			queuedInventoryUpdates.put(slot, item);
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

	public void init(WindowType type, String title, int offset) {
		this.type = type;
		this.title = title;
		this.offset = offset;
	}

	public void open() {
		opened = true;
		for (InventoryBase inventory : inventories.keySet()) {
			inventory.addViewer(this);
		}
		reload();
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowOpenEvent(this));
	}

	public void close() {
		opened = false;
		for (InventoryBase inventory : inventories.keySet()) {
			inventory.removeViewer(this);
		}
		if (getHuman().isSurvival()) {
			dropCursorItem();
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowCloseEvent(this));
	}

	public void reload() {
		ItemStack[] items = new ItemStack[getInventorySize()];
		int nativeSlot;
		for (Entry<InventoryBase, SlotIndexCollection> entry : inventories.entrySet()) {
			InventoryBase inventory = entry.getKey();
			for (int i = 0; i < inventory.getSize(); i++) {
				nativeSlot = entry.getValue().getNativeSlot(i);
				if (nativeSlot < 0 || nativeSlot >= items.length) {
					continue;
				}
				items[nativeSlot] = inventory.getItem(i);
			}
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowItemsEvent(this, items));
	}

	public boolean shiftClick(ItemStack stack, int slot, InventoryBase from, InventoryBase to) {
		// look for the first available slot in the inventory
		for (int i = 0; i < to.getSize(); i++) {
			ItemStack index = to.getItem(i);
			if (index == null) {
				// put clicked item in empty slot
				to.setItem(i, stack);
				return true;
			}
			if (!index.equalsIgnoreSize(stack)) {
				// can't stack different materials
				continue;
			}
			index.stack(stack);
			to.setItem(i, index);
			from.setItem(slot, stack);
			if (stack.isEmpty()) {
				return true;
			}
		}
		return true;
	}

	public boolean click(ClickArguments args) {
		InventoryBase inventory = args.getInventory();
		int slot = args.getSlot();
		System.out.println("Spout slot: " + slot);
		ItemStack clicked = inventory.getItem(slot);
		if (args.isShiftClick()) {
			System.out.println("Shift clicked");
			if (clicked != null) {
				System.out.println("Slot: " + clicked.getMaterial().getName());
				PlayerInventory playerInventory = getHuman().getInventory();
				if (inventory instanceof PlayerQuickbar) {
					System.out.println("To main");
					return shiftClick(clicked, slot, inventory, playerInventory.getMain());
				} else if (inventory instanceof PlayerMainInventory) {
					System.out.println("To quickbar");
					return shiftClick(clicked, slot, inventory, playerInventory.getQuickbar());
				}
			}
		} else if (args.isRightClick()) {
			System.out.println("Right click");
			if (clicked == null) {
				System.out.println("Empty slot");
				if (cursorItem != null) {
					System.out.println("Cursor: " + cursorItem.getMaterial().getName());
					System.out.println("Add one");
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
				System.out.println("Slot: " + clicked.getMaterial().getName());
				System.out.println("Cursor: " + cursorItem.getMaterial().getName());
				// slot is not empty with not empty cursor
				if (cursorItem.equalsIgnoreSize(clicked)) {
					// only stack materials that are the same
					if (clicked.getMaxStackSize() > clicked.getAmount()) {
						System.out.println("Stacking");
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
					System.out.println("Materials don't match. Swapping stacks.");
					// materials don't match
					// swap stacks
					ItemStack newCursor = clicked.clone();
					inventory.setItem(slot, cursorItem);
					cursorItem = newCursor;
					return true;
				}
			} else {
				System.out.println("Slot: " + clicked.getMaterial().getName());
				System.out.println("Empty cursor");
				// slot is not empty with an empty cursor
				// split the stack
				int x = clicked.getAmount();
				int y = x / 2;
				int z = x % 2;
				clicked.setAmount(y);
				inventory.setItem(slot, clicked);
				// cursor gets any remainder
				cursorItem = clicked.clone();
				cursorItem.setAmount(y + z);
				return true;
			}
		} else {
			System.out.println("Left click");
			if (clicked == null) {
				System.out.println("Empty slot");
				if (cursorItem != null) {
					System.out.println("Cursor: " + cursorItem.getMaterial().getName());
					System.out.println("Put whole stack in slot");
					// slot is empty; cursor is not empty.
					// put whole stack down
					clicked = cursorItem.clone();
					inventory.setItem(slot, clicked);
					cursorItem = null;
					return true;
				}
			} else if (cursorItem != null) {
				System.out.println("Clicked: " + clicked.getMaterial().getName());
				System.out.println("Cursor: " + cursorItem.getMaterial().getName());
				// slot is not empty; cursor is not empty.
				// stack
				if (cursorItem.equalsIgnoreSize(clicked)) {
					System.out.println("Stacking");
					clicked.stack(cursorItem);
					inventory.setItem(slot, clicked);
					if (cursorItem.isEmpty()) {
						cursorItem = null;
					}
					return true;
				} else {
					System.out.println("Materials don't match. Swapping stacks.");
					// materials don't match
					// swap stacks
					ItemStack newCursor = clicked.clone();
					inventory.setItem(slot, cursorItem);
					cursorItem = newCursor;
				}
			} else {
				System.out.println("Clicked: " + clicked.getMaterial().getName());
				System.out.println("Empty cursor");
				// slot is not empty; cursor is empty.
				// pick up stack
				cursorItem = clicked.clone();
				inventory.setItem(slot, null);
				return true;
			}
		}
		return false;
	}

	public void creativeClick(InventoryBase inventory, int clickedSlot, ItemStack item) {
		cursorItem = null;
		inventory.setItem(clickedSlot, item);
	}

	public boolean outsideClick() {
		dropCursorItem();
		return true;
	}


	public void dropCursorItem() {
		if (cursorItem != null) {
			getHuman().dropItem(cursorItem);
			cursorItem = null;
		}
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
			System.out.println("For: " + entry.getKey().getClass().getCanonicalName());
			slot = entry.getValue().getSlot(nativeSlot);
			if (slot != -1) {
				return new InventoryEntry(entry.getKey(), slot);
			}
		}
		return null;
	}

	public ClickArguments getClickArguments(int nativeSlot, boolean rightClick, boolean shiftClick) {
		InventoryEntry entry = getInventoryEntry(nativeSlot);
		if (entry != null) {
			return new ClickArguments(entry.getInventory(), entry.getSlot(), rightClick, shiftClick);
		}
		return null;
	}

	public Player getPlayer() {
		return (Player) getHolder();
	}

	public Human getHuman() {
		return getHolder().get(Human.class);
	}

	public Map<InventoryBase, SlotIndexCollection> getInventories() {
		return inventories;
	}

	public void addInventory(InventoryBase inventory, SlotIndexCollection slots) {
		inventories.put(inventory, slots);
	}

	public void removeInventory(InventoryBase inventory) {
		inventories.remove(inventory);
	}

	public int getInstanceId() {
		return instanceId;
	}

	public WindowType getType() {
		return type;
	}

	public void setType(WindowType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean hasCursorItem() {
		return cursorItem != null;
	}

	public ItemStack getCursorItem() {
		return cursorItem;
	}

	public void setCursorItem(ItemStack cursorItem) {
		this.cursorItem = cursorItem;
	}
}
