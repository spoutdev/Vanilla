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
package org.spout.vanilla.component.entity.living.passive;

import java.util.Random;

import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityInteractEvent;
import org.spout.api.event.player.PlayerInteractEntityEvent;
import org.spout.api.inventory.ItemStack;
import org.spout.api.inventory.Slot;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.ai.action.FollowMaterialHolderAction;
import org.spout.vanilla.ai.goal.FollowMaterialHolderGoal;
import org.spout.vanilla.ai.sensor.NearbyMaterialHolderSensor;
import org.spout.vanilla.component.entity.living.Animal;
import org.spout.vanilla.component.entity.living.Passive;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.inventory.entity.QuickbarInventory;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.entity.creature.CreatureType;
import org.spout.vanilla.util.PlayerUtil;

/**
 * A component that identifies the entity as a Cow.
 */
public class Cow extends Animal implements Passive {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreatureProtocol(CreatureType.COW));
		DeathDrops dropComponent = getOwner().add(DeathDrops.class);
		Random random = getRandom();
		dropComponent.addDrop(new ItemStack(VanillaMaterials.RAW_BEEF, random.nextInt(2) + 1));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.LEATHER, random.nextInt(2)));
		dropComponent.addXpDrop((short) (getRandom().nextInt(3) + 1));
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(10);
		}
		NearbyMaterialHolderSensor materialHolderSensor = new NearbyMaterialHolderSensor(getAI(), VanillaMaterials.WHEAT);
		materialHolderSensor.setSensorRadius(16);
		getAI().registerSensor(materialHolderSensor);
		getAI().registerGoal(new FollowMaterialHolderGoal(getAI()));
		getAI().registerAction(new FollowMaterialHolderAction(getAI()));
	}

	@Override
	public void onInteract(final EntityInteractEvent event) {
		if (event instanceof PlayerInteractEntityEvent) {
			final PlayerInteractEntityEvent pie = (PlayerInteractEntityEvent) event;
			final Player player = (Player) pie.getEntity();
			switch (pie.getAction()) {
				case RIGHT_CLICK:
					final QuickbarInventory playerQuickbar = PlayerUtil.getQuickbar(player);
					if (playerQuickbar == null) {
						return;
					}
					final Slot selected = playerQuickbar.getSelectedSlot();
					if (selected.get() != null && selected.get().equalsIgnoreSize(new ItemStack(VanillaMaterials.BUCKET, 0))) {
						selected.addAmount(-1);
						playerQuickbar.add(new ItemStack(VanillaMaterials.MILK_BUCKET, 1));
					}
			}
		}
	}
}
