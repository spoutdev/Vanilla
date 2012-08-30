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

import org.spout.vanilla.components.block.CraftingTable;
import org.spout.vanilla.inventory.block.CraftingTableInventory;
import org.spout.vanilla.util.intmap.SlotIndexCollection;
import org.spout.vanilla.util.intmap.SlotIndexMap;
import org.spout.vanilla.window.CraftingWindow;
import org.spout.vanilla.window.WindowType;

public class CraftingTableWindow extends CraftingWindow {
	private static final SlotIndexCollection CRAFTING_SLOTS = new SlotIndexMap("1-3, 4-6, 7-9, 0");

	public CraftingTableWindow(CraftingTable craftingTable) {
		this(craftingTable, new CraftingTableInventory());
	}

	private CraftingTableWindow(CraftingTable craftingTable, CraftingTableInventory inventory) {
		super(WindowType.CRAFTINGTABLE, "Crafting", 10, craftingTable);
		this.setCraftingGrid(inventory, CRAFTING_SLOTS);
	}
}
