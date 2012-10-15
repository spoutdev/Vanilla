/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.component.misc;

import java.util.List;

import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.substance.Item;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.event.entity.EntityCollectItemEvent;

/**
 * Component that adds a detector to entities to scan for and pickup items.
 */
public class PickupItemComponent extends EntityComponent {
	private final int DISTANCE = VanillaConfiguration.ITEM_PICKUP_RANGE.getInt();
	private List<Entity> nearbyEntities;

	@Override
	public boolean canTick() {
		if (!getOwner().getTransform().isPositionDirty()) {
			return false;
		}
		nearbyEntities = getOwner().getWorld().getNearbyEntities(getOwner(), DISTANCE);
		return !nearbyEntities.isEmpty();
	}

	@Override
	public void onTick(float dt) {
		for (Entity entity : nearbyEntities) {
			if (!entity.has(Item.class) || !entity.get(Item.class).canBeCollected()) {
				continue;
			}
			getOwner().getNetwork().callProtocolEvent(new EntityCollectItemEvent(getOwner(), entity));
			if (getOwner().has(Human.class)) {
				getOwner().get(Human.class).getInventory().add(entity.get(Item.class).getItemStack());
			}
			entity.remove();
		}
	}
}
