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
package org.spout.vanilla.inventory;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

public class CraftingInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	private final int length, width, outputSlot, offset;

	public CraftingInventory(int length, int width, int outputSlot, int offset) {
		super(length * width + 1);
		this.length = length;
		this.width = width;
		this.outputSlot = outputSlot;
		this.offset = offset;
	}

	public CraftingInventory(int length, int width, int outputSlot) {
		this(length, width, outputSlot, 0);
	}

	public int getGridLength() {
		return length;
	}

	public int getGridWidth() {
		return width;
	}

	public int getGridSize() {
		return length * width;
	}

	public int getOutputSlot() {
		return outputSlot;
	}

	public int getOffset() {
		return offset;
	}

	@Override
	public void onSlotChanged(int slot, ItemStack item) {
		int index = 0;
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < length; x++) {
				if (index + offset == slot) {
					updateOutput();
					index++;
				}
			}
		}
	}

	public void updateOutput() {
	}
}
