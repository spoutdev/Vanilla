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
package org.spout.vanilla.event.cause;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.substance.material.Furnace;

/**
 * Caused when a furnace successfully burned fuel
 */
public class FurnaceBurnCause extends FurnaceCause {
	private ItemStack fuel;
	private int burnTime;

	/**
	 * Contains the ItemStack of the fuel which was burned
	 * @param furnace the furnace which has burned fuel
	 * @param fuel ItemStack of fuel
	 * @param burnTime the time one unit of the fuel will burn
	 */
	public FurnaceBurnCause(Furnace furnace, ItemStack fuel, int burnTime) {
		super(furnace);
		this.fuel = fuel;
		this.burnTime = burnTime;
	}

	/**
	 * Gets the fuel ItemStack for this furnace
	 * @return the fuel ItemStack
	 */
	public ItemStack getFuel() {
		return fuel.clone();
	}

	/**
	 * Gets the burn time of one unit of the ItemStack
	 * @return the time one unit of the fuel will burn
	 */
	public int getBurnTime() {
		return burnTime;
	}

	/**
	 * Sets the fuel ItemStack for this furnace
	 * @param fuel the fuel ItemStack
	 */
	public void setFuel(ItemStack fuel) {
		this.fuel = fuel;
	}

	/**
	 * Set the burn time of one unit of fuel for the ItemStack.
	 * Will throw an {@link IllegalArgumentException} if burnTime is <= 0
	 * @param burnTime the time one unit of the fuel will burn
	 */
	public void setBurnTime(int burnTime) {
		if (burnTime <= 0) {
			throw new IllegalArgumentException();
		}
		this.burnTime = burnTime;
	}
}
