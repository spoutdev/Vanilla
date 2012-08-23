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
package org.spout.vanilla.entity.block;

import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.entity.InventoryOwner;
import org.spout.vanilla.entity.VanillaControllerTypes;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.inventory.block.DispenserInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.window.Window;
import org.spout.vanilla.window.block.DispenserWindow;

public class Dispenser extends VanillaWindowBlockController implements InventoryOwner {
	private boolean isPowered = false;
	private final DispenserInventory inventory;

	public Dispenser() {
		super(VanillaControllerTypes.DISPENSER, VanillaMaterials.DISPENSER);
		inventory = new DispenserInventory(this);
	}

	@Override
	public void onTick(float dt) {
	}

	@Override
	public void onAttached() {
		if (getDataMap().containsKey(VanillaData.ITEMS)) {
			this.inventory.setContents(getDataMap().get(VanillaData.ITEMS));
		}
		this.isPowered = VanillaMaterials.DISPENSER.isReceivingPower(this.getBlock());
	}

	@Override
	public void onSave() {
		super.onSave();
		getDataMap().put(VanillaData.ITEMS, this.inventory.getContents());
	}

	/**
	 * Shoots the next Item
	 * @return True if an item was shot, False if not
	 */
	public boolean shootItem() {
		return this.shootItem(this.pollNextItem());
	}

	/**
	 * Shoots an item from this Dispenser
	 * @param item to shoot
	 * @return True if an item was shot, False if not
	 */
	public boolean shootItem(ItemStack item) {
		return VanillaMaterials.DISPENSER.shootItem(this.getBlock(), item);
	}

	/**
	 * Polls the next Item from this Dispenser<br>
	 * If this Dispenser is empty, null is returned
	 * @return the item, or null if none available
	 */
	public ItemStack pollNextItem() {
		for (int i = 0; i < this.inventory.getSize(); i++) {
			ItemStack item = this.inventory.getItem(i);
			if (item != null) {
				this.inventory.addItemAmount(i, -1);
				return item.clone().setAmount(1);
			}
		}
		return null;
	}

	/**
	 * Gets whether this Dispenser is being powered
	 * @return True if powered, False if not
	 */
	public boolean isPowered() {
		return this.isPowered;
	}

	/**
	 * Sets the powered state of this Dispenser<br>
	 * Will shoot an item when power goes from low to high
	 * @param powered state to set to
	 */
	public void setPowered(boolean powered) {
		if (this.isPowered != powered) {
			this.isPowered = powered;
			if (powered) {
				this.shootItem();
			}
		}
	}

	@Override
	public DispenserInventory getInventory() {
		return inventory;
	}

	@Override
	public Window createWindow(VanillaPlayerController player) {
		return new DispenserWindow(this);
	}
}
