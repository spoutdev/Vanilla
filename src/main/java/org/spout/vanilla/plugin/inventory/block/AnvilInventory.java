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

/**
 * Represents an anvil's inventory belonging to an anvil entity.
 */
public class AnvilInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	public static final int SIZE = 3;
	public static final int REPAIR_SLOT = 0;
	public static final int INGREDIENT_SLOT = 1;
	public static final int OUTPUT_SLOT = 2;

	public AnvilInventory() {
		super(SIZE);
	}

	/**
	 * Whether the inventory contains an item to repair
	 * @return true if an item is present
	 */
	public boolean hasInput() {
		return getInput() != null;
	}

	/**
	 * Whether the inventory contains an ingredient to repair with
	 * @return true if an item is present
	 */
	public boolean hasIngredient() {
		return getIngredient() != null;
	}

	/**
	 * Whether the inventory contains an output item.
	 * @return true if an item is present
	 */
	public boolean hasOutput() {
		return getOutput() != null;
	}

	/**
	 * Returns the {@link ItemStack} in the repair slot; can return null.
	 * @return item stack
	 */
	public ItemStack getInput() {
		return get(REPAIR_SLOT);
	}

	/**
	 * Returns the {@link ItemStack} in the ingredient slot; can return null.
	 * @return item stack
	 */
	public ItemStack getIngredient() {
		return get(INGREDIENT_SLOT);
	}

	/**
	 * Returns the {@link ItemStack} in the anvil's output slot; can return null.
	 * @return item stack
	 */
	public ItemStack getOutput() {
		return get(OUTPUT_SLOT);
	}
}
