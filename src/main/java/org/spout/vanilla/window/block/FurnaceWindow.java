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
import org.spout.vanilla.protocol.msg.window.WindowPropertyMessage;
import org.spout.vanilla.util.SlotIndexMap;
import org.spout.vanilla.window.ClickArgs;
import org.spout.vanilla.window.TransactionWindow;
import org.spout.vanilla.window.WindowType;

public class FurnaceWindow extends TransactionWindow {
	private static final byte PROGRESS_ARROW = 0, FIRE_ICON = 1;
	private static final SlotIndexMap MAIN_SLOTS = new SlotIndexMap("30-38, 21-29, 12-20, 3-11");
	private static final SlotIndexMap FURNACE_SLOTS = new SlotIndexMap("1, 2, 0");

	protected final FurnaceInventory furnaceInv;
	private int lastProgress = -1, lastBurnTime = -1;

	public FurnaceWindow(VanillaPlayer owner, Furnace furnace) {
		super(WindowType.FURNACE, "Furnace", owner, furnace);
		this.furnaceInv = furnace.getInventory();
		this.addInventory(owner.getInventory().getMain(), MAIN_SLOTS);
		this.addInventory(this.furnaceInv, FURNACE_SLOTS);
	}

	@Override
	public boolean onClick(InventoryBase inventory, int clickedSlot, ClickArgs args) {
		if (inventory == this.furnaceInv && this.hasItemOnCursor() && clickedSlot == this.furnaceInv.getOutput().getOffset()) {
			return false;
		} else {
			return super.onClick(inventory, clickedSlot, args);
		}
	}

	/**
	 * Updates the Burn time of the Furnace
	 * @param burnTime
	 */
	public void updateBurnTime(int burnTime) {
		if (this.lastBurnTime != burnTime) {
			this.lastBurnTime = burnTime;
			this.sendMessage(new WindowPropertyMessage(this, FIRE_ICON, burnTime));
		}
	}

	/**
	 * Updates the Progress of the Furnace
	 * @param progress
	 */
	public void updateProgress(int progress) {
		if (this.lastProgress != progress) {
			this.lastProgress = progress;
			this.sendMessage(new WindowPropertyMessage(this, PROGRESS_ARROW, progress));
		}
	}
}
