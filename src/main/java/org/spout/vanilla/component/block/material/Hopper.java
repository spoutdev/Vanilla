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
package org.spout.vanilla.component.block.material;

import org.spout.api.entity.Player;
import org.spout.api.inventory.Container;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyFactory;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.block.ViewedBlockComponent;
import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.inventory.HopperCloseEvent;
import org.spout.vanilla.event.inventory.HopperOpenEvent;
import org.spout.vanilla.inventory.block.HopperInventory;
import org.spout.vanilla.inventory.window.block.HopperWindow;

/**
 * Component that represents any kind of Hopper.
 */
public class Hopper extends ViewedBlockComponent implements Container {
	private static final DefaultedKey<HopperInventory> HOPPER_INVENTORY = new DefaultedKeyFactory<HopperInventory>("hopper_inventory", HopperInventory.class);

	/**
	 * Returns true if the block this component is attached to is currently powered
	 * @return true if the block is powered
	 */
	public boolean isPowered() {
		return getData().get(VanillaData.IS_POWERED);
	}

	/**
	 * Sets the block as powered, or unpowered
	 * @param powered
	 */
	public void setPowered(boolean powered) {
		getData().put(VanillaData.IS_POWERED, powered);
	}

	@Override
	public HopperInventory getInventory() {
		return getData().get(HOPPER_INVENTORY);
	}

	@Override
	public boolean open(Player player) {
		HopperOpenEvent event = VanillaPlugin.getInstance().getEngine().getEventManager().callEvent(new HopperOpenEvent(this, player));
		if (!event.isCancelled()) {
			player.get(WindowHolder.class).openWindow(new HopperWindow(player, getInventory()));
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		HopperCloseEvent event = VanillaPlugin.getInstance().getEngine().getEventManager().callEvent(new HopperCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}
}
