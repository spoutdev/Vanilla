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
package org.spout.vanilla.protocol.msg.entity.spawn;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.geo.discrete.Transform;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.vector.Vector3;
import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class EntityObjectMessage extends EntityMessage {
	private final byte type;
	private final int x, y, z, throwerId;
	private final short speedX, speedY, speedZ;
	private final byte pitch, yaw;

	public EntityObjectMessage(Entity entity, byte type, int throwerId, RepositionManager rm) {
		super(entity);
		this.type = type;
		this.throwerId = throwerId;
		Transform transform = entity.getPhysics().getTransform();
		Point pos = transform.getPosition();

		double p = 32d;
		x = rm.convertX((int) Math.floor(pos.getX() * p));
		y = rm.convertY((int) Math.floor(pos.getY() * p));
		z = rm.convertZ((int) Math.floor(pos.getZ() * p));

		double v = 3.9d;
		Vector3 factor = new Vector3(v, v, v);

		Vector3 velocity;
		if (entity.getPhysics().isActivated()) {
			velocity = entity.getPhysics().getMovementVelocity(); //TODO: Check if it's alright.
		} else {
			velocity = Vector3.ZERO;
		}

		velocity = velocity.max(factor.mul(-1)).min(factor);

		double s = 8000d;
		speedX = (short) (velocity.getX() * s);
		speedY = (short) (velocity.getY() * s);
		speedZ = (short) (velocity.getZ() * s);

		final Vector3 axesAngles = transform.getRotation().getAxesAngleDeg();
		pitch = (byte) VanillaByteBufUtils.protocolifyPitch(axesAngles.getX());
		yaw = (byte) VanillaByteBufUtils.protocolifyYaw(axesAngles.getY());
	}

	public EntityObjectMessage(Entity entity, byte type, RepositionManager rm) {
		this(entity, type, 0, rm);
	}

	public EntityObjectMessage(int entityId, byte type, int x, int y, int z, int throwerId, short speedX, short speedY, short speedZ, byte yaw, byte pitch, RepositionManager rm) {
		super(entityId);
		this.type = type;
		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
		this.throwerId = throwerId;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public EntityObjectMessage(int entityId, byte type, int x, int y, int z, int throwerId, byte yaw, byte pitch, RepositionManager rm) {
		this(entityId, type, x, y, z, throwerId, (short) 0, (short) 0, (short) 0, pitch, yaw, rm);
	}

	public byte getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getThrowerId() {
		return throwerId;
	}

	public short getSpeedX() {
		return speedX;
	}

	public short getSpeedY() {
		return speedY;
	}

	public short getSpeedZ() {
		return speedZ;
	}

	public byte getPitch() {
		return pitch;
	}

	public byte getYaw() {
		return yaw;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("type", type)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("speedX", speedX)
				.append("speedY", speedY)
				.append("speedZ", speedZ)
				.append("pitch", pitch)
				.append("yaw", yaw)
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
		final EntityObjectMessage other = (EntityObjectMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.type, other.type)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.speedX, other.speedX)
				.append(this.speedY, other.speedY)
				.append(this.speedZ, other.speedZ)
				.append(this.yaw, other.yaw)
				.append(this.pitch, other.pitch)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getEntityId())
				.append(this.type)
				.append(this.x)
				.append(this.y)
				.append(this.z)
				.append(this.speedX)
				.append(this.speedY)
				.append(this.speedZ)
				.append(this.yaw)
				.append(this.pitch)
				.toHashCode();
	}
}
