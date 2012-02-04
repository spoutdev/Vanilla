/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.entity;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.atomic.Transform;
import org.spout.api.math.Vector3;
import org.spout.api.math.Vector3m;
import org.spout.api.protocol.Message;
import org.spout.vanilla.VanillaMaterials;
import org.spout.vanilla.entity.living.player.MinecraftPlayer;
import org.spout.vanilla.protocol.msg.EntityRotationMessage;
import org.spout.vanilla.protocol.msg.EntityTeleportMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionMessage;
import org.spout.vanilla.protocol.msg.RelativeEntityPositionRotationMessage;

/**
 * Moving entity controller
 */
public abstract class MovingEntity extends MinecraftEntity {
	protected final Vector3m velocity = new Vector3m(Vector3.ZERO);
	private int fireTicks;
	private boolean flammable;

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
//		checkWeb();
		updateMovement(dt);
		checkFireTicks();
	}

	private void updateMovement(float dt) {
		Transform t = parent.getLiveTransform();
		t.setPosition(t.getPosition().add(velocity));
		//TODO: collision
	}

	@Override
	public void preSnapshot() {

	}

	private void checkWeb() {
		Point pos = parent.getTransform().getPosition();
		if (pos.getWorld().getBlock(pos).getBlockMaterial().equals(VanillaMaterials.WEB)) {
			velocity.set(Vector3.ZERO);
		}
	}

	private void checkFireTicks() {
		if (fireTicks > 0) {
			if (!flammable) {
				fireTicks -= 4;
				if (fireTicks < 0) {
					fireTicks = 0;
				}
				return;
			}

			if (fireTicks % 20 == 0) {
				//TODO: damage entity
			}
			--fireTicks;
		}
	}

	private void checkLava() {
		//The code checks for lava within the entity's bounding box shrunk by:
		//-0.10000000149011612D, -0.4000000059604645D, -0.10000000149011612D
	}

	public boolean isFlammable() {
		return flammable;
	}

	public void setFlammable(boolean flammable) {
		this.flammable = flammable;
	}

	public int getFireTicks() {
		return fireTicks;
	}

	public void setFireTicks(int fireTicks) {
		this.fireTicks = fireTicks;
	}

	public Vector3 getVelocity() {
		return velocity;
	}
}
