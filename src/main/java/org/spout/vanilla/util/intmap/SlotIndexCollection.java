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
package org.spout.vanilla.util.intmap;

import org.spout.api.inventory.ItemStack;

/**
 * Maps Spout and Minecraft item slot indices to make them easily obtainable
 */
public class SlotIndexCollection {
	public static final SlotIndexCollection DEFAULT = new SlotIndexCollection(Integer.MAX_VALUE);
	private final int offset, size;

	public SlotIndexCollection(int size) {
		this(size, 0);
	}

	public SlotIndexCollection(int size, int offset) {
		this.offset = offset;
		this.size = size;
	}

	/**
	 * Gets the offset of the mapped information
	 * 
	 * @return the translated offset
	 */
	public int getOffset() {
		return this.offset;
	}

	/**
	 * Gets the size of the mapped information
	 * 
	 * @return collection size
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * Checks if the Spout slot is contained in this collection
	 * 
	 * @param spoutSlotIndex to check
	 * @return True if contained, False if not
	 */
	public boolean containsSpoutSlot(int spoutSlotIndex) {
		return spoutSlotIndex >= 0 && spoutSlotIndex < this.getSize();
	}

	/**
	 * Checks if the Minecraft slot is contained in this collection
	 * 
	 * @param mcSlotIndex to check
	 * @return True if contained, False if not
	 */
	public boolean containsMinecraftSlot(int mcSlotIndex) {
		return mcSlotIndex >= 0 && mcSlotIndex < this.getSize();
	}

	/**
	 * Translates all the slots by the amount specified and returns a new Slot Index Collection with the translated values
	 * 
	 * @param offset to translate
	 * @return a new translated Slot Index Collection
	 */
	public SlotIndexCollection translate(int offset) {
		return new SlotIndexCollection(this.getOffset() + offset);
	}

	/**
	 * Gets the Spout slot index from a Minecraft slot index
	 * 
	 * @param mcSlotIndex
	 * @return the Spout slot index, or -1 if not contained
	 */
	public int getSpoutSlot(int mcSlotIndex) {
		mcSlotIndex -= this.getOffset();
		if (containsMinecraftSlot(mcSlotIndex)) {
			return mcSlotIndex;
		} else {
			return -1;
		}
	}

	/**
	 * Gets the minecraft slot index from a spout slot index
	 * 
	 * @param spoutSlotIndex
	 * @return the Minecraft slot index, or -1 if not contained
	 */
	public int getMinecraftSlot(int spoutSlotIndex) {
		if (containsSpoutSlot(spoutSlotIndex)) {
			return spoutSlotIndex + this.getOffset();
		} else {
			return -1;
		}
	}

	/**
	 * Converts an array of items stored in the Spout item order into the Minecraft item order.
	 * @param spoutItems
	 * @return the items in Minecraft order
	 */
	public ItemStack[] getMinecraftItems(ItemStack[] spoutItems) {
		ItemStack[] rval = spoutItems.clone();
		int index;
		for (int i = 0; i < rval.length; i++) {
			index = getMinecraftSlot(i);
			if (index == -1) {
				System.out.println("Failed to find index for spout item index: " + i);
				continue;
			}
			rval[index] = spoutItems[i];
		}
		return rval;
	}

	/**
	 * Converts an array of items stored in the Minecraft item order into the Spout item order.
	 * @param minecraftItems
	 * @return the items in Spout order
	 */
	public ItemStack[] getSpoutItems(ItemStack[] minecraftItems) {
		ItemStack[] rval = minecraftItems.clone();
		for (int i = 0; i < rval.length; i++) {
			rval[getSpoutSlot(i)] = minecraftItems[i];
		}
		return rval;
	}
}
