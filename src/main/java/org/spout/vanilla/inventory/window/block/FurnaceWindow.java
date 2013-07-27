/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.inventory.window.block;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector2;

import org.spout.vanilla.component.block.material.Furnace;
import org.spout.vanilla.inventory.block.FurnaceInventory;
import org.spout.vanilla.inventory.util.InventoryConverter;
import org.spout.vanilla.inventory.window.Window;
import org.spout.vanilla.inventory.window.WindowType;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.TimedCraftable;

public class FurnaceWindow extends Window {
	private Furnace furnace;

	public FurnaceWindow(Player owner, Furnace furnace, FurnaceInventory inventory) {
		this(owner, inventory, "Furnace");
		this.furnace = furnace;
	}

	public FurnaceWindow(Player owner, FurnaceInventory inventory, String title) {
		super(owner, WindowType.FURNACE, title, 3);
		addInventoryConverter(new InventoryConverter(inventory, "1, 0, 2", new Vector2[0]));
	}

	@Override
	public void close() {
		if (furnace != null) {
			furnace.close(getPlayer());
		}
		super.close();
	}

	@Override
	public boolean onShiftClick(ItemStack stack, int slot, Inventory from) {
		if (!(from instanceof FurnaceInventory)) {
			final FurnaceInventory furnace = this.getFurnace().getInventory();
			Material material = stack.getMaterial();
			// Try to put the item into the furnace, fuel first (!)
			if (material instanceof Fuel && ((Fuel) material).getFuelTime() > 0.0f) {
				// Put into fuel slot
				furnace.getFuelSlot().add(stack);
				from.set(slot, stack);
				if (stack.isEmpty()) {
					return true;
				}
			} else if (material instanceof TimedCraftable) {
				// Put into ingredient slot
				furnace.getIngredientSlot().add(stack);
				from.set(slot, stack);
				if (stack.isEmpty()) {
					return true;
				}
			}
		}
		return super.onShiftClick(stack, slot, from);
	}

	public Furnace getFurnace() {
		return furnace;
	}
}
