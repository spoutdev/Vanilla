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
package org.spout.vanilla.component.entity.minecart.type;

import org.spout.api.Platform;
import org.spout.api.Spout;
import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;
import org.spout.api.material.BlockMaterial;
import org.spout.vanilla.component.entity.minecart.MinecartType;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.MetadataComponent;
import org.spout.vanilla.data.Metadata;
import org.spout.vanilla.material.Fuel;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.util.PlayerUtil;

public class PoweredMinecartType extends MinecartType {
	private float fuel = 0f;

	@Override
	public void onAttached() {
		super.onAttached();
		if (getAttachedCount() == 1) {
			getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.FURNACE, 1));
		}

		// Add smoking puff effect metadata
		getOwner().add(MetadataComponent.class).addMeta(new Metadata<Byte>(Metadata.TYPE_BYTE, 16) {
			@Override
			public Byte getValue() {
				return isFueled() ? (byte) 1 : (byte) 0;
			}

			@Override
			public void setValue(Byte value) {
				setFueled(value.byteValue() == (byte) 1);
			}
		});
	}

	@Override
	public void onDetached() {
		super.onDetached();

		// Unregister smoking puff metadata
		MetadataComponent metadata = getOwner().get(MetadataComponent.class);
		if (metadata != null) {
			metadata.removeMeta(16);
		}
	}

	public void setFueled(boolean isFueled) {
		fuel = isFueled ? 180f : 0f;
	}

	public boolean isFueled() {
		return fuel > 0f;
	}

	@Override
	public boolean canTick() {
		return isFueled();
	}

	@Override
	public void onTick(float dt) {
		// Do not change the fuel counter on the client
		if (Spout.getPlatform() == Platform.SERVER) {
			fuel -= dt;
			if (fuel <= 0f) {
				fuel = 0f;
			}
		}
	}

	@Override
	public void onInteract(final EntityInteractEvent<?> event) {
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

	@Override
	public BlockMaterial getDefaultDisplayedBlock() {
		return VanillaMaterials.FURNACE;
	}
}
