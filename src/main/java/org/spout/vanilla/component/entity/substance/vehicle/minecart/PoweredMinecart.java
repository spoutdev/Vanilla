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
package org.spout.vanilla.component.entity.substance.vehicle.minecart;

import java.util.ArrayList;
import java.util.List;

import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.util.Parameter;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.event.entity.EntityMetaChangeEvent;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.object.ObjectType;
import org.spout.vanilla.protocol.entity.object.vehicle.MinecartObjectEntityProtocol;
import org.spout.vanilla.util.PlayerUtil;

public class PoweredMinecart extends MinecartBase {
	private float fuel = 0f;
	private boolean fueled = false;

	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new MinecartObjectEntityProtocol(ObjectType.MINECART));
		if (getAttachedCount() == 1) {
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.FURNACE, 1));
		}
	}

	public void setFueled(boolean isFueled) {
		if (isFueled) {
			fuel = 180f;
		} else {
			fuel = 0f;
		}
		updateMetadata();
	}

	public boolean isFueled() {
		return fueled;
	}

	@Override
	public boolean canTick() {
		return fuel > 0f;
	}

	@Override
	public void onTick(float dt) {
		fuel -= dt;
		if (fuel <= 0f) {
			updateMetadata();
		}
	}

	@Override
	public void onInteract(final EntityInteractEvent event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case LEFT_CLICK:
					Slot slot = PlayerUtil.getHeldSlot(player);
					if (slot.get() != null) {
						ItemStack stack = slot.get();
						if (stack.getMaterial() instanceof Fuel) {
							setFueled(true);
							slot.addAmount(-1);
						}
					}
			}
		}
		super.onInteract(event);
	}

	private void updateMetadata() {
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();
		parameters.add(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, (byte) (this.fuel > 0f ? 1 : 0))); // Powered flag
		getOwner().getNetwork().callProtocolEvent(new EntityMetaChangeEvent(getOwner(), parameters));
	}

	@Override
	public int getMinecraftBlockID() {
		return VanillaMaterials.FURNACE.getMinecraftId();
	}
}
