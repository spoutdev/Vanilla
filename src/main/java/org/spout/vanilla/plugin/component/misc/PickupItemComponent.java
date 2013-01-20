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
package org.spout.vanilla.plugin.component.misc;

import java.util.List;

import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;

import org.spout.vanilla.plugin.component.inventory.PlayerInventory;
import org.spout.vanilla.plugin.component.substance.Item;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.event.entity.EntityCollectItemEvent;

/**
 * Component that adds a detector to resources.entities to scan for and pickup items.
 */
public class PickupItemComponent extends EntityComponent {
	private final int DISTANCE = VanillaConfiguration.ITEM_PICKUP_RANGE.getInt();
	private List<Entity> nearbyEntities;

	@Override
	public boolean canTick() {
		HealthComponent healthComponent = getOwner().get(HealthComponent.class);
		if (healthComponent != null && healthComponent.isDead()) {
			return false;
		}
		nearbyEntities = getOwner().getWorld().getNearbyEntities(getOwner(), DISTANCE);
		return !nearbyEntities.isEmpty();
	}

	@Override
	public void onTick(float dt) {
		for (Entity entity : nearbyEntities) {
			Item item = entity.get(Item.class);
			if (item != null && item.canBeCollected()) {
				getOwner().getNetwork().callProtocolEvent(new EntityCollectItemEvent(getOwner(), entity));
				PlayerInventory inv = getOwner().get(PlayerInventory.class);
				if (inv != null) {
					inv.add(item.getItemStack());
				}
				entity.remove();
			}
		}
	}
}
