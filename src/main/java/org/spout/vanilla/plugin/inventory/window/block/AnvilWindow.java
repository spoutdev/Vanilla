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

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.Vector2;

import org.spout.vanilla.api.event.inventory.AnvilCloseEvent;
import org.spout.vanilla.plugin.inventory.block.AnvilInventory;
import org.spout.vanilla.plugin.inventory.util.InventoryConverter;
import org.spout.vanilla.plugin.inventory.window.Window;
import org.spout.vanilla.plugin.inventory.window.WindowType;

public class AnvilWindow extends Window {
	private final Block block;
	private final AnvilInventory inventory;

	public AnvilWindow(Player owner, AnvilInventory inventory, Block block) {
		super(owner, WindowType.ANVIL, "Anvil", 3);
		this.inventory = inventory;
		this.block = block;
		addInventoryConverter(new InventoryConverter(inventory, "0-3", new Vector2[0]));
	}

	@Override
	public void close() {
		AnvilCloseEvent event = Spout.getEventManager().callEvent(new AnvilCloseEvent(block, inventory, getPlayer()));
		if (event.isCancelled()) {
			return;
		}

		for (ItemStack item : inventory) {
			if (item != null) {
				getHuman().dropItem(item);
			}
		}
		inventory.clear();
		super.close();
	}
}
