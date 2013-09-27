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
package org.spout.vanilla.component.entity.player;

import org.spout.api.component.entity.EntityComponent;
import org.spout.api.component.entity.PhysicsComponent;
import org.spout.api.entity.Player;
import org.spout.api.entity.state.PlayerInputState;
import org.spout.api.geo.discrete.Transform;
import org.spout.math.imaginary.Quaternion;
import org.spout.math.vector.Vector3;
import org.spout.vanilla.component.entity.misc.EntityHead;

public class PlayerMovementExecutor extends EntityComponent {

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new UnsupportedOperationException("PlayerMovementExecutor can only be attached to players!");
		}
	}

	// TODO: vanilla input
	@Override
	public void onTick(float dt) {
		Player player = (Player) getOwner();
		Transform ts = player.getPhysics().getTransform();
		PlayerInputState inputState = player.input();
		PhysicsComponent sc = player.getPhysics();

		Vector3 offset = Vector3.ZERO;
		float speed = 50;
		if (inputState.getForward()) {
			offset = offset.sub(ts.forwardVector().mul(speed * dt));
		}
		if (inputState.getBackward()) {
			offset = offset.add(ts.forwardVector().mul(speed * dt));
		}
		if (inputState.getLeft()) {
			offset = offset.sub(ts.rightVector().mul(speed * dt));
		}
		if (inputState.getRight()) {
			offset = offset.add(ts.rightVector().mul(speed * dt));
		}
		if (inputState.getJump()) {
			offset = offset.add(ts.upVector().mul(speed * dt));
		}
		if (inputState.getCrouch()) {
			offset = offset.sub(ts.upVector().mul(speed * dt));
		}

		player.get(EntityHead.class).setOrientation(Quaternion.fromAxesAnglesDeg(inputState.pitch(), inputState.yaw(), ts.getRotation().getAxesAngleDeg().getZ()));
		player.getPhysics().translate(offset);
		player.getPhysics().setRotation(Quaternion.fromAxesAnglesDeg(inputState.pitch(), inputState.yaw(), ts.getRotation().getAxesAngleDeg().getZ()));
	}
}
