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
package org.spout.vanilla.component.misc;

import org.spout.api.component.components.PhysicsComponent;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector3;

/**
 * Extension of the PhysicsComponent to put bounds on velocity.
 * <p/>
 * Basically, the official protocol handles it like this...
 * - Motion is in block units, each tick at 1/32000
 * - Velocity is clamped at a min of -0.9 and a max of 0.9 blocks per tick
 * - Protocol velocity values are simply multiplying blocks by the max block units. This leaves
 * a maximum of 28800 per axis per tick for protocol motion.
 */
public class VanillaPhysicsComponent extends PhysicsComponent {
	@Override
	public void setVelocity(Vector3 velocity) {
		final double x = MathHelper.clamp(velocity.getX(), -0.9, 0.9);
		final double y = MathHelper.clamp(velocity.getY(), -0.9, 0.9);
		final double z = MathHelper.clamp(velocity.getZ(), -0.9, 0.9);
		super.setVelocity(new Vector3(x, y, z));
	}

	/**
	 * Gets the velocity scaled for the official protocol's specifications
	 * @return
	 */
	public Vector3 getProtocolVelocity() {
		final Vector3 velocity = getVelocity();
		final float x = velocity.getX() * 32000;
		final float y = velocity.getY() * 32000;
		final float z = velocity.getZ() * 32000;
		return new Vector3(x, y, z);
	}
}
