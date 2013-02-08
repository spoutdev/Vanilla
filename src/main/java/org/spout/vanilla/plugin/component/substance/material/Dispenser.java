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
package org.spout.vanilla.plugin.component.substance.material;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.inventory.Inventory;

import org.spout.vanilla.api.component.substance.material.DispenserComponent;
import org.spout.vanilla.api.event.inventory.DispenserCloseEvent;
import org.spout.vanilla.api.event.inventory.DispenserOpenEvent;

import org.spout.vanilla.plugin.component.inventory.WindowHolder;
import org.spout.vanilla.api.data.VanillaData;
import org.spout.vanilla.plugin.inventory.block.DispenserInventory;
import org.spout.vanilla.plugin.inventory.window.block.DispenserWindow;

/**
 * Component that represent a Dispenser in the world.
 */
public class Dispenser extends DispenserComponent {
	private final DispenserInventory inventory = new DispenserInventory();

	/**
	 * Retrieve the powered status of the dispenser.
	 * @return True if the dispenser is powered. Else false.
	 */
	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	/**
	 * Sets the powered status of the dispenser
	 * @param powered True if the dispenser is powered else false.
	 */
	public void setPowered(boolean powered) {
		getData().put(VanillaData.IS_POWERED, powered);
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public boolean open(Player player) {
		DispenserOpenEvent event = Spout.getEventManager().callEvent(new DispenserOpenEvent(this, player));
		if (!event.isCancelled()) {
			player.get(WindowHolder.class).openWindow(new DispenserWindow(player, inventory));
			return true;
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		DispenserCloseEvent event = Spout.getEventManager().callEvent(new DispenserCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}
}
