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
package org.spout.vanilla.controller.action;

import org.spout.api.entity.Entity;
import org.spout.api.entity.action.EntityAction;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaActionController;

public class WanderAction extends EntityAction<VanillaActionController> {
	private static final double WANDER_FREQ = 1;
	@SuppressWarnings("unused")
	private Vector3 movement = Vector3.ZERO;
	@SuppressWarnings("unused")
	private Quaternion rotation = Quaternion.IDENTITY;

	@Override
	public boolean shouldRun(Entity entity, VanillaActionController controller) {
		return controller.getRandom().nextDouble() * 3.0 >= WANDER_FREQ;
	}

	@Override
	public void run(Entity entity, VanillaActionController controller, float dt) {
		//Get the direction the entity is facing
		Vector3 entityForward = MathHelper.getDirectionVector(entity.getRotation());
		//Get somewhere we want to go.  Make sure it is length 1
		Vector3 randomTarget = new Vector3(Math.random(), 0, Math.random()).normalize();
		//Get the rotation to that target
		Quaternion rotationTo = entityForward.rotationTo(randomTarget);
		//Look at it
		entity.setRotation(rotationTo);
		//Move forward
		controller.setVelocity(MathHelper.getDirectionVector(entity.getRotation()).multiply(dt));
		controller.move(); //still needed?
	}
}
