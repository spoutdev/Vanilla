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
package org.spout.vanilla.plugin.component.substance.object.projectile;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.plugin.VanillaPlugin;
import org.spout.vanilla.plugin.component.living.passive.Chicken;
import org.spout.vanilla.plugin.component.misc.HealthComponent;
import org.spout.vanilla.plugin.component.substance.object.ObjectEntity;
import org.spout.vanilla.plugin.protocol.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.plugin.protocol.entity.object.ObjectType;

public class Egg extends ObjectEntity implements Projectile {
	private Entity shooter;

	@Override
	public void onAttached() {
		getOwner().getNetwork().setEntityProtocol(VanillaPlugin.VANILLA_PROTOCOL_ID, new ObjectEntityProtocol(ObjectType.EGG));
		super.onAttached();
	}

	@Override
	public Entity getShooter() {
		return shooter;
	}

	@Override
	public void setShooter(Entity shooter) {
		this.shooter = shooter;
	}

	@Override
	public void onCollided(Point colliderPoint, Point collidedPoint, Entity entity) {
		HealthComponent health = entity.get(HealthComponent.class);
		if (health != null) {
			health.damage(0);
		}
		spawnChicken(collidedPoint);
		
		getOwner().remove();
	}

	@Override
	public void onCollided(Point collidedroint, Point collidedPoint, Block block) {
		spawnChicken(collidedPoint);
		getOwner().remove();
	}
	
	private void spawnChicken(Point point) {
		if (this.getRandom().nextInt(8) == 0) { //1 chance out of 8 to spawn a egg on collision
			Entity toSpawn = point.getWorld().createEntity(point, Chicken.class);
			toSpawn.add(Chicken.class);
			point.getWorld().spawnEntity(toSpawn);
		}
	}
}
