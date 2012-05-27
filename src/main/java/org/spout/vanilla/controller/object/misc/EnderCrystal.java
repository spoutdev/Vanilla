/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.controller.object.misc;

import org.spout.api.entity.type.ControllerType;
import org.spout.api.entity.type.EmptyConstructorControllerType;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.object.Substance;
import org.spout.vanilla.controller.source.HealthChangeReason;

public class EnderCrystal extends Substance {
	public static final ControllerType TYPE = new EmptyConstructorControllerType(EnderCrystal.class, "Ender Crystal");

	public EnderCrystal() {
		super(VanillaControllerTypes.ENDER_CRYSTAL);
	}

	@Override
	public void onAttached() {
		//TODO Remove when collisions are fixed.
		setMaxHealth(1);
		setHealth(1, new HealthChangeReason(HealthChangeReason.Type.SPAWN));
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		//		Point point = getParent().getLastTransform().getPosition();
		//		if (getParent().isDead()) {
		//			ExplosionModels.SPHERICAL.execute(point, 4.0f);
		//
		//			Set<Entity> entities = point.getWorld().getRegion((int) point.getX(), (int) point.getY(), (int) point.getZ()).getAll();
		//
		//			Vector3 explosionMaximun = new Vector3(point.getX() + 6, point.getY() + 6, point.getZ() + 6);
		//			for (Entity e : entities) {
		//				Point p = e.getPosition();
		//				if (Vector3.distance(p, explosionMaximun) <= 6) {
		//					//TODO move this to an explosion utility class as it will calculate both damage and knockback of entities
		//				}
		//			}
		//		} // TODO: Zidane fix this you jerk. NPEs on point.getWorld().getRegion()..etc
		if (getParent().getPitch() != 0) {
			pitch(0.0f);
		}
		yaw(getRandom().nextFloat() * 360f);
	}
}
