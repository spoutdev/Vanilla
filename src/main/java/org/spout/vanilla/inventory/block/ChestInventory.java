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
package org.spout.vanilla.inventory.block;

import org.spout.api.inventory.Inventory;

/**
 * Represents the inventory of a
 * {@link org.spout.vanilla.component.substance.material.Chest}.
 */
public class ChestInventory extends Inventory {
	private static final long serialVersionUID = 1L;
	public static final int SINGLE_SIZE = 27;
	public static final int DOUBLE_SIZE = SINGLE_SIZE * 2;

	public ChestInventory(int size) {
		super(size);
	}

	public ChestInventory(boolean d) {
		this(d ? DOUBLE_SIZE : SINGLE_SIZE);
	}

	public ChestInventory() {
		this(false);
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

	public String getTitle() {
		return getTitle("Unknown chest");
	}
}
