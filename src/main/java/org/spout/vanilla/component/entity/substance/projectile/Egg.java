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
package org.spout.vanilla.component.entity.substance.projectile;

import org.spout.api.entity.Entity;
import org.spout.api.event.entity.EntityCollideEntityEvent;
import org.spout.api.event.entity.EntityCollideEvent;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.component.entity.living.passive.Chicken;
import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.component.entity.substance.Substance;
import org.spout.vanilla.protocol.entity.object.ObjectEntityProtocol;
import org.spout.vanilla.protocol.entity.object.ObjectType;

public class Egg extends Substance implements Projectile {
	private Entity shooter;

	@Override
	public void onAttached() {
		setEntityProtocol(new ObjectEntityProtocol(ObjectType.EGG));
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
	public void onCollided(EntityCollideEvent event) {
		if (event instanceof EntityCollideEntityEvent) {
			Health health = ((EntityCollideEntityEvent) event).getCollided().get(Health.class);
			if (health != null) {
				health.damage(0);
			}
		}
		spawnChickens(new Point(event.getContactInfo().getNormal(), getOwner().getWorld()));
		getOwner().remove();
	}

	/**
	 * Spawns a chicken by a chance of 1/8. If a chicken is spawned, there is an additional chance of 1/32 to spawn four instead of one chicken.
	 *
	 * @param point the point the chicken(s) will spawn at.
	 */
	@SuppressWarnings ("unchecked")
	private void spawnChickens(Point point) {
		if (getRandom().nextInt(8) == 0) {
			int chickensToSpawn = 1;
			if (getRandom().nextInt(32) == 0) {
				chickensToSpawn = 4;
			}
			for (int i = 0; i < chickensToSpawn; i++) {
				point.getWorld().createAndSpawnEntity(point, LoadOption.NO_LOAD, Chicken.class);
			}
		}
	}
}
