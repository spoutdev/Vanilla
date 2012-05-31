/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.block;

import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;

public class Chest extends VanillaBlockController {
	private ChestInventory inventory;
	private boolean opened = false;
	private boolean isDouble = false;

	public Chest() {
		this(false);
	}

	public Chest(boolean isDouble) {
		super(VanillaControllerTypes.CHEST, VanillaMaterials.CHEST);
		this.isDouble = isDouble;
		inventory = new ChestInventory(this);
	}

	@Override
	public void onAttached() {
		this.isDouble = this.data().containsKey("double") && ((Boolean) this.data().get("double"));
		ItemStack[] contents;
		if (this.data().containsKey("items")) {
			contents = (ItemStack[]) this.data().get("items");
		} else {
			contents = new ItemStack[this.isDouble ? ChestInventory.DOUBLE_CHEST_SIZE : ChestInventory.SINGLE_CHEST_SIZE];
		}
		this.inventory = new ChestInventory(this, contents);
	}

	@Override
	public void onSave() {
		this.data().put("double", this.isDouble);
		this.data().put("items", this.inventory.getContents());
	}

	@Override
	public void onTick(float dt) {
		if (this.inventory.hasViewingPlayers()) {
			Point position = this.getBlock().getPosition();
			for (VanillaPlayer player : this.inventory.getViewingPlayers().toArray(new VanillaPlayer[0])) {
				if (player.getParent().getPosition().distance(position) > 64) {
					this.inventory.close(player);
				}
			}
		}
	}

	public ChestInventory getInventory() {
		return inventory;
	}

	public void setOpened(boolean opened) {
		if (this.opened != opened) {
			this.opened = opened;
			byte data = opened ? (byte) 1 : 0;
			VanillaNetworkSynchronizer.playBlockAction(getBlock(), (byte) 1, data);
		}
	}

	public boolean isDouble() {
		return this.isDouble;
	}

	public boolean isOpened() {
		return opened;
	}
}
