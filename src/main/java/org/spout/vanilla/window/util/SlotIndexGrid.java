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
package org.spout.vanilla.window.util;

public class SlotIndexGrid extends SlotIndexCollection {
	private final int length, width, offset;

	public SlotIndexGrid(int length, int width, int offset) {
		super(new int[length * width]);
		this.length = length;
		this.width = width;
		this.offset = offset;
		int index = 0;
		for (int w = 0; w < width; w++) {
			for (int l = 0; l < length; l++) {
				slots[index] = l + getSize() - (offset * w);
				index++;
			}
		}
	}

	public SlotIndexGrid(int length, int width) {
		this(length, width, 0);
	}

	public SlotIndexGrid translate(int offset) {
		return new SlotIndexGrid(length, width, this.offset + offset);
	}

	public int getLength() {
		return length;
	}

	public int getWidth() {
		return width;
	}

	public int getSize() {
		return length * width;
	}
}
