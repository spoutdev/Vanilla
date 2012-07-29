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
package org.spout.vanilla.protocol.msg.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.math.Vector3;
import org.spout.api.util.SpoutToStringStyle;
import org.spout.vanilla.protocol.msg.EntityMessage;

public final class EntityRelativePositionRotationMessage extends EntityMessage {
	private final int deltaX, deltaY, deltaZ, rotation, pitch;

	public EntityRelativePositionRotationMessage(int id, Vector3 deltaPosition, int rotation, int pitch) {
		this(id, (int) deltaPosition.getX(), (int) deltaPosition.getY(), (int) deltaPosition.getZ(), rotation, pitch);
	}

	public EntityRelativePositionRotationMessage(int id, int deltaX, int deltaY, int deltaZ, int rotation, int pitch) {
		super(id);
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaZ = deltaZ;
		this.rotation = rotation;
		this.pitch = pitch;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public int getDeltaZ() {
		return deltaZ;
	}

	public int getRotation() {
		return rotation;
	}

	public int getPitch() {
		return pitch;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("deltaX", deltaX)
				.append("deltaY", deltaY)
				.append("deltaZ", deltaZ)
				.append("rotation", rotation)
				.append("pitch", pitch)
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
		final EntityRelativePositionRotationMessage other = (EntityRelativePositionRotationMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.deltaX, other.deltaX)
				.append(this.deltaY, other.deltaY)
				.append(this.deltaZ, other.deltaZ)
				.append(this.rotation, other.rotation)
				.append(this.pitch, other.pitch)
				.isEquals();
	}
}
