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
package org.spout.vanilla.inventory.player;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.inventory.CraftingInventory;

/**
 * Represents the crafting inventory in the player's inventory.
 */
public class PlayerCraftingInventory extends CraftingInventory {
	private static final long serialVersionUID = 1L;
	public static final int LENGTH = 2, WIDTH = 2, OUTPUT_SLOT = 4;
	public static final int TOP_LEFT_SLOT = 0, TOP_RIGHT_SLOT = 1, BOTTOM_LEFT_SLOT = 2, BOTTOM_RIGHT_SLOT = 3, OUTPUT = 4;

	public PlayerCraftingInventory() {
		super(LENGTH, WIDTH, OUTPUT_SLOT);
	}

	/**
	 * Returns the current {@link ItemStack} in the top left input in the crafting grid slot (slot 42) ; can return null.
	 * @return top left input item stack
	 */
	public ItemStack getTopLeft() {
		return get(TOP_LEFT_SLOT);
	}

	/**
	 * Returns the current {@link ItemStack} in the top right input in the crafting grid slot (slot 43) ; can return null.
	 * @return top right item stack
	 */
	public ItemStack getTopRight() {
		return get(TOP_RIGHT_SLOT);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom left input in the crafting grid slot (slot 38) ; can return null.
	 * @return bottom left input item stack
	 */
	public ItemStack getBottomLeft() {
		return get(BOTTOM_LEFT_SLOT);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom right input in the crafting grid slot (slot 39) ; can return null.
	 * @return bottom right input item stack
	 */
	public ItemStack getBottomRight() {
		return get(BOTTOM_RIGHT_SLOT);
	}

	public ItemStack getOutput() {
		return get(OUTPUT);
	}
}
