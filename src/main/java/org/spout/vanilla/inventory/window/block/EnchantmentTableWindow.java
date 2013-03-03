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
package org.spout.vanilla.inventory.window.block;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector2;

import org.spout.vanilla.component.block.material.EnchantmentTable;
import org.spout.vanilla.inventory.block.EnchantmentTableInventory;
import org.spout.vanilla.inventory.util.InventoryConverter;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.inventory.window.WindowType;
import org.spout.vanilla.inventory.window.prop.EnchantmentTableProperty;
import org.spout.vanilla.material.enchantment.Enchantment;
import org.spout.vanilla.material.item.VanillaItemMaterial;

public class EnchantmentTableWindow extends Window {
	private final EnchantmentTable enchantmentTable;
	private final int[] levels = new int[3];

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
		if (!(inventory instanceof EnchantmentTableInventory) || slot != 0) {
			return;
		}
		if (item != null && item.getMaterial() instanceof VanillaItemMaterial && ((VanillaItemMaterial) item.getMaterial()).isEnchantable() && !Enchantment.isEnchanted(item)) {
			// Ensures that the third slot is always the maximum amount of possible levels based on nearby bookshelves
			int bookshelves = getEnchantmentTable().getNearbyBookshelves();
			if (bookshelves > 15) {
				bookshelves = 15;
			}

			debug("Calculating enchantment levels with " + bookshelves + " bookshelves.");

			int base = (GenericMath.getRandom().nextInt(8) + 1) + GenericMath.floor(bookshelves / 2) + GenericMath.getRandom().nextInt(bookshelves + 1);
			setEnchantmentLevel(EnchantmentTableProperty.SLOT_1, GenericMath.max((byte) (base / 3), (byte) 1)); // Minimum level
			setEnchantmentLevel(EnchantmentTableProperty.SLOT_2, (base * 2) / 3 + 1);
			setEnchantmentLevel(EnchantmentTableProperty.SLOT_3, GenericMath.max((byte) base, (byte) (bookshelves * 2))); // Maximum level
		} else {
			for (int i = 0; i < 2; ++i) {
				setEnchantmentLevel(i, 0);
			}
		}
	}

	public EnchantmentTable getEnchantmentTable() {
		return enchantmentTable;
	}

	/**
	 * Returns the level of the enchantment in the given slot.
	 * @param slot Slot to check
	 * @return Level of the the enchantment
	 */
	public int getEnchantmentLevel(int slot) {
		if (slot < 0 || slot > 2) {
			throw new IllegalArgumentException("Slot must be between 0 and 2");
		}

		return levels[slot];
	}

	/**
	 * Sets the level of the enchantment in the given {@link EnchantmentTableProperty} slot.
	 * @param slot Slot to set, null to clear the
	 * @param level Level of the enchantment
	 */
	public void setEnchantmentLevel(EnchantmentTableProperty slot, int level) {
		setEnchantmentLevel(slot.getId(), level);
	}

	/**
	 * Sets the level of the enchantment in the given slot.
	 * @param slot Slot to set
	 * @param level Level of the enchantment
	 */
	public void setEnchantmentLevel(int slot, int level) {
		if (slot < 0 || slot > 2) {
			throw new IllegalArgumentException("Slot must be between 0 and 2");
		}
		levels[slot] = level;
		setProperty(slot, level);
	}
}
