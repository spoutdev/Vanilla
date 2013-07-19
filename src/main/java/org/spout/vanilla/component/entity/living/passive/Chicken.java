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
package org.spout.vanilla.component.entity.living.passive;

import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.math.GenericMath;
import org.spout.api.math.Vector3;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.ai.action.FollowMaterialHolderAction;
import org.spout.vanilla.ai.goal.FollowMaterialHolderGoal;
import org.spout.vanilla.ai.sensor.NearbyMaterialHolderSensor;
import org.spout.vanilla.component.entity.living.Animal;
import org.spout.vanilla.component.entity.living.Passive;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.substance.Item;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.CreatureProtocol;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

/**
 * A component that identifies the entity as a Chicken.
 */
public class Chicken extends Animal implements Passive {
	// Chicken lay eggs every 5-10 minutes.
	public static final int MINIMUM_EGG_BREEDING_TIME = 300;

	@Override
	public void onAttached() {
		super.onAttached();
		Float nextEgg = (float) (GenericMath.getRandom().nextInt(MINIMUM_EGG_BREEDING_TIME) + MINIMUM_EGG_BREEDING_TIME);
		getOwner().getData().put(VanillaData.TIME_TILL_EGG, nextEgg);
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new CreatureProtocol(CreatureType.CHICKEN));
		DeathDrops dropComponent = getOwner().add(DeathDrops.class);
		dropComponent.addDrop(new ItemStack(VanillaMaterials.FEATHER, getRandom().nextInt(2)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.RAW_CHICKEN, 1));
		dropComponent.addXpDrop((short) (getRandom().nextInt(3) + 1));

		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(4);
		}
		NearbyMaterialHolderSensor materialHolderSensor = new NearbyMaterialHolderSensor(getAI(), VanillaMaterials.SEEDS);
		materialHolderSensor.setSensorRadius(16);
		getAI().registerSensor(materialHolderSensor);
		getAI().registerGoal(new FollowMaterialHolderGoal(getAI()));
		getAI().registerAction(new FollowMaterialHolderAction(getAI()));
	}

	@Override
	public void onTick(float dt) {
		Float nextEggTime = getOwner().getData().get(VanillaData.TIME_TILL_EGG);
		nextEggTime -= dt;
		getOwner().getData().put(VanillaData.TIME_TILL_EGG, nextEggTime);
		if (shouldLayEgg()) {
			layEgg();
		}
	}

	private void layEgg() {
		Point position = getOwner().getPhysics().getPosition();
		Item.drop(position, new ItemStack(VanillaMaterials.EGG, 1), Vector3.ZERO);
		Float nextEgg = (float) (GenericMath.getRandom().nextInt(MINIMUM_EGG_BREEDING_TIME) + MINIMUM_EGG_BREEDING_TIME);
		getOwner().getData().put(VanillaData.TIME_TILL_EGG, nextEgg);
	}

	private boolean shouldLayEgg() {
		Float nextEggTime = getOwner().getData().get(VanillaData.TIME_TILL_EGG);
		return nextEggTime <= 0;
	}
}
