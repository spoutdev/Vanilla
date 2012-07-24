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
package org.spout.vanilla.inventory.block;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.special.InventorySlot;

import org.spout.vanilla.controller.block.Furnace;
import org.spout.vanilla.inventory.VanillaInventory;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;

/**
 * Represents a furnace inventory belonging to a furnace controller.
 */
public class FurnaceInventory extends Inventory implements VanillaInventory {
	private static final long serialVersionUID = 1L;
	private final Furnace owner;
	private final InventorySlot output;
	private final InventorySlot fuel;
	private final InventorySlot ingredient;

	public FurnaceInventory(Furnace owner) {
		super(3);
		this.owner = owner;
		this.output = this.createSlot(0);
		this.fuel = this.createSlot(1);
		this.ingredient = this.createSlot(2);
	}

	/**
	 * Returns the furnace controller that this inventory belongs to.
	 * @return owner the furnace controller
	 */
	public Furnace getOwner() {
		return owner;
	}

	/**
	 * Returns the {@link InventorySlot} of the output slot (slot 37)
	 * @return output slot
	 */
	public InventorySlot getOutput() {
		return this.output;
	}

	/**
	 * Returns the {@link InventorySlot} of the fuel slot (slot 35)
	 * @return fuel slot
	 */
	public InventorySlot getFuel() {
		return this.fuel;
	}

	/**
	 * Returns the {@link InventorySlot} of the ingredient slot (slot 38)
	 * @return ingredient slot
	 */
	public InventorySlot getIngredient() {
		return this.ingredient;
	}

	/**
	 * Whether or not the inventory is fueled and ready to go!
	 * @return true if has fuel in slot.
	 */
	public boolean hasFuel() {
		return getFuel().getItem() != null && getFuel().getItem().getMaterial() instanceof Fuel;
	}

	/**
	 * Whether or not the inventory has an ingredient and ready to cook!
	 * @return true if has ingredient in slot.
	 */
	public boolean hasIngredient() {
		return getIngredient().getItem() != null && getIngredient().getItem().getMaterial() instanceof TimedCraftable;
	}
}
