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
package org.spout.vanilla.plugin.inventory.block;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.material.Fuel;
import org.spout.vanilla.api.material.TimedCraftable;

/**
 * Represents a furnace inventory belonging to a furnace entity.
 */
public class FurnaceInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	public static final int SIZE = 3;
	public static final int OUTPUT_SLOT = 2, FUEL_SLOT = 0, INGREDIENT_SLOT = 1;

	public FurnaceInventory() {
		super(SIZE);
	}

	public ItemStack getOutput() {
		return get(OUTPUT_SLOT);
	}

	public void setOutput(ItemStack item) {
		set(OUTPUT_SLOT, item);
	}

	public ItemStack getFuel() {
		return get(FUEL_SLOT);
	}

	public void setFuel(ItemStack item) {
		set(FUEL_SLOT, item);
	}

	public ItemStack getIngredient() {
		return get(INGREDIENT_SLOT);
	}

	public void setIngredient(ItemStack item) {
		set(INGREDIENT_SLOT, item);
	}

	/**
	 * Whether or not the inventory is fueled and ready to go!
	 * @return true if has fuel in slot.
	 */
	public boolean hasFuel() {
		return getFuel() != null && getFuel().getMaterial() instanceof Fuel;
	}

	/**
	 * Whether or not the inventory has an ingredient and ready to cook!
	 * @return true if has ingredient in slot.
	 */
	public boolean hasIngredient() {
		return getIngredient() != null && getIngredient().getMaterial() instanceof TimedCraftable;
	}
}
