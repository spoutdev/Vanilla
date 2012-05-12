/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.PlayerInventory;

/**
 * Represents a players inventory
 */
public class VanillaPlayerInventory extends PlayerInventory implements VanillaInventory {
	private static final long serialVersionUID = 1L;

	public VanillaPlayerInventory() {
		super(45);
	}

	/**
	 * Returns the current {@link ItemStack} in the helmet slot (slot 44) ; can return null.
	 * @return helmet item stack
	 */
	public ItemStack getHelmet() {
		return getItem(44);
	}

	/**
	 * Returns the current {@link ItemStack} in the chest plate slot (slot 41) ; can return null.
	 * @return chest plate item stack
	 */
	public ItemStack getChestPlate() {
		return getItem(41);
	}

	/**
	 * Returns the current {@link ItemStack} in the leggings slot (slot 37) ; can return null.
	 * @return leggings item stack
	 */
	public ItemStack getLeggings() {
		return getItem(37);
	}

	/**
	 * Returns the current {@link ItemStack} in the boots slot (slot 36) ; can return null.
	 * @return boots item stack
	 */
	public ItemStack getBoots() {
		return getItem(36);
	}

	/**
	 * Returns the current {@link ItemStack} in the top left input in the crafting grid slot (slot 42) ; can return null.
	 * @return top left input item stack
	 */
	public ItemStack getTopLeftInput() {
		return getItem(42);
	}

	/**
	 * Returns the current {@link ItemStack} in the top right input in the crafting grid slot (slot 43) ; can return null.
	 * @return top right item stack
	 */
	public ItemStack getTopRightInput() {
		return getItem(43);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom left input in the crafting grid slot (slot 38) ; can return null.
	 * @return bottom left input item stack
	 */
	public ItemStack getBottomLeftInput() {
		return getItem(38);
	}

	/**
	 * Returns the current {@link ItemStack} in the bottom right input in the crafting grid slot (slot 39) ; can return null.
	 * @return bottom right input item stack
	 */
	public ItemStack getBottomRightInput() {
		return getItem(39);
	}

	/**
	 * Returns the current {@link ItemStack} in the output slot (slot 40) ; can return null.
	 * @return output item stack
	 */
	public ItemStack getOutput() {
		return getItem(40);
	}

	@Override
	public Window getWindow() {
		return Window.PLAYER_INVENTORY;
	}
}
