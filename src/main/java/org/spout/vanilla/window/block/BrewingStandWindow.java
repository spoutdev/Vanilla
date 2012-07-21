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

import org.spout.vanilla.controller.block.BrewingStand;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.BrewingStandInventory;
import org.spout.vanilla.util.SlotIndexMap;
import org.spout.vanilla.window.ClickArgs;
import org.spout.vanilla.window.TransactionWindow;
import org.spout.vanilla.window.WindowType;

public class BrewingStandWindow extends TransactionWindow {
	private static final SlotIndexMap MAIN_SLOTS = new SlotIndexMap("31-39, 22-30, 13-21, 4-12");
	private static final SlotIndexMap BREWING_SLOTS = new SlotIndexMap("0-3");

	protected final BrewingStandInventory brewing;

	public BrewingStandWindow(VanillaPlayer owner, BrewingStand stand) {
		super(WindowType.BREWINGSTAND, "Brewing Stand", owner, stand);
		this.brewing = stand.getInventory();
		this.addInventory(owner.getInventory().getMain(), MAIN_SLOTS);
		this.addInventory(this.brewing, BREWING_SLOTS);
	}

	@Override
	public boolean onClick(InventoryBase inventory, int clickedSlot, ClickArgs args) {
		if (inventory == this.brewing && this.hasItemOnCursor()) {
			return false;
		}
		return super.onClick(inventory, clickedSlot, args);
	}
}
