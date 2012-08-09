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
package org.spout.vanilla.window;

import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.util.intmap.SlotIndexCollection;
import org.spout.vanilla.util.intmap.SlotIndexGrid;
import org.spout.vanilla.util.intmap.SlotIndexMap;
import org.spout.vanilla.util.intmap.SlotIndexRow;

/**
 * The default player window always displayed
 */
public class DefaultWindow extends CraftingWindow {
	private static final SlotIndexCollection MAIN_SLOTS = new SlotIndexGrid(9, 4, 9);
	private static final SlotIndexCollection ARMOR_SLOTS = new SlotIndexRow(4, 5);
	private static final SlotIndexCollection CRAFTING_SLOTS = new SlotIndexMap("1-4, 0");

	public DefaultWindow(VanillaPlayerController owner) {
		super(WindowType.DEFAULT, "Inventory", owner, owner.getInventory().getCraftingGrid());
		this.addInventory(owner.getInventory().getMain(), MAIN_SLOTS);
		this.addInventory(owner.getInventory().getCraftingGrid(), CRAFTING_SLOTS);
		this.addInventory(owner.getInventory().getArmor(), ARMOR_SLOTS);
	}

	@Override
	public int getInstanceId() {
		return 0;
	}

	@Override
	public void close() {
		super.close();
		if (this.getOwner().isSurvival()) {
			this.dropItemOnCursor();
		}
	}
}
