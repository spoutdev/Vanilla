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
package org.spout.vanilla.plugin.inventory.window.block;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector2;

import org.spout.vanilla.api.inventory.window.prop.EnchantmentTableProperty;

import org.spout.vanilla.plugin.component.substance.material.EnchantmentTable;
import org.spout.vanilla.plugin.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.plugin.inventory.util.InventoryConverter;
import org.spout.vanilla.plugin.inventory.window.Window;
import org.spout.vanilla.plugin.inventory.window.WindowType;

public class EnchantmentTableWindow extends Window {
	private final EnchantmentTable enchantmentTable;

	public EnchantmentTableWindow(Player owner, EnchantmentTable enchantmentTable, EnchantmentTableInventory inventory, String title) {
		super(owner, WindowType.ENCHANTMENT_TABLE, title, 1);
		addInventoryConverter(new InventoryConverter(inventory, "0", new Vector2[0]));
		this.enchantmentTable = enchantmentTable;
	}

	public EnchantmentTableWindow(Player owner, EnchantmentTable enchantmentTable, EnchantmentTableInventory inventory) {
		this(owner, enchantmentTable, inventory, "Enchant");
	}

	public EnchantmentTableWindow(Player owner, EnchantmentTableInventory inventory, String title) {
		this(owner, null, inventory, title);
	}

	@Override
	public void onSlotSet(Inventory inventory, int slot, ItemStack item, ItemStack previous) {
		super.onSlotSet(inventory, slot, item, previous);
		if (inventory instanceof EnchantmentTableInventory && slot == 0) {
			EnchantmentTableInventory inv = (EnchantmentTableInventory) inventory;

			// Ensures that the third slot is always the maximum amount of possible levels based on nearby bookshelves
			int bookshelves = getEnchantmentTable().getNearbyBookshelves();
			int base = (GenericMath.getRandom().nextInt(8) + 1) + GenericMath.floor(bookshelves / 2) + GenericMath.getRandom().nextInt(bookshelves + 1);
			inv.setEnchantmentLevel(EnchantmentTableProperty.SLOT_1, GenericMath.max((byte) (base / 3), (byte) 1)); // Minimum level
			inv.setEnchantmentLevel(EnchantmentTableProperty.SLOT_2, (base * 2) / 3 + 1);
			inv.setEnchantmentLevel(EnchantmentTableProperty.SLOT_3, GenericMath.max((byte) base, (byte) (bookshelves * 2))); // Maximum level
		}
	}

	public EnchantmentTable getEnchantmentTable() {
		return enchantmentTable;
	}
}
