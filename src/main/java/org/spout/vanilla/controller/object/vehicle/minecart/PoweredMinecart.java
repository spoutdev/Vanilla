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
package org.spout.vanilla.controller.object.vehicle.minecart;

import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;
import org.spout.vanilla.controller.object.vehicle.Vehicle;
import org.spout.vanilla.controller.object.vehicle.Minecart;

public class PoweredMinecart extends Minecart implements Vehicle {

	private int fuelTicks = 0;
	private Vector2 pushVelocity = Vector2.ZERO;
	
	@Override
	public void onPostMove(float dt) {
		if (this.isOnRail()) {
			Vector3 velocity = this.getVelocity();
			double fuelPower = this.pushVelocity.length();
			if (fuelPower > 0.01) {
				this.pushVelocity = this.pushVelocity.divide(fuelPower);
				velocity = velocity.multiply(0.8, 0.0, 0.8);
				
				final double boost = 0.04;
				
				velocity = velocity.add(this.pushVelocity.multiply(boost).toVector3(0f));
			} else {
				velocity = velocity.multiply(0.9, 0.0, 0.9);
			}
			this.setVelocity(velocity);
		} else {
			super.onPostMove(dt);
		}
	}
	
	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		
		if (this.fuelTicks > 0 && --this.fuelTicks == 0) {
			this.pushVelocity = Vector2.ZERO;
			//TODO: Change meta data to stop smoking
		}
	}
	
	@Override
	public void onVelocityUpdated(float dt) {
		super.onVelocityUpdated(dt);
		
		double velLength = this.pushVelocity.length();
		Vector3 velocity = this.getVelocity();
		if (velLength > 0.01 && velocity.lengthSquared() > 0.001f) {
			this.pushVelocity = this.pushVelocity.divide(velLength);
			if (this.pushVelocity.getX() * velocity.getX() + this.pushVelocity.getY() * velocity.getZ() < 0) {
				this.pushVelocity = Vector2.ZERO;
			} else {
				this.pushVelocity = new Vector2(velocity.getX(), velocity.getZ());
			}
		}
	}
}
