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
import java.util.logging.Level;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.spout.api.Spout;
import org.spout.api.component.Component;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.api.protocol.event.ProtocolEvent;

import org.spout.vanilla.component.inventory.PlayerInventory;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.event.entity.EntityEquipmentEvent;
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

/**
 * Represents a Window that players can view to display an inventory.
 */
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

	/**
	 * Initializes this window to the specified type, title, and offset of first main slot.
	 *
	 * @param type of window
	 * @param title of window
	 * @param offset of first slot
	 * @return this
	 */
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

	/**
	 * Initializes this window to the specified type and title.
	 *
	 * @param type of window
	 * @param title of window
	 * @return this
	 */
	public Window init(WindowType type, String title) {
		return init(type, title, 0);
	}

	/**
	 * Opens the window on the client.
	 */
	public void open() {
		opened = true;
		reload();
		callProtocolEvent(new WindowOpenEvent(this));
	}

	/**
	 * Closes the window on the client.
	 */
	public void close() {
		opened = false;
		if (getHuman().isSurvival()) {
			dropCursorItem();
		}
		callProtocolEvent(new WindowCloseEvent(this));
	}

	/**
	 * Reloads all inventories that this window is watching to reflect on the
	 * client.
	 */
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
		callProtocolEvent(new WindowItemsEvent(this, items));
	}

	/**
	 * Called when a client clicks the window while holding shift.
	 *
	 * @param stack to manipulate
	 * @param slot clicked
	 * @param from inventory slot was in
	 * @return true if click was successful
	 */
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

	/**
	 * Called when the client clicks a window.
	 *
	 * @param args {@link ClickArguments} of the session
	 * @return true if click is allowed
	 */
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

	/**
	 * Called when the client clicks the window while in creative mode.
	 *
	 * @param inventory clicked
	 * @param clickedSlot slot clicked
	 * @param item item that was put in quickbar
	 */
	public void onCreativeClick(Inventory inventory, int clickedSlot, ItemStack item) {
		cursorItem = null;
		inventory.set(clickedSlot, item);
	}

	/**
	 * Called when the client clicks outside the bounds of a window
	 *
	 * @return true if click is permitted
	 */
	public boolean onOutsideClick() {
		dropCursorItem();
		return true;
	}

	/**
	 * Drops the current item on the cursor into the world.
	 */
	public void dropCursorItem() {
		if (cursorItem != null) {
			getHuman().dropItem(cursorItem);
			cursorItem = null;
		}
	}

	/**
	 * Gets the combined size of all watched inventories.
	 *
	 * @return size of all watching inventories
	 */
	public int getInventorySize() {
		int size = 0;
		for (InventoryConverter converter : converters) {
			size += converter.getInventory().size();
		}
		return size;
	}

	/**
	 * Returns true if the window is currently opened.
	 *
	 * @return true if opened
	 */
	public boolean isOpened() {
		return opened;
	}

	/**
	 * Returns an inventory entry with the inventory that the native slot
	 * is mapped to for this window.
	 *
	 * @param nativeSlot clicked
	 * @return inventory that was clicked
	 */
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

	/**
	 * Returns the click arguments to send to
	 * {@link #onClick(org.spout.vanilla.inventory.window.ClickArguments)}.
	 *
	 * @param nativeSlot slot clicked
	 * @param rightClick true if client right clicked window
	 * @param shiftClick true if client was holding down shift
	 * @return new click arguments to send to click call
	 */
	public ClickArguments getClickArguments(int nativeSlot, boolean rightClick, boolean shiftClick) {
		InventoryEntry entry = getInventoryEntry(nativeSlot);
		if (entry != null) {
			return new ClickArguments(entry.getInventory(), entry.getSlot(), rightClick, shiftClick);
		}
		return null;
	}

	/**
	 * Gets the holder of this window casted to a player.
	 *
	 * @return player
	 */
	public Player getPlayer() {
		return (Player) getOwner();
	}

	/**
	 * Gets the Human component of the holder
	 *
	 * @return human
	 */
	public Human getHuman() {
		return getOwner().add(Human.class);
	}

	/**
	 * Gets an inventory converter from the specified inventory
	 *
	 * @param inventory to search for
	 * @return converter with specified inventory
	 */
	public InventoryConverter getInventoryConverter(Inventory inventory) {
		for (InventoryConverter converter : converters) {
			if (converter.getInventory().equals(inventory)) {
				return converter;
			}
		}
		return null;
	}

	/**
	 * Returns all inventory converters the window is watching
	 *
	 * @return all converters
	 */
	public Set<InventoryConverter> getInventoryConverters() {
		return converters;
	}

	/**
	 * Adds an inventory converter to start watching.
	 *
	 * @param converter
	 */
	public void addInventoryConverter(InventoryConverter converter) {
		converter.getInventory().addViewer(this);
		converters.add(converter);
	}

	/**
	 * Removes an inventory converter to stop watching/
	 *
	 * @param converter
	 */
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

	/**
	 * Sets a property of the window on the client.
	 *
	 * @param prop of the window
	 * @param value of the property
	 */
	public void setProperty(WindowProperty prop, int value) {
		properties.put(prop, value);
		callProtocolEvent(new WindowPropertyEvent(this, prop.getId(), value));
	}

	/**
	 * Returns the property of the window.
	 *
	 * @param prop of the window
	 * @return value of specified property
	 */
	public int getProperty(WindowProperty prop) {
		return properties.get(prop);
	}

	/**
	 * Returns the unique id of this window.
	 *
	 * @return id of the window
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the {@link WindowType} of this window.
	 *
	 * @return type of window
	 */
	public WindowType getType() {
		return type;
	}

	/**
	 * Sets the type of this window. Changes will not be staged until window is
	 * re-opened.
	 *
	 * @param type to set
	 */
	public void setType(WindowType type) {
		this.type = type;
	}

	/**
	 * Returns the title displayed on the window.
	 *
	 * @return title displayed on window
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title displayed on this window. Changes will not be staged
	 * until window is re-opened.
	 *
	 * @param title to display
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns true if there is currently an {@link ItemStack} on the client's
	 * cursor.
	 *
	 * @return true if item on cursor
	 */
	public boolean hasCursorItem() {
		return cursorItem != null;
	}

	/**
	 * Returns the {@link ItemStack} on the client's cursor.
	 *
	 * @return item on cursor
	 */
	public ItemStack getCursorItem() {
		return cursorItem;
	}

	/**
	 * Sets the current {@link ItemStack} on the cursor. Note that setting this
	 * does not actually set the item on the client but rather is just for
	 * internal use only for keeping track of the cursor item server side.
	 * The server cannot set the cursor item on the client.
	 *
	 * @param cursorItem to set
	 */
	public void setCursorItem(ItemStack cursorItem) {
		this.cursorItem = cursorItem;
	}

	/**
	 * Attempts to call a {@link ProtocolEvent} for the player.
	 *
	 * @param event to call
	 */
	protected void callProtocolEvent(ProtocolEvent event) {
		if (getPlayer() == null) {
			if (Spout.debugMode()) {
				Spout.getLogger().log(Level.WARNING, "Sending protocol message with null player");
			}
			return;
		}
		if (getPlayer().getNetworkSynchronizer() == null) {
			if (Spout.debugMode()) {
				Spout.getLogger().log(Level.WARNING, "Sending protocol message with null network synchronizer");
			}
			return;
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(event);
	}

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("A Window may only be attached to a player.");
		}
	}

	@Override
	public void onDetached() {
		removeAllInventoryConverters();
		close();
		getOwner().add(DefaultWindow.class);
	}

	@Override
	public void onSlotSet(Inventory inventory, int slot, ItemStack item) {
		InventoryConverter slots = getInventoryConverter(inventory);
		System.out.println("Slot set");
		if (slots != null) {
			int nativeSlot = slots.getNativeSlot(slot);
			System.out.println("Updating slot");
			callProtocolEvent(new WindowSlotEvent(this, inventory, nativeSlot, item));
		}

		// update held item
		PlayerQuickbar quickbar = getHuman().getInventory().getQuickbar();
		System.out.println("Slot: " + slot);
		System.out.println("Current slot: " + quickbar.getCurrentSlot());
		if (inventory instanceof PlayerQuickbar && slot == quickbar.getCurrentSlot()) {
			System.out.println("Rendering held item");
			Player player = getPlayer();
			player.getNetwork().callProtocolEvent(new EntityEquipmentEvent(player, 0, item));
		}
	}

	@Override
	public boolean canTick() {
		return false;
	}
}
