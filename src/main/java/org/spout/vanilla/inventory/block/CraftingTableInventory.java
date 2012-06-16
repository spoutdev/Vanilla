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
import org.spout.api.util.StringUtil;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.CraftingGrid;
import org.spout.vanilla.inventory.WindowInventory;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.window.block.CraftingTableWindow;

public class CraftingTableInventory extends WindowInventory implements CraftingGrid {
	private final int[] GRID_ARRAY = StringUtil.getIntArray("0-5, 7-9");
	private static final int OUTPUT_SLOT = 6, ROW_SIZE = 3, COLUMN_SIZE = 3;
	private static final long serialVersionUID = 1L;

	public CraftingTableInventory() {
		super(10);
	}

	@Override
	public Window createWindow(VanillaPlayer player) {
		return new CraftingTableWindow(player, this);
	}

	@Override
	public int getOutputSlot() {
		return OUTPUT_SLOT;
	}

	@Override
	public int getRowSize() {
		return ROW_SIZE;
	}

	@Override
	public int getColumnSize() {
		return COLUMN_SIZE;
	}

	@Override
	public int[] getGridArray() {
		return GRID_ARRAY;
	}

	@Override
	public Inventory getGridInventory() {
		return this;
	}
}
