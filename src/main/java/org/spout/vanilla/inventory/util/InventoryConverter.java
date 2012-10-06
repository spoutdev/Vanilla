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
package org.spout.vanilla.inventory.util;

import org.spout.api.inventory.Inventory;
import org.spout.api.util.StringUtil;

/**
 * Converts slots sent to the client into the proper Spout format.
 */
public class InventoryConverter {
	protected final Inventory inventory;
	protected final int[] slots;

	public InventoryConverter(Inventory inventory, int[] slots) {
		this.inventory = inventory;
		this.slots = slots;
	}

	public InventoryConverter(Inventory inventory, String elements) {
		this(inventory, StringUtil.getIntArray(elements));
	}

	/**
	 * Returns a slot sent from the client in the proper Spout format.
	 *
	 * @param nativeSlot slot sent from the client
	 * @return slot in the proper Spout format
	 */
	public int getSlot(int nativeSlot) {
		for (int i = 0; i < slots.length; i++) {
			int slot = slots[i];
			if (slot == nativeSlot) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns a slot to send to the client from the specified 'Spout slot'.
	 *
	 * @param slot 'Spout formatted' slot to convert to slot sent to and from
	 * the client
	 * @return slot received and sent to and from the client
	 */
	public int getNativeSlot(int slot) {
		return slots[slot];
	}

	/**
	 * Returns the inventory associated with the converter
	 *
	 * @return inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	/**
	 * Returns the 'Spout formatted slots' mapped to the 'native slots' in an
	 * array.
	 *
	 * @return slot mapping array
	 */
	public int[] toArray() {
		return slots;
	}
}
