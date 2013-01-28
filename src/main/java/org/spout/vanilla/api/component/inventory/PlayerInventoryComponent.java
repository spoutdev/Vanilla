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
package org.spout.vanilla.api.component.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.api.inventory.entity.ArmorInventory;
import org.spout.vanilla.api.inventory.entity.PlayerCraftingInventory;
import org.spout.vanilla.api.inventory.entity.QuickbarInventory;

/**
 * Represents the inventory that an Entity might have.
 */
public abstract class PlayerInventoryComponent extends EntityInventoryComponent {

	/**
	 * Returns the entity's armor inventory
	 * @return armor
	 */
	public abstract ArmorInventory getArmor();

	/**
	 * Returns the entity's held item
	 * @return itemstack
	 */
	public abstract ItemStack getHeldItem();

	/**
	 * Gets the item inventory of this player inventory
	 * @return an Inventory with the items
	 */
	public abstract Inventory getMain();

	/**
	 * Gets the crafting grid inventory of this player inventory
	 * @return an inventory with the crafting grid items
	 */
	public abstract PlayerCraftingInventory getCraftingGrid();

	/**
	 * Gets the enderchest inventory of this player
	 * @return an inventory for the persistent chest
	 */
	public abstract Inventory getEnderChestInventory();

	/**
	 * Attempts to add the specified item to the quickbar and then the main if
	 * not all of the item is transferred.
	 * @param item to add
	 * @return true if item is completely transferred
	 */
	public abstract boolean add(ItemStack item);

	/**
	 * Updates all slots of the given inventory
	 * @param inv to update
	 */
	protected void updateAll(Inventory inv) {
		if (inv != null) {
			inv.updateAll();
		}
	}
}
