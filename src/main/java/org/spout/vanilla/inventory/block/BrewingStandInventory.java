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
package org.spout.vanilla.inventory.block;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.PotionReagent;

import org.spout.vanilla.material.item.potion.Potion;

/**
 * Represents the inventory of a {@link org.spout.vanilla.component.substance.material.BrewingStand}
 */
public class BrewingStandInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	public static final int SIZE = 4;
	public static final int[] OUTPUT_SLOTS = {0, 1, 2};
	public static final int INPUT_SLOT = 3;

	public BrewingStandInventory() {
		super(SIZE);
	}

	/**
	 * Gets the output slot at the given index. There are three output slots; the index given must be between 0 and 2.
	 * @param index of the output slot
	 * @return {@link ItemStack} in the output
	 */
	public ItemStack getOutput(int index) {
		int firstSlot = OUTPUT_SLOTS[0];
		int lastSlot = OUTPUT_SLOTS[OUTPUT_SLOTS.length - 1];
		if (index < firstSlot || index > lastSlot) {
			throw new IllegalArgumentException("The output index of the brewing stand must be between " + firstSlot + " and " + lastSlot + ".");
		}
		return get(index);
	}

	/**
	 * Whether the inventory contains an item in any of the output slots
	 * @return true if there is an item in at least one of the output slots
	 */
	public boolean hasOutput() {
		for (int i = 0; i < OUTPUT_SLOTS.length; i++) {
			if (getOutput(i) != null) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the input of the brewing stand
	 * @return {@link ItemStack} in input of brewing stand.
	 */
	public ItemStack getInput() {
		return get(INPUT_SLOT);
	}

	/**
	 * Whether the inventory contains an item in the input slot
	 * @return true if an item is present
	 */
	public boolean hasInput() {
		return getInput() != null;
	}

	@Override
	public boolean canSet(int i, ItemStack item) {
		if (!super.canSet(i, item)) {
			return false;
		}

		// Only allow potion ingredients to be put in the input slot
		if (i == INPUT_SLOT) {
			if (item != null && item.getMaterial() instanceof PotionReagent) {
				return true;
			}
		}

		for (int outputSlot : OUTPUT_SLOTS) {
			if (outputSlot == i) {
				// Slot is an output slot, make sure the item is something that can become a potion
				if (item != null && item.getMaterial() instanceof Potion) {
					return true;
				}
			}
		}

		return false;
	}
}
