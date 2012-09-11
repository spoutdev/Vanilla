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
package org.spout.vanilla.protocol.msg.player.pos;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class PlayerPositionYawMessage implements Message {
	private final PlayerPositionMessage position;
	private final PlayerYawMessage rotation;

	public PlayerPositionYawMessage(double x, double y, double z, double stance, float yaw, float pitch, boolean onGround) {
		position = new PlayerPositionMessage(x, y, z, stance, onGround);
		rotation = new PlayerYawMessage(yaw, pitch, onGround);
	}

	public PlayerPositionMessage getPlayerPositionMessage() {
		return position;
	}

	public PlayerYawMessage getPlayerLookMessage() {
		return rotation;
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	public double getStance() {
		return position.getStance();
	}

	public double getZ() {
		return position.getZ();
	}

	public float getYaw() {
		return rotation.getYaw();
	}

	public float getPitch() {
		return rotation.getPitch();
	}

	public boolean isOnGround() {
		return position.isOnGround();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("position", position)
				.append("rotation", rotation)
				.toString();
	}

	public Vector3 getLookingAtVector() {
		return rotation.getLookingAtVector();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerPositionYawMessage other = (PlayerPositionYawMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.position, other.position)
				.append(this.rotation, other.rotation)
				.isEquals();
	}
}
