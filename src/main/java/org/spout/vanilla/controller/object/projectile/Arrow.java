/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.object.projectile;

import java.util.Random;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.math.Quaternion;
import org.spout.api.math.Vector3;

import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.object.Projectile;

public class Arrow extends Projectile {
	private static final Vector3 MAX_ARROW_SPEED = new Vector3(0.8, 0.8, 0.8); //TODO are these numbers right?

	protected Arrow() {
		super(VanillaControllerTypes.SHOT_ARROW, Quaternion.IDENTITY, MAX_ARROW_SPEED);
	}

	public Arrow(Quaternion rotation, float charge, float randomCharge) {
		this(rotation, charge, randomCharge, MAX_ARROW_SPEED);
	}

	public Arrow(Quaternion rotation, float charge, float randomCharge, Vector3 maxSpeed) {
		super(VanillaControllerTypes.SHOT_ARROW, rotation, maxSpeed);
		Random rand = new Random();
		Vector3 randVel = new Vector3(rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian()).multiply(randomCharge).multiply(0.0075);
		this.setVelocity(rotation.getAxisAngles().add(randVel).multiply(charge));
	}

	public Arrow(Quaternion rotation, Vector3 velocity, Vector3 maxSpeed) {
		super(VanillaControllerTypes.SHOT_ARROW, rotation, maxSpeed);
		this.setVelocity(velocity);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		// gravity
		this.setVelocity(this.getVelocity().subtract(0, 0.04, 0));
		this.move();
		// slow-down
		this.setVelocity(this.getVelocity().multiply(0.98));

		//TODO: proper entity on ground function
		Block below = getParent().getWorld().getBlock(getParent().getPosition().subtract(0.0, 0.2, 0.0), getParent());
		if (below.getMaterial().isSolid()) {
			//this.setVelocity(this.getVelocity().multiply(0.7, -0.5, 0.7));
			this.setVelocity(this.getVelocity().multiply(0.7, 0.0, 0.7).add(0.0, 0.06, 0.0));
		}
	}
}
