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

/**
 * A mathematic slot index map that inverts the slot in a fixed amount of steps<br>
 * For example, a 3x3 map would equal a regular Slot Index Map with the elements:<br><br>
 * <b>6-8, 3-5, 0-2</b>
 */
public class SlotIndexGrid extends SlotIndexCollection {
	private final int width, height;

	public SlotIndexGrid(int width, int height) {
		this(width, height, 0);
	}

	public SlotIndexGrid(int width, int height, int offset) {
		super(width * height, offset);
		this.width = width;
		this.height = height;
	}

	private int convertSlot(int slot) {
		if (containsSpoutSlot(slot)) {
			final int row = this.height - (slot / this.width) - 1;
			final int column = slot % this.width;
			return this.width * row + column;
		} else {
			return -1;
		}
	}

	@Override
	public SlotIndexGrid translate(int offset) {
		return new SlotIndexGrid(this.width, this.height, this.getOffset() + offset);
	}

	@Override
	public int getMinecraftSlot(int spoutSlotIndex) {
		final int mcSlot = convertSlot(spoutSlotIndex);
		return mcSlot == -1 ? -1 : mcSlot + this.getOffset();
	}

	@Override
	public int getSpoutSlot(int mcSlotIndex) {
		return convertSlot(mcSlotIndex - this.getOffset());
	}
}
