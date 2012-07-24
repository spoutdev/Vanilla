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

import org.spout.api.util.StringUtil;

/**
 * Maps Spout and Minecraft item slot indices to make them easily obtainable
 */
public class SlotIndexMap extends SlotIndexCollection {
	private final int[] toMC;
	private final int[] toSpout;

	public SlotIndexMap(String elements) {
		this(StringUtil.getIntArray(elements));
	}

	public SlotIndexMap(int[] toMC) {
		this(0, toMC);
	}

	public SlotIndexMap(int offset, int[] toMC) {
		super(toMC.length);
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

	public SlotIndexMap(int offset, int[] toMC, int[] toSpout) {
		super(toMC.length, offset);
		this.toMC = toMC;
		this.toSpout = toSpout;
	}

	@Override
	public boolean containsSpoutSlot(int spoutSlotIndex) {
		return spoutSlotIndex >= 0 && spoutSlotIndex < this.toMC.length;
	}

	@Override
	public boolean containsMinecraftSlot(int mcSlotIndex) {
		return mcSlotIndex >= 0 && mcSlotIndex < this.toSpout.length;
	}

	@Override
	public SlotIndexMap translate(int offset) {
		return new SlotIndexMap(this.getOffset() + offset, this.toMC, this.toSpout);
	}

	@Override
	public int getSpoutSlot(int mcSlotIndex) {
		mcSlotIndex -= this.getOffset();
		if (containsMinecraftSlot(mcSlotIndex)) {
			return toSpout[mcSlotIndex];
		} else {
			return -1;
		}
	}

	@Override
	public int getMinecraftSlot(int spoutSlotIndex) {
		if (containsSpoutSlot(spoutSlotIndex)) {
			return toMC[spoutSlotIndex] + this.getOffset();
		} else {
			return -1;
		}
	}
}
