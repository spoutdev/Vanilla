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
package org.spout.vanilla.plugin.component.living.hostile;

import com.bulletphysics.collision.shapes.BoxShape;

import org.spout.api.component.impl.PhysicsComponent;
import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.api.component.Hostile;
import org.spout.vanilla.api.data.Difficulty;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.ai.action.ActionAttack;
import org.spout.vanilla.plugin.ai.goal.AttackPlayerGoal;
import org.spout.vanilla.plugin.ai.sensor.NearbyComponentsSensor;
import org.spout.vanilla.plugin.component.inventory.VanillaEntityInventory;
import org.spout.vanilla.plugin.component.living.Living;
import org.spout.vanilla.plugin.component.living.neutral.Human;
import org.spout.vanilla.plugin.component.misc.DamageComponent;
import org.spout.vanilla.plugin.component.misc.DropComponent;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.data.VanillaData;
import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.protocol.entity.creature.ZombieEntityProtocol;

/**
 * A component that identifies the entity as a Zombie.
 */
public class Zombie extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new ZombieEntityProtocol());
		PhysicsComponent physics = getOwner().add(PhysicsComponent.class);
		getOwner().add(DropComponent.class).addDrop(new ItemStack(VanillaMaterials.ROTTEN_FLESH, getRandom().nextInt(2))).addXpDrop((short) 5);
		VanillaEntityInventory inventory = getOwner().add(VanillaEntityInventory.class);

		physics.setMass(5f);
		physics.setCollisionShape(new BoxShape(1F, 2F, 1F));
		physics.setFriction(10f);
		physics.setRestitution(0f);

		if (getAttachedCount() == 1) {
			getOwner().add(HealthComponent.class).setSpawnHealth(20);
		}

		DamageComponent damage = getOwner().add(DamageComponent.class);
		damage.getDamageLevel(Difficulty.EASY).setAmount(3);
		damage.getDamageLevel(Difficulty.NORMAL).setAmount(4);
		damage.getDamageLevel(Difficulty.HARD).setAmount(6);
		damage.getDamageLevel(Difficulty.HARDCORE).setAmount(damage.getDamageLevel(Difficulty.HARD).getAmount());

		final NearbyComponentsSensor humanSensor = new NearbyComponentsSensor(getAI(), Human.class);
		humanSensor.setSensorRadius(10);
		getAI().registerSensor(humanSensor);
		//Go attack nearby players AI
		getAI().registerGoal(new AttackPlayerGoal(getAI()));
		getAI().registerAction(new ActionAttack(getAI()));
	}

	/**
	 * True if the zombie was once a villager, or is a Villager Zombie
	 * @return true if this is a villager zombie
	 */
	public boolean wasVillager() {
		return getOwner().getData().get(VanillaData.WAS_VILLAGER);
	}

	/**
	 * Sets if this is a villager zombie.
	 * @param value
	 */
	public void setWasVillager(boolean value) {
		getOwner().getData().put(VanillaData.WAS_VILLAGER, value);
	}

	@Override
	public void onCollided(Point colliderPoint, Point collidedPoint, Entity entity) {
		System.out.println("COLLIDED WITH A " + entity);
		entity.get(HealthComponent.class).damage(getOwner().get(DamageComponent.class).getDamageLevel(colliderPoint.getWorld().getDataMap().get(VanillaData.DIFFICULTY)).getAmount());
	}
}
