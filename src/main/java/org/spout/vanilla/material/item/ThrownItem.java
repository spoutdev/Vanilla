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
package org.spout.vanilla.material.item;

import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.Action;

import org.spout.math.vector.Vector3;
import org.spout.physics.collision.shape.SphereShape;
import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.component.entity.substance.projectile.Projectile;

/**
 * Represents an Item that can be thrown away at a speed by right-click interaction. After throwing, one count of the selected item is removed from the thrower its selected slot, if the thrower is not
 * suppressing this cost of shooting the item.
 */
public abstract class ThrownItem extends EntitySpawnItem<Substance> {
	private float launchForce = 250.0f;

	public ThrownItem(String name, int id, Class<? extends Substance> itemThrown) {
		super(name, id, null);
		this.setSpawnedComponent(itemThrown);
	}

	/**
	 * Gets the force at which Entities are thrown away
	 *
	 * @return launchForce
	 */
	public float getLaunchForce() {
		return this.launchForce;
	}

	/**
	 * Sets the force at which Entities are thrown away
	 *
	 * @param launchForce to set to
	 */
	public void setLaunchForce(float launchForce) {
		this.launchForce = launchForce;
	}

	/**
	 * Called after an Entity was just spawned and is ready to be thrown
	 *
	 * @param spawnedComponent of the Entity that was spawned
	 * @param thrower of the Spawned Component
	 */
	public void onThrown(Substance spawnedComponent, Entity thrower) {
		this.handleSelectionRemove(thrower);
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		onInteract(entity, type, 10f);
	}

	public void onInteract(Entity entity, Action type, float mass) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK) {
			Substance item = this.spawnEntity(entity, new Vector3(0, 1.6f, 0));
			PhysicsComponent physics = item.getOwner().getPhysics();
			physics.activate(mass, new SphereShape(1f), false, true);
			if (item instanceof Projectile) {
				((Projectile) item).setShooter(entity);
			}
			physics.force(entity.getPhysics().getRotation().getDirection().mul(getLaunchForce()));
			this.onThrown(item, entity);
		}
	}
}
