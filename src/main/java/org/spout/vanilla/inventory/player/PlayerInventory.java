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
package org.spout.vanilla.inventory.player;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.special.InventoryBundle;

import org.spout.vanilla.inventory.VanillaInventory;

/**
 * Represents a players inventory
 */
public class PlayerInventory extends InventoryBundle implements VanillaInventory {
	private static final long serialVersionUID = 1L;
	private final Inventory items;
	private final PlayerCraftingGrid craftingGrid;
	private final PlayerArmorInventory armor;

	public PlayerInventory() {
		super(new Inventory(36), new PlayerCraftingGrid(), new PlayerArmorInventory());
		this.items = (Inventory) this.getInventories()[0];
		this.craftingGrid = (PlayerCraftingGrid) this.getInventories()[1];
		this.armor = (PlayerArmorInventory) this.getInventories()[2];
		this.startWatching();
	}

	/**
	 * Gets the item inventory of this player inventory
	 * @return an Inventory with the items
	 */
	public Inventory getItems() {
		return this.items;
	}

	/**
	 * Gets the armor inventory of this player inventory
	 * @return an Inventory with the armor items
	 */
	public PlayerArmorInventory getArmor() {
		return this.armor;
	}

	/**
	 * Gets the crafting grid inventory of this player inventory
	 * @return an inventory with the crafting grid items
	 */
	public PlayerCraftingGrid getCraftingGrid() {
		return this.craftingGrid;
	}
}
