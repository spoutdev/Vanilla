/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.inventory.block;

import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;

/**
 * Represents the inventory of a
 * {@link org.spout.vanilla.plugin.component.substance.material.chest.Chest}.
 */
public class ChestInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	public static final int SINGLE_SIZE = 27;
	public static final int DOUBLE_SIZE = SINGLE_SIZE * 2;
	private final ChestInventory left, right;

	public ChestInventory() {
		super(SINGLE_SIZE);
		left = right = null;
	}

	public ChestInventory(int size) {
		super(size);
		left = right = null;
	}

	public ChestInventory(ChestInventory left, ChestInventory right) {
		super(0);
		this.left = left;
		this.right = right;
	}

	public String getTitle(String def) {
		switch (size()) {
			case SINGLE_SIZE:
				return "Chest";
			case DOUBLE_SIZE:
				return "Large chest";
			default:
				return def;
		}
	}
	
	@Override
	public ItemStack[] getContents() {
		if (left == null || right == null) {
			return super.getContents();
		}
		ItemStack[] items = new ItemStack[DOUBLE_SIZE];
		System.arraycopy(left.getContents(), 0, items, 0, SINGLE_SIZE);
		System.arraycopy(right.getContents(), 0, items, SINGLE_SIZE, SINGLE_SIZE);
		return items;
	}

	@Override
	public ItemStack set(int i, ItemStack item, boolean update) {
		if (left == null || right == null) {
			return super.set(i, item, update);
		}
		if (i < SINGLE_SIZE) {
			return left.set(i, item, update);
		} else {
			return right.set(i - SINGLE_SIZE, item, update);
		}
	}

	public String getTitle() {
		return getTitle("Unknown chest");
	}
}
