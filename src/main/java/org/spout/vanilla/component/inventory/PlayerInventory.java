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
package org.spout.vanilla.component.inventory;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.player.PlayerArmorInventory;
import org.spout.vanilla.inventory.player.PlayerCraftingInventory;
import org.spout.vanilla.inventory.player.PlayerMainInventory;
import org.spout.vanilla.inventory.player.PlayerQuickbar;

/**
 * Represents a players inventory
 */
public class PlayerInventory extends EntityComponent {
	/**
	 * Gets the quickbar slots of this player inventory
	 * @return the quickbar slots
	 */
	public PlayerQuickbar getQuickbar() {
		return getData().get(VanillaData.QUICKBAR_INVENTORY);
	}

	/**
	 * Gets the item inventory of this player inventory
	 * @return an Inventory with the items
	 */
	public PlayerMainInventory getMain() {
		return getData().get(VanillaData.MAIN_INVENTORY);
	}

	/**
	 * Gets the armor inventory of this player inventory
	 * @return an Inventory with the armor items
	 */
	public PlayerArmorInventory getArmor() {
		return getData().get(VanillaData.ARMOR_INVENTORY);
	}

	/**
	 * Gets the crafting grid inventory of this player inventory
	 * @return an inventory with the crafting grid items
	 */
	public PlayerCraftingInventory getCraftingGrid() {
		return getData().get(VanillaData.CRAFTING_INVENTORY);
	}

	public ChestInventory getEnderChestInventory() {
		return getData().get(VanillaData.ENDER_CHEST_INVENTORY);
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

	/**
	 * Clears all inventories
	 */
	public void clear() {
		getMain().clear();
		getCraftingGrid().clear();
		getArmor().clear();
		getQuickbar().clear();
	}
}
