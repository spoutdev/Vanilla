/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.block.FurnaceController;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;

/**
 * Represents a furnace inventory belonging to a furnace controller.
 */
public class FurnaceInventory extends Inventory implements VanillaInventory {
	private static final long serialVersionUID = 1L;
	private final FurnaceController owner;

	public FurnaceInventory(FurnaceController owner) {
		super(39);
		this.owner = owner;
	}

	/**
	 * Returns the furnace controller that this inventory belongs to.
	 *
	 * @return owner the furnace controller
	 */
	public FurnaceController getOwner() {
		return owner;
	}

	/**
	 * Returns the {@link ItemStack} in the output slot (slot 37); can return null.
	 *
	 * @return output item stack
	 */
	public ItemStack getOutput() {
		return getItem(37);
	}

	/**
	 * Sets the output of the inventory.
	 *
	 * @param output
	 */
	public void setOutput(ItemStack output) {
		setItem(37, output);
	}

	/**
	 * Returns the {@link ItemStack} in the fuel slot (slot 35); can return null.
	 *
	 * @return fuel item stack
	 */
	public ItemStack getFuel() {
		return getItem(36);
	}

	/**
	 * Sets the fuel slot of the inventory
	 *
	 * @param fuel
	 */
	public void setFuel(ItemStack fuel) {
		setItem(36, fuel);
	}

	/**
	 * Returns the {@link ItemStack} in the ingredient slot (slot 38); can return null.
	 *
	 * @return ingredient item stack
	 */
	public ItemStack getIngredient() {
		return getItem(38);
	}

	public void setIngredient(ItemStack ingredient) {
		setItem(38, ingredient);
	}

	/**
	 * Whether or not the inventory is fueled and ready to go!
	 *
	 * @return true if has fuel in slot.
	 */
	public boolean hasFuel() {
		return getFuel() != null && getFuel().getMaterial() instanceof Fuel;
	}

	/**
	 * Whether or not the inventory has an ingredient and ready to cook!
	 *
	 * @return true if has ingredient in slot.
	 */
	public boolean hasIngredient() {
		return getIngredient() != null && getIngredient().getMaterial() instanceof TimedCraftable;
	}

	@Override
	public Window getWindow() {
		return Window.FURNACE;
	}
}
