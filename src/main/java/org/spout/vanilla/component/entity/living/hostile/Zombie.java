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
package org.spout.vanilla.component.entity.living.hostile;

import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.event.entity.EntityCollideEntityEvent;
import org.spout.api.event.entity.EntityCollideEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;

import org.spout.physics.collision.shape.BoxShape;

import org.spout.vanilla.ai.action.ActionAttack;
import org.spout.vanilla.ai.goal.AttackPlayerGoal;
import org.spout.vanilla.ai.sensor.NearbyComponentsSensor;
import org.spout.vanilla.component.entity.inventory.EntityInventory;
import org.spout.vanilla.component.entity.living.Hostile;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.Damage;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.EntityItemCollector;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.ZombieEntityProtocol;

/**
 * A component that identifies the entity as a Zombie.
 */
public class Zombie extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new ZombieEntityProtocol());
		getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.ROTTEN_FLESH, getRandom().nextInt(2))).addXpDrop((short) 5);
		getOwner().add(EntityInventory.class);
		getOwner().add(EntityItemCollector.class);

		PhysicsComponent physics = getOwner().getPhysics();
		physics.activate(2f, new BoxShape(1f, 2f, 1f), false, true);
		physics.setFriction(10f);
		physics.setRestitution(0f);

		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(20);
		}

		Damage damage = getOwner().add(Damage.class);
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
	 *
	 * @return true if this is a villager zombie
	 */
	public boolean wasVillager() {
		return getOwner().getData().get(VanillaData.WAS_VILLAGER);
	}

	/**
	 * Sets if this is a villager zombie.
	 */
	public void setWasVillager(boolean value) {
		getOwner().getData().put(VanillaData.WAS_VILLAGER, value);
	}

	@Override
	public void onCollided(EntityCollideEvent event) {
		if (event instanceof EntityCollideEntityEvent) {
			Point point = ((EntityCollideEntityEvent) event).getCollided().getPhysics().getPosition();
			Health health = ((EntityCollideEntityEvent) event).getCollided().get(Health.class);
			if (health != null) {
				health.damage(getOwner().get(Damage.class).getDamageLevel(point.getWorld().getData().get(VanillaData.DIFFICULTY)).getAmount());
			}
		} else {
			//TODO: Fall damage
		}
	}
}
