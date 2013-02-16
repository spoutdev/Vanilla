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

import org.spout.vanilla.component.substance.material.Furnace;

/**
 * Event which is called when an unit of an ItemStack is burned as fuel.
 * todo implement calling of this event
 */
public class FurnaceBurnEvent extends BlockEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private final Furnace furnace;
	private final Cause cause;
	private final ItemStack fuel;
	private float burnTime;
	private boolean burning;

	public FurnaceBurnEvent(Furnace furnace, Cause<?> reason, ItemStack fuel, float burnTime) {
		super(furnace.getBlock(), reason);
		this.furnace = furnace;
		this.cause = reason;
		this.fuel = fuel;
		this.burnTime = burnTime;
		// TODO will need to check if that is really necessary here
		this.burning = true;
	}

	/**
	 * Gets the fuel ItemStack for this furnace
	 * @return the fuel ItemStack
	 */
	public ItemStack getFuel() {
		return fuel.clone();
	}

	/**
	 * Returns if the Furnace is burning at the moment
	 * @return true if isBurning
	 */
	public boolean isBurning() {
		return burning;
	}

	/**
	 * Sets the burning state of the furnace.
	 * @param burning
	 */
	public void setBurning(boolean burning) {
		this.burning = burning;
	}

	/**
	 * Gets the burn time of one unit of the ItemStack
	 * @return the time one unit of the fuel will burn
	 */
	public float getBurnTime() {
		return burnTime;
	}

	/**
	 * Set the burn time of one unit of fuel for the ItemStack.
	 * Will throw an {@link IllegalArgumentException} if burnTime is <= 0
	 * @param burnTime the time one unit of the fuel will burn
	 * @see Furnace for more information about the BurnTime
	 */
	public void setBurnTime(float burnTime) {
		if (burnTime <= 0) {
			throw new IllegalArgumentException();
		}
		this.burnTime = burnTime;
	}

	/**
	 * Returns the Furnace where fuel was burned
	 * @return furnace
	 */
	public Furnace getFurnace() {
		return furnace;
	}

	/**
	 * Returns the cause which caused the FurnaceBurnEvent
	 * @return cause
	 */
	public Cause<?> getCause() {
		return cause;
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
