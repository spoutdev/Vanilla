/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.inventory.window.block.chest;

import org.spout.api.entity.Player;
import org.spout.api.math.Vector2;

import org.spout.vanilla.component.substance.material.chest.Chest;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.util.GridInventoryConverter;
import org.spout.vanilla.inventory.window.WindowType;

public class ChestWindow extends AbstractChestWindow {
	public ChestWindow(Player owner, Chest chest) {
		super(owner, chest, WindowType.CHEST, chest.getInventory().getTitle(), chest.getInventory().size());
		addInventoryConverter(new GridInventoryConverter(chest.getInventory(), 9, Vector2.ZERO));
	}

	public ChestWindow(Player owner, ChestInventory inv, String title) {
		super(owner, WindowType.CHEST, title, inv.size());
		addInventoryConverter(new GridInventoryConverter(inv, 9, Vector2.ZERO));
	}
}
