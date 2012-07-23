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
package org.spout.vanilla.util;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.StringUtil;

/**
 * Maps Spout and Minecraft item slot indices to make them easily obtainable
 */
public class SlotIndexMap {
	public static final SlotIndexMap DEFAULT = new SlotIndexMap();
	public static final SlotIndexMap GRID_9x1 = new SlotIndexMap("0-8");
	public static final SlotIndexMap GRID_9x2 = new SlotIndexMap("9-17, 0-8");
	public static final SlotIndexMap GRID_9x3 = new SlotIndexMap("18-26, 9-17, 0-8");
	public static final SlotIndexMap GRID_9x4 = new SlotIndexMap("27-35, 18-26, 9-17, 0-8");
	public static final SlotIndexMap GRID_9x5 = new SlotIndexMap("36-44, 27-35, 18-26, 9-17, 0-8");
	public static final SlotIndexMap GRID_9x6 = new SlotIndexMap("45-53, 36-44, 27-35, 18-26, 9-17, 0-8");
	public static final SlotIndexMap GRID_3x3 = new SlotIndexMap("0-8");

	private final int offset;
	private final int[] toMC;
	private final int[] toSpout;

	public SlotIndexMap() {
		this.toMC = this.toSpout = null;
		this.offset = 0;
	}

	public SlotIndexMap(SlotIndexMap base, int offset) {
		this.offset = base.offset + offset;
		this.toMC = base.toMC;
		this.toSpout = base.toSpout;
	}

	public SlotIndexMap(String elements) {
		this(StringUtil.getIntArray(elements));
	}

	public SlotIndexMap(int[] toMC) {
		this.offset = 0;
		this.toMC = toMC;
		int max = 0;
		for (int value : toMC) {
			if (value > max) {
				max = value;
			}
		}
		this.toSpout = new int[max + 1];
		for (int i = 0; i < this.toMC.length; i++) {
			this.toSpout[this.toMC[i]] = i;
		}
	}

	/**
	 * Translates all the slots by the amount specified and returns a new Slot Index Map with the translated values
	 * 
	 * @param offset to translate
	 * @return a new translated Slot Index Map
	 */
	public SlotIndexMap translate(int offset) {
		return new SlotIndexMap(this, offset);
	}

	/**
	 * Gets the Spout slot index from a Minecraft slot index
	 * @param mcSlotIndex
	 * @return the Spout slot index
	 */
	public int getSpoutSlot(int mcSlotIndex) {
		mcSlotIndex -= this.offset;
		if (toSpout == null) {
			return mcSlotIndex;
		} else if (mcSlotIndex < 0 || mcSlotIndex >= toSpout.length) {
			return -1;
		}
		return toSpout[mcSlotIndex];
	}

	/**
	 * Gets the minecraft slot index from a spout slot index
	 * @param spoutSlotIndex
	 * @return the Minecraft slot index
	 */
	public int getMinecraftSlot(int spoutSlotIndex) {
		if (toMC == null) {
			return spoutSlotIndex + this.offset;
		} else if (spoutSlotIndex < 0 || spoutSlotIndex >= toMC.length) {
			return -1;
		}
		return toMC[spoutSlotIndex] + this.offset;
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
