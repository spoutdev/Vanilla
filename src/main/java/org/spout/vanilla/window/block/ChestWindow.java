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

import org.spout.vanilla.controller.block.Chest;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.util.SlotIndexMap;
import org.spout.vanilla.window.TransactionWindow;
import org.spout.vanilla.window.WindowType;

public class ChestWindow extends TransactionWindow {
	private static final SlotIndexMap SMALL_CHEST_SLOTS = SlotIndexMap.GRID_9x3;
	private static final SlotIndexMap LARGE_CHEST1_SLOTS = SlotIndexMap.GRID_9x3.translate(27);
	private static final SlotIndexMap LARGE_CHEST2_SLOTS = SlotIndexMap.GRID_9x3;

	public ChestWindow(VanillaPlayer owner, Chest chest1, Chest chest2) {
		super(WindowType.CHEST, "Double Chest", owner, 54, chest1, chest2);
		this.addInventory(chest1.getInventory(), LARGE_CHEST1_SLOTS);
		this.addInventory(chest2.getInventory(), LARGE_CHEST2_SLOTS);
	}

	public ChestWindow(VanillaPlayer owner, Chest chest) {
		super(WindowType.CHEST, "Chest", owner, 27, chest);
		this.addInventory(chest.getInventory(), SMALL_CHEST_SLOTS);
	}
}
