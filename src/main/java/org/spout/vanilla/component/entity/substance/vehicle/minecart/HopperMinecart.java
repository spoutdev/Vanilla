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
package org.spout.vanilla.component.entity.substance.vehicle.minecart;

import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyFactory;

import org.spout.vanilla.component.entity.inventory.WindowHolder;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.inventory.block.HopperInventory;
import org.spout.vanilla.inventory.window.block.HopperWindow;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.object.ObjectType;
import org.spout.vanilla.protocol.entity.object.vehicle.MinecartObjectEntityProtocol;

public class HopperMinecart extends MinecartBase {
	private static final DefaultedKey<HopperInventory> HOPPER_INVENTORY = new DefaultedKeyFactory<HopperInventory>("hopper_inventory", HopperInventory.class);

	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new MinecartObjectEntityProtocol(ObjectType.MINECART));
		if (getAttachedCount() == 1) {
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.HOPPER, 1));
		}
	}

	public HopperInventory getInventory() {
		return this.getData().get(HOPPER_INVENTORY);
	}

	@Override
	public void onInteract(final EntityInteractEvent event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case RIGHT_CLICK:
					player.add(WindowHolder.class).openWindow(new HopperWindow(player, getInventory(), "Minecart"));
			}
		}
		super.onInteract(event);
	}

	@Override
	protected void onDestroy() {
		for (ItemStack stack : (ItemStack[]) getInventory().toArray()) {
			if (stack != null) {
				getOwner().get(DeathDrops.class).addDrop(stack);
			}
		}
		super.onDestroy();
	}

	@Override
	public int getMinecraftBlockID() {
		return VanillaMaterials.HOPPER.getMinecraftId();
	}
}
