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

import org.spout.api.inventory.PlayerInventory;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.material.item.Armor;
import org.spout.vanilla.util.InventoryUtil;

/**
 * Represents a player's inventory
 */
public class VanillaPlayerInventory extends PlayerInventory implements WindowInventory {
	private static final long serialVersionUID = 1L;
	private static final int[] SLOTS = { 36, 37, 38, 39, 40, 41, 42, 43, 44, 27, 28, 29, 30, 31, 32, 33, 34, 35, 18, 19, 20, 21, 22, 23, 24, 25, 26, 9, 10, 11, 12, 13, 14, 15, 16, 17, 8, 7, 3, 4, 0, 6, 1, 2, 5 };

	public VanillaPlayerInventory() {
		super(45);
	}

	@Override
	public VanillaItemStack getItem(int slot) {
		return (VanillaItemStack) super.getItem(slot);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the helmet slot (slot 44) ; can return null.
	 * @return helmet item stack
	 */
	public VanillaItemStack getHelmet() {
		return getItem(44);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the chest plate slot (slot 41) ; can return null.
	 * @return chest plate item stack
	 */
	public VanillaItemStack getChestPlate() {
		return getItem(41);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the leggings slot (slot 37) ; can return null.
	 * @return leggings item stack
	 */
	public VanillaItemStack getLeggings() {
		return getItem(37);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the boots slot (slot 36) ; can return null.
	 * @return boots item stack
	 */
	public VanillaItemStack getBoots() {
		return getItem(36);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the top left input in the crafting grid slot (slot 42) ; can return null.
	 * @return top left input item stack
	 */
	public VanillaItemStack getTopLeftInput() {
		return getItem(42);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the top right input in the crafting grid slot (slot 43) ; can return null.
	 * @return top right item stack
	 */
	public VanillaItemStack getTopRightInput() {
		return getItem(43);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the bottom left input in the crafting grid slot (slot 38) ; can return null.
	 * @return bottom left input item stack
	 */
	public VanillaItemStack getBottomLeftInput() {
		return getItem(38);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the bottom right input in the crafting grid slot (slot 39) ; can return null.
	 * @return bottom right input item stack
	 */
	public VanillaItemStack getBottomRightInput() {
		return getItem(39);
	}

	/**
	 * Returns the current {@link VanillaItemStack} in the output slot (slot 40) ; can return null.
	 * @return output item stack
	 */
	public VanillaItemStack getOutput() {
		return getItem(40);
	}

	@Override
	public Window getWindow() {
		return Window.PLAYER_INVENTORY;
	}

	@Override
	public void open(VanillaPlayer player) {
	}

	@Override
	public void onClosed(VanillaPlayer player) {
	}

	@Override
	public boolean onClicked(VanillaPlayer controller, int clickedSlot, VanillaItemStack slotStack) {
		// Only allow armor in the armor slots
		VanillaItemStack cursorStack = (VanillaItemStack) controller.getItemOnCursor();
		boolean armorSlot = clickedSlot == 36 || clickedSlot == 37 || clickedSlot == 41 || clickedSlot == 44;
		if (armorSlot && cursorStack != null && !(cursorStack.getMaterial() instanceof Armor)) {
			return false;
		}

		// Do not allow input in the output slot.
		if (clickedSlot == 40 && cursorStack != null) {
			return false;
		}

		slotStack = InventoryUtil.nullIfEmpty(slotStack);
		setItem(clickedSlot, slotStack);
		return true;
	}

	@Override
	public int getNativeSlotIndex(int index) {
		return SLOTS[index];
	}

	@Override
	public int getSlotIndex(int nativeIndex) {
		for (int i = 0; i < SLOTS.length; i++) {
			if (SLOTS[i] == nativeIndex) {
				return i;
			}
		}
		return -1;
	}
}
