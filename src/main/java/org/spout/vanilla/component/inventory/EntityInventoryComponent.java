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
package org.spout.vanilla.component.inventory;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.inventory.entity.ArmorInventory;
import org.spout.vanilla.inventory.entity.QuickbarInventory;

/**
 * Represents the inventory that an Entity might have.
 */
public class EntityInventoryComponent extends EntityComponent {

	/**
	 * Returns the entity's armor inventory
	 * @return armor
	 */
	public ArmorInventory getArmor() {
		return getData().get(VanillaData.ARMOR_INVENTORY);
	}

	/**
	 * Gets the quickbar slots for this entity
	 * @return quickbar
	 */
	public QuickbarInventory getQuickbar() {
		return getData().get(VanillaData.ENTITY_HELD_INVENTORY);
	}

	/**
	 * Returns the entity's held item
	 * @return itemstack
	 */
	public ItemStack getHeldItem() {
		return getQuickbar().getSelectedItem();
	}

	/**
	 * Update all inventories attached to this component
	 */
	public void updateAll() {
		updateAll(getArmor());
		updateAll(getQuickbar());
	}

	/**
	 * Updates all slots of the given inventory
	 * @param inv to update
	 */
	protected void updateAll(Inventory inv) {
		if (inv != null) {
			inv.updateAll();
		}
	}

	/**
	 * Returns a List of all inventories that are dropped when the component owner is removed.
	 * @return list of droppable inventories
	 */
	public List<Inventory> getDroppable() {
		List<Inventory> inventories = new ArrayList<Inventory>(2);
		inventories.add(getArmor());
		inventories.add(getQuickbar());
		return inventories;
	}

	/**
	 * Clears all inventory slots
	 */
	public void clear() {
		getArmor().clear();
		getQuickbar().clear();
	}
}
