/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * vanilla is distributed in the hope that it will be useful,
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
package org.spout.vanilla.controller.object;

import org.spout.api.math.Vector3;
import org.spout.vanilla.controller.VanillaControllerType;

public abstract class MovingSubstance extends Substance {
	private Vector3 movedVelocity = Vector3.ZERO;
	private Vector3 velocity = Vector3.ZERO;
	private float maxSpeed; //might turn this into a vector too?

	protected MovingSubstance(VanillaControllerType type) {
		super(type);
	}

	/**
	 * Uses the current velocity and maximum speed to move this entity
	 */
	public void move() {
		this.movedVelocity = Vector3.min(this.velocity, new Vector3(this.maxSpeed, this.maxSpeed, this.maxSpeed));
		super.move(this.movedVelocity);
	}

	public final float getMaxSpeed() {
		return this.maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public final Vector3 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3 velocity) {
		this.velocity = velocity;
	}

	public final Vector3 getMovedVelocity() {
		return this.movedVelocity;
	}
}
