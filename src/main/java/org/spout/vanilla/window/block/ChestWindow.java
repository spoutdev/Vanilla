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
import org.spout.vanilla.window.Window;

public class ChestWindow extends Window {
	public static final SlotIndexMap SMALL_CHEST_SLOTS = new SlotIndexMap("54-62, 45-53, 36-44, 27-35, 18-26, 9-17, 0-8");
	public static final SlotIndexMap LARGE_CHEST_SLOTS = new SlotIndexMap("81-89, 72-80, 63-71, 54-62, 45-53, 36-44, 27-35, 18-26, 9-17, 0-8");

	public ChestWindow(VanillaPlayer owner, Chest chest1, Chest chest2) {
		super(0, "Double Chest", owner, chest1, chest2);
		this.setInventory(owner.getInventory().getItems(), chest1.getInventory(), chest2.getInventory());
		this.setSlotIndexMap(LARGE_CHEST_SLOTS);
	}

	public ChestWindow(VanillaPlayer owner, Chest chest) {
		super(0, "Chest", owner, chest);
		this.setInventory(owner.getInventory().getItems(), chest.getInventory());
		this.setSlotIndexMap(SMALL_CHEST_SLOTS);
	}

	@Override
	public int getInventorySize() {
		return this.getInventory().getSize() - this.getOwner().getInventory().getItems().getSize();
	}
}
