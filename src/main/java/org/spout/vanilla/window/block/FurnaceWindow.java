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
package org.spout.vanilla.window.block;

import org.spout.api.inventory.InventoryBase;

import org.spout.vanilla.controller.block.Furnace;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.util.SlotIndexMap;
import org.spout.vanilla.window.TransactionWindow;

public class FurnaceWindow extends TransactionWindow {
	private static final SlotIndexMap SLOTS = new SlotIndexMap("30-38, 21-29, 12-20, 3-11, 1, 2, 0");
	protected final FurnaceInventory furnaceInv;

	public FurnaceWindow(VanillaPlayer owner, Furnace furnace) {
		super(2, "Furnace", owner, furnace);
		this.setSlotIndexMap(SLOTS);
		this.furnaceInv = furnace.getInventory();
	}

	@Override
	public boolean onClick(InventoryBase inventory, int clickedSlot, boolean rightClick, boolean shift) {
		if (inventory == this.furnaceInv && itemOnCursor != null && clickedSlot == this.furnaceInv.getOutput().getOffset()) {
			return false;
		} else {
			return super.onClick(inventory, clickedSlot, rightClick, shift);
		}
	}
}
