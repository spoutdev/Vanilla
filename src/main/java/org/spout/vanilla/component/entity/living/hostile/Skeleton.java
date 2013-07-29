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

import java.util.Random;

import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.inventory.ItemStack;
import org.spout.physics.collision.shape.BoxShape;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.inventory.EntityInventory;
import org.spout.vanilla.component.entity.living.Hostile;
import org.spout.vanilla.component.entity.living.Living;
import org.spout.vanilla.component.entity.misc.DeathDrops;
import org.spout.vanilla.component.entity.misc.EntityItemCollector;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.SkeletonEntityProtocol;

/**
 * A component that identifies the entity as a Skeleton.
 */
public class Skeleton extends Living implements Hostile {
	@Override
	public void onAttached() {
		super.onAttached();
		Random random = getRandom();

		setEntityProtocol(new SkeletonEntityProtocol());
		getOwner().add(EntityInventory.class);
		getOwner().add(EntityItemCollector.class);

		//Physics
		PhysicsComponent physics = getOwner().getPhysics();
		physics.activate(2f, new BoxShape(1f, 2f, 1f), false, true);
		physics.setFriction(10f);
		physics.setRestitution(0f);

		DeathDrops dropComponent = getOwner().add(DeathDrops.class);
		dropComponent.addDrop(new ItemStack(VanillaMaterials.ARROW, random.nextInt(2)));
		dropComponent.addDrop(new ItemStack(VanillaMaterials.BONE, random.nextInt(2)));
		dropComponent.addXpDrop((short) 5);

		if (getAttachedCount() == 1) {
			getOwner().add(Health.class).setSpawnHealth(20);
		}

		//TODO: There's 2 kind of damage for Skele's : Sword & Bow
	}
}
