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
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.Parameter;
import org.spout.physics.collision.shape.BoxShape;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.ai.action.ActionAttack;
import org.spout.vanilla.ai.goal.AttackPlayerGoal;
import org.spout.vanilla.ai.sensor.NearbyComponentsSensor;
import org.spout.vanilla.component.entity.living.Hostile;
import org.spout.vanilla.component.entity.living.Human;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.CreeperEntityProtocol;

/**
 * A component that identifies the entity as a Creeper.
 */
public class Creeper extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		setEntityProtocol(new CreeperEntityProtocol());
		getOwner().add(DeathDrops.class).addDrop(new ItemStack(VanillaMaterials.GUNPOWDER, getRandom().nextInt(2))).addXpDrop((short) 5);
		PhysicsComponent physics = getOwner().getPhysics();
		physics.activate(2f, new BoxShape(1f, 2f, 1f), false, true);
		physics.setFriction(10f);
		physics.setRestitution(0f);
		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(20);
		}

		NearbyComponentsSensor humanSensor = new NearbyComponentsSensor(getAI(), Human.class);
		humanSensor.setSensorRadius(16);
		getAI().registerSensor(humanSensor);
		getAI().registerGoal(new AttackPlayerGoal(getAI()));
		getAI().registerAction(new ActionAttack(getAI()));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
	}

	public float getFuse() {
		return getData().get(VanillaData.CREEPER_FUSE);
	}

	public void setFuse(float fuse) {
		getData().put(VanillaData.CREEPER_FUSE, fuse);
	}

	public float getExplosionRadius() {
		return getData().get(VanillaData.EXPLOSION_RADIUS);
	}

	public void setExplosionRadius(float radius) {
		getData().put(VanillaData.EXPLOSION_RADIUS, radius);
	}

	public byte getState() {
		return getData().get(VanillaData.STATE);
	}

	public void setState(byte state) {
		getData().put(VanillaData.STATE, state);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 16, state));
	}

	public boolean isCharged() {
		return getData().get(VanillaData.CHARGED);
	}

	public void setCharged(boolean charged) {
		getData().put(VanillaData.CHARGED, charged);
		setMetadata(new Parameter<Byte>(Parameter.TYPE_BYTE, 17, charged ? (byte) 1 : 0));
	}
}
