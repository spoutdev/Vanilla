/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
package org.spout.vanilla.util;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.material.block.BlockFace;

public class VanillaMessageHandlerUtils {
	
	
	/**
	 * Converts a network block face to a spout one!
	 * @param messageFace The network face!
	 * @return The spout one!
	 */
	public static BlockFace messageToBlockFace(int messageFace) {
		switch (messageFace) {
			case 0:
				return BlockFace.BOTTOM;
			case 1:
				return BlockFace.TOP;
			case 2:
				return BlockFace.EAST; 
			case 3:
				return BlockFace.WEST;
			case 4:
				return BlockFace.NORTH;
			case 5:
				return BlockFace.SOUTH;
			default:
				return BlockFace.THIS;
		}
	}

	private final static int PLAYER_SLOT_CONVERSION[] = {36, 37, 38, 39, 40, 41, 42, 43, 44, // quickbar
			9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 5, 6, 7, 8, /* armor */ 4, 3, 2, 1, 0 // crafting
	};

	/**
	 * Converts a spout inventory slot to a network one!
	 * @param slot The spout slot id.
	 * @return The network id for that slot.
	 */
	public static int spoutInventorySlotToNetwork(int slot) {
		return PLAYER_SLOT_CONVERSION[slot];
	}

	/**
	 * Converts a network inventory slot to a spout one!
	 * @param slot The network slot id.
	 * @return The spout slot id.
	 */
	public static int networkInventorySlotToSpout(int slot) {
		for (int i = 0; i < PLAYER_SLOT_CONVERSION.length; ++i) {
			if (PLAYER_SLOT_CONVERSION[i] == slot) {
				return i;
			}
		}
		return -1;
	}

	private static final TObjectIntMap<Class<? extends Inventory>> INVENTORY_MAPPING = new TObjectIntHashMap<Class<? extends Inventory>>();

	static {
		INVENTORY_MAPPING.put(PlayerInventory.class, 0);
	}

	/**
	 * Gets the network inventory id of the inventory!
	 * @param inventory The inventory's class.
	 * @return The returned inventory id!
	 */
	public static byte getInventoryId(Class<? extends Inventory> inventory) {
		int result = INVENTORY_MAPPING.get(inventory);
		if (result == INVENTORY_MAPPING.getNoEntryValue()) {
			result = 0;
		}
		return (byte) result;
	}
}
