/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.inventory.window;

import java.util.logging.Level;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;

import org.spout.api.Platform;
import org.spout.api.ServerOnly;
import org.spout.api.entity.Player;
import org.spout.api.event.cause.PlayerCause;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.InventoryViewer;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.protocol.event.ProtocolEvent;

import org.spout.vanilla.component.entity.inventory.PlayerInventory;
import org.spout.vanilla.event.inventory.InventoryCanSetEvent;
import org.spout.vanilla.event.window.WindowItemsEvent;
import org.spout.vanilla.event.window.WindowPropertyEvent;
import org.spout.vanilla.inventory.window.prop.WindowProperty;

public abstract class AbstractWindow implements InventoryViewer {
	private final Player owner;
	protected final int offset;
	protected final String title;
	protected final WindowType type;
	protected final TObjectIntMap<Integer> properties = new TObjectIntHashMap<Integer>();
	private static int windowId = 0;
	protected int id = -1;
	protected ItemStack cursorItem;
	// Client only
	protected boolean shiftDown;

	public AbstractWindow(Player owner, WindowType type, String title, int offset) {
		this.owner = owner;
		this.type = type;
		this.title = title;
		this.offset = offset;

		switch (owner.getEngine().getPlatform()) {
			case PROXY:
			case SERVER:
				// Initialize the window id on the server
				id = windowId++;
				break;
		}
	}

	/**
	 * Gets the owner of this window
	 * @return player
	 */
	public final Player getPlayer() {
		return owner;
	}

	/**
	 * Returns the player inventory.
	 * @return player inventory
	 */
	public PlayerInventory getPlayerInventory() {
		return getPlayer().get(PlayerInventory.class);
	}

	/**
	 * Opens the window from the server on the client.
	 */
	public abstract void open();

	/**
	 * Closes this window
	 */
	public abstract void close();

	/**
	 * Reloads the window's items
	 */
	public final void reload() {
		ItemStack[] items = new ItemStack[getSize()];
		for (int i = 0; i < items.length; i++) {
			Slot entry = getSlot(i);
			if (entry != null) {
				items[i] = entry.get();
			}
		}

		callProtocolEvent(new WindowItemsEvent(this, items));
	}

	/**
	 * Handles a click when the shift button is held down
	 * @param stack clicked
	 * @param slot clicked
	 * @param from inventory with item
	 * @return true if successful
	 */
	public abstract boolean onShiftClick(ItemStack stack, int slot, Inventory from);

	/**
	 * Handles a click on the server or the client.
	 * @param args to handle
	 * @return true if successful
	 */
	public abstract boolean onClick(ClickArguments args);

	/**
	 * Handles a click on the creative message
	 * @param inventory clicked
	 * @param clickedSlot slot clicked
	 * @param item clicked
	 */
	public abstract void onCreativeClick(Inventory inventory, int clickedSlot, ItemStack item);

	/**
	 * Called when the cursor clicks outside of the window.
	 * @return true if successful
	 */
	public abstract boolean onOutsideClick();

	/**
	 * Drops the cursor
	 */
	@ServerOnly
	public abstract void dropCursorItem();

	/**
	 * Gets the number of slots on the window.
	 * @return size of window
	 */
	public abstract int getSize();

	/**
	 * Whether the window is currently being viewed.
	 * @return true if being viewed
	 */
	public abstract boolean isOpened();

	/**
	 * Gets the inventory at the specified native slot. Returns -1 if
	 * non-existent.
	 * @param nativeSlot clicked
	 * @return inventory entry at slot
	 */
	public abstract Slot getSlot(int nativeSlot);

	/**
	 * Arguments to handle
	 * @param nativeSlot
	 * @param rightClick
	 * @param shiftClick
	 * @return
	 */
	public abstract ClickArguments getClickArguments(int nativeSlot, boolean rightClick, boolean shiftClick);

	/**
	 * Sets a property of the window
	 * @param id of the property
	 * @param value value of property
	 */
	public void setProperty(int id, int value) {
		properties.put(id, value);
		switch (owner.getEngine().getPlatform()) {
			case PROXY:
			case SERVER:
				callProtocolEvent(new WindowPropertyEvent(this, id, value));
				break;
			case CLIENT:
				// TODO: Window properties
				break;
			default:
				throw new IllegalStateException("Unknown platform: " + owner.getEngine().getPlatform());
		}
	}

	/**
	 * Sets a property of the window.
	 * @param prop property to set
	 * @param value set
	 */
	public void setProperty(WindowProperty prop, int value) {
		setProperty(prop.getId(), value);
	}

	/**
	 * Returns the value of the specified property.
	 * @param prop
	 * @return value of property
	 */
	public int getProperty(WindowProperty prop) {
		return properties.get(prop);
	}

	/**
	 * Returns the id of the window.
	 * @return id window
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the type of the window.
	 * @return window type
	 */
	public WindowType getType() {
		return type;
	}

	/**
	 * Returns the title of the window.
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Whether or not there is an item on the cursor
	 * @return true if has item on cursor
	 */
	public boolean hasCursorItem() {
		return cursorItem != null;
	}

	/**
	 * Gets the item on the cursor
	 * @return item on cursor
	 */
	public ItemStack getCursorItem() {
		return cursorItem;
	}

	/**
	 * Sets the item on the cursor.
	 * @param cursorItem
	 */
	public void setCursorItem(ItemStack cursorItem) {
		this.cursorItem = cursorItem;
		if (owner.getEngine().getPlatform() == Platform.CLIENT) {
			// TODO: Attach item to cursor
		}
	}

	public void setShiftDown(boolean shiftDown) {
		this.shiftDown = shiftDown;
	}

	public boolean isShiftDown() {
		return shiftDown;
	}

	public boolean canTick() {
		return false;
	}

	protected void callProtocolEvent(ProtocolEvent event) {
		// TODO: C->S events
		if (getPlayer() == null) {
			debug(Level.WARNING, "Sending protocol message with null player");
			return;
		}
		if (getPlayer().getNetworkSynchronizer() == null) {
			debug(Level.WARNING, "Sending protocol message with null network synchronizer");
			return;
		}
		getPlayer().getNetworkSynchronizer().callProtocolEvent(event);
	}

	protected void debug(Level level, String msg) {
		if (owner.getEngine().debugMode()) {
			owner.getEngine().getLogger().log(level, msg);
		}
	}

	protected void debug(String msg) {
		if (owner.getEngine().debugMode()) {
			debug(Level.INFO, msg);
		}
	}

	/**
	 * Checks whether a certain slot can be set to the item specified<br>
	 * Fires the {@link InventoryCanSetEvent}
	 * @param inventory of the slot
	 * @param index of the slot
	 * @param item to set to
	 * @return True if the slot can be set to the item, False if not
	 */
	protected boolean canSet(Inventory inventory, int index, ItemStack item) {
		final boolean canSet = inventory.canSet(index, item);
		InventoryCanSetEvent event = owner.getEngine().getEventManager().callEvent(new InventoryCanSetEvent(inventory, new PlayerCause(getPlayer()), index, item, !canSet));
		return !event.isCancelled();
	}
}
