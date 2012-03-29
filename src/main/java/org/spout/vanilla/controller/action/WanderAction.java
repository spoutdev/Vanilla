/*
 * This file is part of SpoutAPI (http://www.spout.org/).
 *
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.controller.action;

import org.spout.api.entity.Entity;
import org.spout.api.entity.action.EntityAction;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaController;

public class WanderAction extends EntityAction<VanillaController> {
	private static final double WANDER_FREQ = 2.75;
	private double freq = 0;
	private Vector3 movement = Vector3.ZERO;
	private Quaternion rotation = Quaternion.IDENTITY;

	@Override
	public boolean shouldRun(Entity entity, VanillaController controller) {
		freq = controller.getRandom().nextDouble() * 3.0;
		if (freq >= WANDER_FREQ) {
			return true;
		}
		return false;
	}

	@Override
	public void run(Entity entity, VanillaController controller, float dt) {
		//Grab current position
		Point p = entity.getPosition();

		//Grab the current vectored point
		Vector3 current = new Vector3(p.getX(), p.getY(), p.getZ()).normalize();

		//Set the movement vector
		movement = new Vector3(controller.getRandom().nextFloat() * 3 + 1, 0, controller.getRandom().nextFloat() * 3 + 1).normalize();

		Vector3 combined = Vector3.add(current, movement).normalize();

		//Compute the angle and the axis for rotation
		float angle = Vector3.dot(combined, movement);
		Vector3 axis = Vector3.cross(combined, movement);

		//Adjust our rotation quaternion
		rotation = new Quaternion((angle * dt), axis);

		//Rotate and move
		entity.setRotation(rotation);
		controller.move(Vector3.transform(movement.multiply(dt * 1.2), rotation));
	}
}
