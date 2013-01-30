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
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.Vector2;

import org.spout.vanilla.api.material.PotionReagent;

import org.spout.vanilla.plugin.component.substance.material.BrewingStand;
import org.spout.vanilla.plugin.inventory.block.BrewingStandInventory;
import org.spout.vanilla.plugin.inventory.util.InventoryConverter;
import org.spout.vanilla.plugin.inventory.window.Window;
import org.spout.vanilla.plugin.inventory.window.WindowType;
import org.spout.vanilla.plugin.material.item.potion.Potion;

public class BrewingStandWindow extends Window {
	private BrewingStand brewingStand;

	public BrewingStandWindow(Player owner, BrewingStandInventory inventory, String title) {
		super(owner, WindowType.BREWING_STAND, title, 4);
		addInventoryConverter(new InventoryConverter(inventory, new int[]{0, 1, 2, 3}, new Vector2[0]));
	}

	public BrewingStandWindow(Player owner, BrewingStand brewingStand, BrewingStandInventory inventory) {
		this(owner, inventory, "Brewing Stand");
		this.brewingStand = brewingStand;
	}

	@Override
	public boolean onShiftClick(ItemStack item, int slot, Inventory from) {
		if (!(from instanceof BrewingStandInventory)) {
			final BrewingStandInventory inventory = getBrewingStand().getInventory();
			Material material = item.getMaterial();
			if (material instanceof Potion) {
				// Put into output slots
				for (int i = 0; i < 3; i++) {
					if (inventory.getOutput(i) == null) {
						inventory.set(i, item);
						from.set(slot, null);
						return true;
					}
				}
			} else if (material instanceof PotionReagent) {
				// Put into input slot
				if (inventory.getInput() == null) {
					inventory.set(BrewingStandInventory.INPUT_SLOT, item);
					from.set(slot, null);
					return true;
				}
			}
		}

		return super.onShiftClick(item, slot, from);
	}

	public BrewingStand getBrewingStand() {
		return brewingStand;
	}
}
