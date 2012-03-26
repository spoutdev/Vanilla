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
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaController;

public class WanderAction extends EntityAction<VanillaController> {
	private static final double WANDER_FREQ = 2.75;
	private double freq = 0;

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
		float x = controller.getRandom().nextFloat() * 3 + 2;
		float y = 0;
		float z = controller.getRandom().nextFloat() * 3 + 2;

		Vector3 movement = new Vector3(x, y, z);
		Quaternion newRot = entity.getRotation().rotate(5 * dt, Vector3.UP);
		entity.setRotation(newRot);
		controller.move(Vector3.transform(movement.multiply(dt * 0.9), entity.getRotation()));
	}
}
