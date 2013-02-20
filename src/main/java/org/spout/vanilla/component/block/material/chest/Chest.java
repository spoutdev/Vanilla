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
package org.spout.vanilla.component.block.material.chest;

import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.Container;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyFactory;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.data.effect.store.SoundEffects;
import org.spout.vanilla.event.inventory.ChestCloseEvent;
import org.spout.vanilla.event.inventory.ChestOpenEvent;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.window.block.chest.ChestWindow;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.VanillaMaterials;

/**
 * Represents a basic chest in the world.
 */
public class Chest extends AbstractChest implements Container {
	private static final DefaultedKey<ChestInventory> CHEST_INVENTORY = new DefaultedKeyFactory<ChestInventory>("chest_inventory", ChestInventory.class);

	/**
	 * Whether the chest has a double inventory.
	 * @return true if has a double inventory.
	 */
	public boolean isDouble() {
		return VanillaMaterials.CHEST.isDouble(getBlock());
	}

	@Override
	public void setOpened(Player player, boolean opened) {
		if (isDouble()) {
			Block block = getBlock();
			//MC will only play the chest open/close animation if you play it on the NE-most chest block in double chests
			if (block.translate(BlockFace.EAST).getMaterial() == VanillaMaterials.CHEST) {
				VanillaBlockMaterial.playBlockAction(block.translate(BlockFace.EAST), (byte) 1, opened ? (byte) 1 : (byte) 0);
			} else if (block.translate(BlockFace.NORTH).getMaterial() == VanillaMaterials.CHEST) {
				VanillaBlockMaterial.playBlockAction(block.translate(BlockFace.NORTH), (byte) 1, opened ? (byte) 1 : (byte) 0);
			}
		}
		super.setOpened(player, opened);
	}

	@Override
	public ChestInventory getInventory() {
		return getData().get(CHEST_INVENTORY);
	}

	public ChestInventory getLargestInventory() {
		final Block block = getBlock();
		final Block otherHalf = VanillaMaterials.CHEST.getOtherHalf(block);
		ChestInventory inventory = getInventory();
		if (otherHalf != null) {
			if (block.translate(BlockFace.EAST).equals(otherHalf) || block.translate(BlockFace.NORTH).equals(otherHalf)) {
				inventory = new ChestInventory(((Chest) otherHalf.getComponent()).getInventory(), inventory);
			} else {
				inventory = new ChestInventory(inventory, ((Chest) otherHalf.getComponent()).getInventory());
			}
		}
		return inventory;
	}

	@Override
	public boolean open(Player player) {
		ChestOpenEvent event = Spout.getEventManager().callEvent(new ChestOpenEvent(this, player));
		if (!event.isCancelled()) {
			// Finally open the window
			player.get(WindowHolder.class).openWindow(new ChestWindow(player, this));
			SoundEffects.RANDOM_CHESTOPEN.playGlobal(player.getScene().getPosition());
			super.open(player);
			return true;
		}
		return false;
	}

	@Override
	public boolean close(Player player) {
		ChestCloseEvent event = Spout.getEventManager().callEvent(new ChestCloseEvent(this, player));
		if (!event.isCancelled()) {
			return super.close(player);
		}
		return false;
	}
}
