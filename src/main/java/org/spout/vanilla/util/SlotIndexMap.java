package org.spout.vanilla.util;

import org.spout.api.inventory.ItemStack;
import org.spout.api.util.StringUtil;

/**
 * Maps Spout and Minecraft item slot indices to make them easily obtainable
 */
public class SlotIndexMap {

	private final int[] toMC;
	private final int[] toSpout;

	public SlotIndexMap() {
		this.toMC = this.toSpout = null;
	}

	public SlotIndexMap(String elements) {
		this(StringUtil.getIntArray(elements));
	}

	public SlotIndexMap(int[] toMC) {
		this.toMC = toMC;
		this.toSpout = new int[toMC.length];
		for (int i = 0; i < this.toMC.length; i++) {
			this.toSpout[this.toMC[i]] = i;
		}
	}

	/**
	 * Gets the Spout slot index from a Minecraft slot index
	 * @param mcSlotIndex
	 * @return the Spout slot index
	 */
	public int getSpoutSlot(int mcSlotIndex) {
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
			return spoutSlotIndex;
		} else if (spoutSlotIndex < 0 || spoutSlotIndex >= toMC.length) {
			return -1;
		}
		return toMC[spoutSlotIndex];
	}

	/**
	 * Converts an array of items stored in the Spout item order into the Minecraft item order.
	 * @param spoutItems
	 * @return the items in Minecraft order
	 */
	public ItemStack[] getMinecraftItems(ItemStack[] spoutItems) {
		ItemStack[] rval = spoutItems.clone();
		for (int i = 0; i < rval.length; i++) {
			rval[i] = spoutItems[getMinecraftSlot(i)];
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
			rval[i] = minecraftItems[getSpoutSlot(i)];
		}
		return rval;
	}
}
