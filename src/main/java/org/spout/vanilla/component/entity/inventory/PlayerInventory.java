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
package org.spout.vanilla.component.entity.inventory;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.entity.ArmorInventory;
import org.spout.vanilla.inventory.entity.QuickbarInventory;
import org.spout.vanilla.inventory.player.PlayerCraftingInventory;

/**
 * Represents the inventory of a Player.
 */
public class PlayerInventory extends EntityInventory {
	/**
	 * Returns the entity's armor inventory
	 * @return armor
	 */
	public ArmorInventory getArmor() {
		return getData().get(VanillaData.ARMOR_INVENTORY);
	}

	/**
	 * Gets the item inventory of this player inventory
	 * @return an Inventory with the items
	 */
	public Inventory getMain() {
		return getData().get(VanillaData.MAIN_INVENTORY);
	}

	/**
	 * Gets the crafting grid inventory of this player inventory
	 * @return an inventory with the crafting grid items
	 */
	public PlayerCraftingInventory getCraftingGrid() {
		return getData().get(VanillaData.CRAFTING_INVENTORY);
	}

	/**
	 * Gets the enderchest inventory of this player
	 * @return an inventory for the persistent chest
	 */
	public Inventory getEnderChestInventory() {
		return getData().get(VanillaData.ENDER_CHEST_INVENTORY);
	}

	@Override
	public QuickbarInventory getQuickbar() {
		return getData().get(VanillaData.QUICKBAR_INVENTORY);
	}

	/**
	 * Attempts to add the specified item to the quickbar and then the main if
	 * not all of the item is transferred.
	 * @param item to add
	 * @return true if item is completely transferred
	 */
	public boolean add(ItemStack item) {
		getQuickbar().add(item);
		if (!item.isEmpty()) {
			return getMain().add(item);
		}
		return true;
	}

	@Override
	public List<Inventory> getDroppable() {
		List<Inventory> inventories = new ArrayList<Inventory>(5);
		inventories.add(getQuickbar());
		inventories.add(getArmor());
		inventories.add(getCraftingGrid());
		inventories.add(getMain());
		return inventories;
	}

	@Override
	public void clear() {
		getMain().clear();
		getCraftingGrid().clear();
		getArmor().clear();
		getQuickbar().clear();
	}

	@Override
	public void updateAll() {
		updateAll(getQuickbar());
		updateAll(getMain());
		updateAll(getArmor());
		updateAll(getCraftingGrid());
		updateAll(getEnderChestInventory());
	}
}
