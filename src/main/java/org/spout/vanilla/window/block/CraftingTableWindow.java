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

import org.spout.vanilla.controller.block.CraftingTable;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.CraftingTableInventory;
import org.spout.vanilla.util.SlotIndexMap;
import org.spout.vanilla.window.CraftingWindow;

public class CraftingTableWindow extends CraftingWindow {
	private static final SlotIndexMap SLOTS = new SlotIndexMap("37-45, 28-36, 19-27, 10-18, 7-9, 4-6, 1-3, 0");

	public CraftingTableWindow(VanillaPlayer owner, CraftingTable craftingTable) {
		this(owner, craftingTable, new CraftingTableInventory());
	}

	private CraftingTableWindow(VanillaPlayer owner, CraftingTable craftingTable, CraftingTableInventory inventory) {
		super(1, "Crafting", owner, inventory, craftingTable);
		this.setInventory(owner.getInventory().getMain(), inventory);
		this.setSlotIndexMap(SLOTS);
	}

	@Override
	public int getInventorySize() {
		return this.getInventory().getSize() - this.getOwner().getInventory().getMain().getSize();
	}
}
