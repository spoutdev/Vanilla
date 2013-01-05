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
package org.spout.vanilla.component.substance.material.chest;

import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Block;

import org.spout.vanilla.component.inventory.WindowHolder;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.inventory.Container;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.window.block.chest.ChestWindow;
import org.spout.vanilla.material.VanillaMaterials;

public class Chest extends AbstractChest implements Container {
	
	/**
	 * Whether the chest has a double inventory.
	 * @return true if has a double inventory.
	 */
	public boolean isDouble() {
		return getInventory().size() == ChestInventory.DOUBLE_SIZE;
	}

	/**
	 * Sets the size of the Chest's inventory to either
	 * {@link ChestInventory#DOUBLE_SIZE} or
	 * {@link ChestInventory#SINGLE_SIZE}.
	 * @param d whether the chest should be a double or single chest
	 */
	public void setDouble(boolean d) {
		// Return if chest is already specified size
		ChestInventory oldInventory = getInventory();
		if ((d && oldInventory.size() == ChestInventory.DOUBLE_SIZE) || (!d && oldInventory.size() == ChestInventory.SINGLE_SIZE)) {
			return;
		}

		// Create new inventory and try to merge with the old inventory
		ChestInventory newInventory = new ChestInventory(d);
		newInventory.addAll(oldInventory);
		getData().put(VanillaData.CHEST_INVENTORY, newInventory);
	}

	@Override
	public ChestInventory getInventory() {
		return getData().get(VanillaData.CHEST_INVENTORY);
	}

	@Override
	public void open(Player player) {
		// Get the block at the component's position
		final Block block = getBlock();

		// Make sure it's the right size and open the chest if closed
		setDouble(VanillaMaterials.CHEST.isDouble(block));

		// Finally open the window
		player.get(WindowHolder.class).openWindow(new ChestWindow(player, this));
		
		SoundEffects.RANDOM_CHESTOPEN.playGlobal(player.getTransform().getPosition());
		
		super.open(player);
	}
}
