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
package org.spout.vanilla.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Event;
import org.spout.api.event.HandlerList;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.controller.block.Furnace;

/**
 * Event which is called when an fuel in the furnace is being burned.
 */
public class FurnaceBurnEvent extends Event implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private Furnace furnace;
	private ItemStack fuel;

	public FurnaceBurnEvent(Furnace furnace, ItemStack fuel) {
		super();
		this.furnace = furnace;
		this.fuel = fuel;
	}

	/**
	 * Returns the Furnace which caused the FurnaceBurnEvent
	 * @return furnace
	 */
	public Furnace getFurnace() {
		return furnace;
	}

	/**
	 * Returns the ItemStack which should be burned
	 * @return fuel
	 */
	public ItemStack getFuel() {
		return fuel;
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
