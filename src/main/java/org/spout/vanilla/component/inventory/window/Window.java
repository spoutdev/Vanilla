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
package org.spout.vanilla.component.inventory.window;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.spout.api.Spout;
import org.spout.api.component.Component;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.event.window.WindowCloseEvent;
import org.spout.vanilla.event.window.WindowItemsEvent;
import org.spout.vanilla.event.window.WindowOpenEvent;
import org.spout.vanilla.event.window.WindowPropertyEvent;
import org.spout.vanilla.event.window.WindowSlotEvent;
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;
import org.spout.vanilla.inventory.util.GridInventoryConverter;
import org.spout.vanilla.inventory.util.InventoryConverter;
import org.spout.vanilla.inventory.window.ClickArguments;
import org.spout.vanilla.inventory.window.InventoryEntry;
import org.spout.vanilla.inventory.window.WindowType;
import org.spout.vanilla.inventory.window.prop.WindowProperty;
import org.spout.vanilla.util.InventoryUtil;

public class Window extends EntityComponent implements InventoryViewer {
	private final Set<InventoryConverter> converters = new HashSet<InventoryConverter>();
	protected final TObjectIntMap<WindowProperty> properties = new TObjectIntHashMap<WindowProperty>();
	protected final int id = InventoryUtil.nextWindowId();
	protected int offset;
	protected WindowType type;
	protected boolean opened;
	protected String title;
	protected ItemStack cursorItem;

	static {
		Component.addDependency(Window.class, Human.class);
		Component.addDependency(DefaultWindow.class, Human.class);
	}
	
	@Override
	public void onAttached() {
		if (!(getHolder() instanceof Player)) {
			throw new IllegalStateException("A Window may only be attached to a player.");
		}
	}

	@Override
	public void onDetached() {
		removeAllInventoryConverters();
		close();
		getHolder().add(DefaultWindow.class);
	}

	@Override
	public void onSlotSet(Inventory inventory, int slot, ItemStack item) {
		InventoryConverter slots = getInventoryConverter(inventory);
		System.out.println("Slot set");
		if (slots != null) {
			slot = slots.getNativeSlot(slot);
			System.out.println("Updating slot");
			getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowSlotEvent(this, inventory, slot, item));
		}
	}

	@Override
	public boolean canTick() {
		return false;
	}

	public Window init(WindowType type, String title, int offset) {
		this.type = type;
		this.title = title;
		this.offset = offset;
		PlayerInventory inventory = getHuman().getInventory();
		GridInventoryConverter main = new GridInventoryConverter(inventory.getMain(), 9, offset);
		addInventoryConverter(main);
		addInventoryConverter(new GridInventoryConverter(inventory.getQuickbar(), 9, offset + main.getGrid().getSize()));
		return this;
	}

	public Window init(WindowType type, String title) {
		return init(type, title, 0);
	}

	public void open() {
		opened = true;
		reload();
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowOpenEvent(this));
	}

	public void close() {
		opened = false;
		//if (getHuman().isSurvival()) {
			//dropCursorItem();
		//}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowCloseEvent(this));
	}

	public void reload() {
		ItemStack[] items = new ItemStack[getInventorySize()];
		int nativeSlot;
		for (InventoryConverter converter : converters) {
			Inventory inventory = converter.getInventory();
			for (int i = 0; i < inventory.size(); i++) {
				nativeSlot = converter.getNativeSlot(i);
				if (nativeSlot < 0 || nativeSlot >= items.length) {
					continue;
				}
				items[nativeSlot] = inventory.get(i);
			}
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowItemsEvent(this, items));
	}

	public boolean onShiftClick(ItemStack stack, int slot, Inventory from) {
		PlayerInventory inventory = getHuman().getInventory();
		PlayerMainInventory main = inventory.getMain();
		GridInventoryConverter converter = (GridInventoryConverter) getInventoryConverter(main);
		if (converter == null) {
			return false;
		}
		if (from instanceof PlayerQuickbar) {
			main.add(converter.getSlot(offset), stack);
			return true;
		} else if (from instanceof PlayerMainInventory) {
			PlayerQuickbar quickbar = inventory.getQuickbar();
			InventoryConverter quickbarConverter = getInventoryConverter(quickbar);
			if (quickbarConverter == null) {
				return false;
			}
			quickbar.add(converter.getSlot(offset + converter.getGrid().getSize()), stack);
			return true;
		}
		from.set(slot, stack);
		return false;
	}

	public boolean onClick(ClickArguments args) {
		Inventory inventory = args.getInventory();
		int slot = args.getSlot();
		System.out.println("Spout slot: " + slot);
		ItemStack clicked = inventory.get(slot);
		if (args.isShiftClick()) {
			System.out.println("Shift clicked");
			if (clicked != null) {
				return onShiftClick(clicked, slot, inventory);
			}
		} else if (args.isRightClick()) {
			System.out.println("Right onClick");
			if (clicked == null) {
				System.out.println("Empty slot");
				if (cursorItem != null) {
					System.out.println("Cursor: " + cursorItem.getMaterial().getName());
					System.out.println("Add one");
					// slot is empty with a not empty cursor
					// add one
					clicked = cursorItem.clone();
					clicked.setAmount(1);
					inventory.set(slot, clicked);
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
						inventory.set(slot, clicked);
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
					inventory.set(slot, cursorItem);
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
				inventory.set(slot, clicked);
				// cursor gets any remainder
				cursorItem = clicked.clone();
				cursorItem.setAmount(y + z);
				return true;
			}
		} else {
			System.out.println("Left onClick");
			if (clicked == null) {
				System.out.println("Empty slot");
				if (cursorItem != null) {
					System.out.println("Cursor: " + cursorItem.getMaterial().getName());
					System.out.println("Put whole stack in slot");
					// slot is empty; cursor is not empty.
					// put whole stack down
					clicked = cursorItem.clone();
					inventory.set(slot, clicked);
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
					inventory.set(slot, clicked);
					if (cursorItem.isEmpty()) {
						cursorItem = null;
					}
					return true;
				} else {
					System.out.println("Materials don't match. Swapping stacks.");
					// materials don't match
					// swap stacks
					ItemStack newCursor = clicked.clone();
					inventory.set(slot, cursorItem);
					cursorItem = newCursor;
				}
			} else {
				System.out.println("Clicked: " + clicked.getMaterial().getName());
				System.out.println("Empty cursor");
				// slot is not empty; cursor is empty.
				// pick up stack
				cursorItem = clicked.clone();
				inventory.set(slot, null);
				return true;
			}
		}
		return false;
	}

	public void onCreativeClick(Inventory inventory, int clickedSlot, ItemStack item) {
		cursorItem = null;
		inventory.set(clickedSlot, item);
	}

	public boolean onOutsideClick() {
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
		for (InventoryConverter converter : converters) {
			size += converter.getInventory().size();
		}
		return size;
	}

	public boolean isOpened() {
		return opened;
	}

	public InventoryEntry getInventoryEntry(int nativeSlot) {
		int slot;
		for (InventoryConverter converter : converters) {
			slot = converter.getSlot(nativeSlot);
			if (slot != -1) {
				return new InventoryEntry(converter.getInventory(), slot);
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

	public InventoryConverter getInventoryConverter(Inventory inventory) {
		for (InventoryConverter converter : converters) {
			if (converter.getInventory().equals(inventory)) {
				return converter;
			}
		}
		return null;
	}

	public Set<InventoryConverter> getInventoryConverters() {
		return converters;
	}

	public void addInventoryConverter(InventoryConverter converter) {
		converter.getInventory().addViewer(this);
		converters.add(converter);
	}

	public void removeInventoryConverter(InventoryConverter converter) {
		converter.getInventory().removeViewer(this);
		converters.remove(converter);
	}

	public void removeAllInventoryConverters() {
		Iterator<InventoryConverter> i = converters.iterator();
		while (i.hasNext()) {
			InventoryConverter converter = i.next();
			converter.getInventory().removeViewer(this);
			i.remove();
		}
	}

	public void setProperty(WindowProperty prop, int value) {
		properties.put(prop, value);
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new WindowPropertyEvent(this, prop.getId(), value));
	}

	public int getProperty(WindowProperty prop) {
		return properties.get(prop);
	}

	public int getId() {
		return id;
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
