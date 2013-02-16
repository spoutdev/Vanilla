/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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

import java.util.List;
import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.math.Vector3;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.Parameter;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class EntityMobMessage extends EntityMessage {
	private final int type, x, y, z, yaw, pitch, headYaw;
	private final short velocityZ, velocityX, velocityY;
	private final List<Parameter<?>> parameters;

	public EntityMobMessage(int id, int type, Vector3 pos, int yaw, int pitch, int headYaw, short velocityZ, short velocityX, short velocityY, List<Parameter<?>> parameters, RepositionManager rm) {
		this(id, type, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), yaw, pitch, headYaw, velocityZ, velocityX, velocityY, parameters, rm);
	}

	public EntityMobMessage(int id, int type, int x, int y, int z, int yaw, int pitch, int headYaw, short velocityZ, short velocityX, short velocityY, List<Parameter<?>> parameters, RepositionManager rm) {
		super(id);
		this.type = type;
		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
		this.yaw = yaw;
		this.pitch = pitch;
		this.headYaw = headYaw;
		this.velocityZ = velocityZ;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.parameters = parameters;
	}

	public int getType() {
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

	public int getYaw() {
		return yaw;
	}

	public int getPitch() {
		return pitch;
	}

	public int getHeadYaw() {
		return headYaw;
	}

	public short getVelocityZ() {
		return velocityZ;
	}

	public short getVelocityX() {
		return velocityX;
	}

	public short getVelocityY() {
		return velocityY;
	}

	public List<Parameter<?>> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("type", type)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("yaw", yaw)
				.append("pitch", pitch)
				.append("headYaw", headYaw)
				.append("velocityz", velocityZ)
				.append("velocityx", velocityX)
				.append("velocityY", velocityY)
				.append("parameters", parameters)
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
		final EntityMobMessage other = (EntityMobMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.type, other.type)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.yaw, other.yaw)
				.append(this.pitch, other.pitch)
				.append(this.headYaw, other.headYaw)
				.append(this.velocityZ, other.velocityZ)
				.append(this.velocityX, other.velocityX)
				.append(this.velocityY, other.velocityY)
				.append(this.parameters, other.parameters)
				.isEquals();
	}
}
