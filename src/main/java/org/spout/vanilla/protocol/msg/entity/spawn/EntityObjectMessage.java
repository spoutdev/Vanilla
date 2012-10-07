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
package org.spout.vanilla.protocol.msg.entity.spawn;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.entity.Entity;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.component.substance.object.ObjectEntity;
import org.spout.vanilla.component.substance.object.projectile.Projectile;
import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class EntityObjectMessage extends EntityMessage {
	private final byte type;
	private final int x, y, z, throwerId;
	private final short speedX, speedY, speedZ;

	public EntityObjectMessage(Entity entity, byte type, int throwerId) {
		super(entity);
		this.type = type;
		this.throwerId = throwerId;
		Point pos = entity.getTransform().getPosition();

		double p = 32d;
		x = (int) Math.floor(pos.getX() * p);
		y = (int) Math.floor(pos.getY() * p);
		z = (int) Math.floor(pos.getZ() * p);

		double v = 3.9d;
		Vector3 factor = new Vector3(v, v, v);
		Vector3 velocity = entity.get(ObjectEntity.class).getVelocity();
		velocity = velocity.max(factor.multiply(-1)).min(factor);

		double s = 8000d;
		speedX = (short)(velocity.getX() * s);
		speedY = (short)(velocity.getY() * s);
		speedZ = (short)(velocity.getZ() * s);
	}

	public EntityObjectMessage(Entity entity, byte type) {
		this(entity, type, 0);
	}

	public EntityObjectMessage(int entityId, byte type, int x, int y, int z, int throwerId, short speedX, short speedY, short speedZ) {
		super(entityId);
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.throwerId = throwerId;
		this.speedX = speedX;
		this.speedY = speedY;
		this.speedZ = speedZ;
	}

	public EntityObjectMessage(int entityId, byte type, int x, int y, int z, int throwerId) {
		this(entityId, type, x, y, z, throwerId, (short) 0, (short) 0, (short) 0);
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
				.append(this.speedZ, speedZ)
				.isEquals();
	}
}
