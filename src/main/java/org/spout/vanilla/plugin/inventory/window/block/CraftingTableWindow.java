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
package org.spout.vanilla.plugin.inventory.window.block;

import org.spout.api.entity.Player;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.util.GridIterator;
import org.spout.api.math.Vector2;

import org.spout.vanilla.plugin.inventory.block.CraftingTableInventory;
import org.spout.vanilla.plugin.inventory.util.InventoryConverter;
import org.spout.vanilla.plugin.inventory.window.Window;
import org.spout.vanilla.plugin.inventory.window.WindowType;

public class CraftingTableWindow extends Window {
	private final CraftingTableInventory inventory = new CraftingTableInventory();

	public CraftingTableWindow(Player owner) {
		this(owner, "Crafting");
	}

	public CraftingTableWindow(Player owner, String title) {
		super(owner, WindowType.CRAFTING_TABLE, title, 10);
		addInventoryConverter(new InventoryConverter(inventory, "7-9, 4-6, 1-3, 0", new Vector2[0]));
	}

	@Override
	public void close() {
		GridIterator iterator = inventory.getGrid().iterator();
		while (iterator.hasNext()) {
			ItemStack item = inventory.get(iterator.next());
			if (item != null) {
				getHuman().dropItem(item);
			}
		}
		inventory.clear();
		super.close();
	}
}
