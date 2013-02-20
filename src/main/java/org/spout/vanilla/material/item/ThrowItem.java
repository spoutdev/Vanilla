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
package org.spout.vanilla.material.item;

import com.bulletphysics.collision.shapes.SphereShape;

import org.spout.api.component.impl.SceneComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.player.PlayerInteractEvent.Action;
import org.spout.api.geo.World;
import org.spout.api.math.Vector3;
import org.spout.api.math.VectorMath;

import org.spout.vanilla.component.substance.object.ObjectEntity;
import org.spout.vanilla.component.substance.object.projectile.Projectile;

public abstract class ThrowItem extends VanillaItemMaterial {
	private Class<? extends ObjectEntity> itemThrown;

	public ThrowItem(String name, int id, Class<? extends ObjectEntity> itemThrown) {
		super(name, id, null);
		this.itemThrown = itemThrown;
	}

	@Override
	public void onInteract(Entity entity, Action type) {
		onInteract(entity, type, 5f);
	}

	public void onInteract(Entity entity, Action type, float mass) {
		super.onInteract(entity, type);
		if (type == Action.RIGHT_CLICK) {
			World world = entity.getWorld();
			ObjectEntity item = world.createEntity(entity.getScene().getPosition().add(0, 1.6f, 0), itemThrown).add(itemThrown);
			SceneComponent scene = item.getOwner().getScene();
			scene.setShape(mass, new SphereShape(0.1f)); // TODO: Correct this
			scene.impulse(VectorMath.getDirection(entity.getScene().getRotation()).multiply(30)); //TODO: Need real parameters
			if (item instanceof Projectile) {
				((Projectile) item).setShooter(entity);
			}
			world.spawnEntity(item.getOwner());
		}
	}
}
