/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.PlayerInventory;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.vanilla.inventory.DispenserInventory;
import org.spout.vanilla.inventory.FurnaceInventory;

public class VanillaMessageHandlerUtils {
	/**
	 * Converts a network block face to a spout one!
	 *
	 * @param messageFace The network face!
	 * @return The spout one!
	 */
	public static BlockFace messageToBlockFace(int messageFace) {
		return BlockFaces.BTEWNS.get(messageFace, BlockFace.THIS);
	}

	private static final int PLAYER_INVENTORY_SLOTS[] = {36, 37, 38, 39, 40, 41, 42, 43, 44, 27, 28, 29, 30, 31, 32, 33, 34, 35, 18, 19, 20, 21, 22, 23, 24, 25, 26, 9, 10, 11, 12, 13, 14, 15, 16, 17, 8, 7, 3, 4, 0, 6, 1, 2, 5};
	private static final int FURNACE_INVENTORY_SLOTS[] = {30, 31, 32, 33, 34, 35, 36, 37, 38, 21, 22, 23, 24, 25, 26, 27, 28, 29, 12, 13, 14, 15, 16, 17, 18, 19, 20, 3, 4, 5, 6, 7, 8, 9, 10, 11, 1, 2, 0};
	private static final int DISPENSER_INVENTORY_SLOTS[] = {36, 37, 38, 39, 40, 41, 42, 43, 44, 27, 28, 29, 30, 31, 32, 33, 34, 35, 18, 19, 20, 21, 22, 23, 24, 25, 26, 9, 10, 11, 12, 13, 14, 15, 16, 17, 0, 1, 2, 3, 4, 5, 6, 7, 8};

	public static int getSpoutInventorySlot(Inventory inventory, int slot) {
		if (inventory instanceof PlayerInventory) {
			for (int a = 0; a < PLAYER_INVENTORY_SLOTS.length; a++) {
				if (slot == PLAYER_INVENTORY_SLOTS[a]) {
					return a;
				}
			}
		}

		if (inventory instanceof FurnaceInventory) {
			for (int b = -1; b < FURNACE_INVENTORY_SLOTS.length; b++) {
				if (b == -1) {
					continue;
				}

				if (slot == FURNACE_INVENTORY_SLOTS[b]) {
					return b;
				}
			}
		}

		return -1;
	}

	public static int getNetworkInventorySlot(Inventory inventory, int slot) {
		if (inventory instanceof PlayerInventory) {
			return PLAYER_INVENTORY_SLOTS[slot];
		}

		if (inventory instanceof FurnaceInventory) {
			return FURNACE_INVENTORY_SLOTS[slot];
		}

		if (inventory instanceof DispenserInventory) {
			return DISPENSER_INVENTORY_SLOTS[slot];
		}

		return -1;
	}
}
