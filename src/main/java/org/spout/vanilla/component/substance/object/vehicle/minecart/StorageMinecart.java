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
package org.spout.vanilla.component.substance.object.vehicle.minecart;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.inventory.ItemStack;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyFactory;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.inventory.WindowHolder;
import org.spout.vanilla.component.misc.EntityDropComponent;
import org.spout.vanilla.inventory.block.ChestInventory;
import org.spout.vanilla.inventory.window.block.chest.ChestWindow;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.object.ObjectType;
import org.spout.vanilla.protocol.entity.object.vehicle.MinecartObjectEntityProtocol;

public class StorageMinecart extends MinecartBase {
	public static final DefaultedKey<ChestInventory> CHEST_INVENTORY = new DefaultedKeyFactory<ChestInventory>("chest_inventory", ChestInventory.class);

	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new MinecartObjectEntityProtocol(ObjectType.STORAGE_MINECART));
		if (getAttachedCount() == 1) {
			getOwner().add(EntityDropComponent.class).addDrop(new ItemStack(VanillaMaterials.CHEST, 1));
		}
	}

	public ChestInventory getInventory() {
		return this.getData().get(CHEST_INVENTORY);
	}

	@Override
	public void onInteract(Action action, Entity source) {
		if (!(source instanceof Player)) {
			return;
		}
		if (Action.RIGHT_CLICK.equals(action)) {
			source.add(WindowHolder.class).openWindow(new ChestWindow((Player) source, getInventory(), "Minecart"));
		}
		super.onInteract(action, source);
	}

	@Override
	protected void onDestroy() {
		for (ItemStack stack : (ItemStack[]) getInventory().toArray()) {
			if (stack != null) {
				getOwner().get(EntityDropComponent.class).addDrop(stack);
			}
		}
		super.onDestroy();
	}
}
