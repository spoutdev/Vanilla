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
package org.spout.vanilla.protocol.msg.player.pos;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.imaginary.Quaternionf;
import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public final class PlayerPositionLookMessage extends VanillaMainChannelMessage {
	private final PlayerPositionMessage position;
	private final PlayerLookMessage rotation;

	public PlayerPositionLookMessage(double x, double y, double z, double stance, float yaw, float pitch, boolean onGround, RepositionManager rm) {
		position = new PlayerPositionMessage(x, y, z, stance, onGround, rm);
		rotation = new PlayerLookMessage(yaw, pitch, onGround);
	}

	public PlayerPositionLookMessage(double x, double y, double z, double stance, float yaw, float pitch, boolean onGround, int channelId, RepositionManager rm) {
		super(channelId);
		position = new PlayerPositionMessage(x, y, z, stance, onGround, rm);
		rotation = new PlayerLookMessage(yaw, pitch, onGround);
	}

	public PlayerPositionMessage getPlayerPositionMessage() {
		return position;
	}

	public PlayerLookMessage getPlayerLookMessage() {
		return rotation;
	}

	public double getX() {
		return position.getX();
	}

	public double getY() {
		return position.getY();
	}

	public double getZ() {
		return position.getZ();
	}

	public double getStance() {
		return position.getStance();
	}

	public Quaternionf getRotation() {
		return rotation.getRotation();
	}

	public float getYaw() {
		return getRotation().getAxesAngleDeg().getY();
	}

	public float getPitch() {
		return getRotation().getAxesAngleDeg().getX();
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerPositionLookMessage other = (PlayerPositionLookMessage) obj;
		return new EqualsBuilder()
				.append(this.position, other.position)
				.append(this.rotation, other.rotation)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(this.position)
				.append(this.rotation)
				.toHashCode();
	}
}
