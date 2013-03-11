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
package org.spout.vanilla.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.block.BlockEvent;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.block.material.Furnace;

/**
 * Event which is called when an unit of an ItemStack has finished smelting.
 */
public class FurnaceSmeltEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Furnace furnace;
	private final ItemStack source;
	private ItemStack result;

	public FurnaceSmeltEvent(Furnace furnace, Cause<?> reason, ItemStack source, ItemStack result) {
		super(furnace.getBlock(), reason);
		this.furnace = furnace;
		this.source = source;
		this.result = result;
	}

	/**
	 * Gets the smelted ItemStack
	 * @return ItemStack which was smelted
	 */
	public ItemStack getSource() {
		return source;
	}

	/**
	 * Gets the result of the smelting process for this furnace, as a clone.
	 * Changes to this itemstack will not be reflected on event completion.
	 * @return the result ItemStack
	 */
	public ItemStack getResult() {
		return result.clone();
	}

	/**
	 * Sets the result ItemStack
	 * @param newResult the result ItemStack
	 */
	public void setResult(ItemStack newResult) {
		result = newResult;
	}

	/**
	 * Returns the Furnace in which an item was smelted.
	 * @return furnace
	 */
	public Furnace getFurnace() {
		return furnace;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
