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
import org.spout.api.inventory.ItemStack;

/**
 * Represents the inventory of a
 * {@link org.spout.vanilla.component.substance.material.BrewingStand}
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
	 * Gets the output slot at the given index. There are three output slots; the index given must be between 0 and 3.
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
	 * Gets the input of the brewing stand
	 * @return {@link ItemStack} in input of brewing stand.
	 */
	public ItemStack getInput() {
		return get(INPUT_SLOT);
	}
}
