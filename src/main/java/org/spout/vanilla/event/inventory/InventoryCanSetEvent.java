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
package org.spout.vanilla.event.inventory;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.inventory.InventoryEvent;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

/**
 * Thrown when checking if an {@link ItemStack} can be set into an empty inventory slot, or when swapping items.<br/>
 * This event is thrown even when a slot is being set when the system would normally not allow it.<br/>
 * This event is <b>NOT</b> thrown when a player clicks to add items onto a slot that already has the item, or when a player clicks on a crafting slot which already has an item.
 */
public class InventoryCanSetEvent extends InventoryEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final int slot;
	private final ItemStack item;
	private boolean cancelled;

	public InventoryCanSetEvent(Inventory inventory, Cause<?> reason, int slot, ItemStack item, boolean cancelled) {
		super(inventory, reason);
		this.slot = slot;
		this.item = item;
		this.cancelled = cancelled;
	}

	/**
	 * The index of the slot that the item is being put into.
	 * @return slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Returns a clone of the {@link ItemStack} being set into the slot.
	 * @return the itemstack
	 */
	public ItemStack getItem() {
		return item.clone();
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
}
