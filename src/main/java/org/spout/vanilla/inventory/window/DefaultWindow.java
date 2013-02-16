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
package org.spout.vanilla.inventory.window;

import org.spout.api.Client;
import org.spout.api.Server;
import org.spout.api.ServerOnly;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.util.GridIterator;
import org.spout.api.math.Vector2;
import org.spout.api.plugin.Platform;

import org.spout.vanilla.component.inventory.PlayerInventoryComponent;
import org.spout.vanilla.inventory.player.PlayerCraftingInventory;
import org.spout.vanilla.inventory.util.InventoryConverter;

import org.spout.vanilla.inventory.entity.ArmorInventory;

import org.spout.vanilla.inventory.entity.EntityArmorInventory;

public class DefaultWindow extends Window {
	private boolean opened = false;

	public DefaultWindow(Player owner) {
		super(owner, WindowType.DEFAULT, "Inventory", 9);
		PlayerInventoryComponent inventory = getPlayerInventory();

		addInventoryConverter(new InventoryConverter(inventory.getArmor(), "8, 7, 6, 5", new Vector2[]{
				Vector2.ZERO, Vector2.ZERO, Vector2.ZERO, Vector2.ZERO
		}));

		addInventoryConverter(new InventoryConverter(inventory.getCraftingGrid(), "3-4, 1-2, 0", new Vector2[]{
				Vector2.ZERO, Vector2.ZERO, Vector2.ZERO, Vector2.ZERO
		}));
	}

	@Override
	public void close() {
		PlayerInventoryComponent pInv = getPlayerInventory();
		//Disconnecting
		if (pInv == null) {
			return;
		}
		PlayerCraftingInventory inventory = getPlayerInventory().getCraftingGrid();
		GridIterator iterator = inventory.getGrid().iterator();
		while (iterator.hasNext()) {
			ItemStack item = inventory.get(iterator.next());
			if (item != null) {
				getHuman().dropItem(item);
			}
		}
		inventory.clear();
		//Do not call super-close, DefaultWindow only pseudo-closes
		if (!(Spout.getEngine() instanceof Server)) {
			super.close();
		}
		opened = false;
	}

	@Override
	public void onSlotSet(Inventory inventory, int slot, ItemStack item, ItemStack previous) {
		super.onSlotSet(inventory, slot, item, previous);
		if (inventory instanceof EntityArmorInventory) {
			((EntityArmorInventory) inventory).updateSlot(slot, item, getPlayer());
		}
	}

	@Override
	@ServerOnly
	public boolean onShiftClick(ItemStack stack, int slot, Inventory from) {
		if (Spout.getPlatform() == Platform.CLIENT) {
			throw new IllegalStateException("Shift click handling is handled server side.");
		}
		final PlayerInventoryComponent inventory = getPlayerInventory();

		// Transferring to the armor slots
		if (!(from instanceof EntityArmorInventory)) {
			// Transferring to the armor slots
			final ArmorInventory armor = inventory.getArmor();
			for (int i = 0; i < armor.size(); i++) {
				if (armor.get(i) == null && canSet(armor, i, stack)) {
					armor.set(i, ItemStack.cloneSpecial(stack));
					from.set(slot, stack.setAmount(0));
					return true;
				}
			}
		}

		return super.onShiftClick(stack, slot, from);
	}

	@Override
	public boolean onClick(ClickArguments args) {
		opened = true;
		return super.onClick(args);
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public boolean isOpened() {
		if (Spout.getEngine() instanceof Client) {
			return super.isOpened();
		}
		return opened;
	}

	@Override
	public void open() {
		opened = true;
		if (Spout.getEngine() instanceof Client) {
			super.open();
		}
	}
}
