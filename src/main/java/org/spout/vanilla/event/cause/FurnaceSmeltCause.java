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
 * Caused when a furnace successfully smelted an ingredient
 */
public class FurnaceSmeltCause extends FurnaceCause {
	private final ItemStack resource;
	private ItemStack result;

	/**
	 * Contains the ItemStack of the ingredient which was smelted
	 * @param furnace the furnace which smelted the ingredient
	 * @param resource ItemStack which was smelted
	 * @param result ItemStack which is the result
	 */
	public FurnaceSmeltCause(Furnace furnace, ItemStack resource, ItemStack result) {
		super(furnace);
		this.resource = resource;
		this.result = result;
	}

	/**
	 * Gets the ingredient for this furnace
	 * @return the ingredient ItemStack
	 */
	public ItemStack getResource() {
		return resource.clone();
	}

	/**
	 * Gets the result of the smelting process for this furnace
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
}
