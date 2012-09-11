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

import org.spout.api.math.Vector3;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class EntityVehicleMessage extends EntityMessage {
	private final int type, fireballId;
	private final double x, y, z, fireballX, fireballY, fireballZ;

	public EntityVehicleMessage(int id, int type, Vector3 pos) {
		this(id, type, pos.getX(), pos.getY(), pos.getZ());
	}

	public EntityVehicleMessage(int id, int type, double x, double y, double z) {
		this(id, type, x, y, z, 0, 0, 0, 0);
	}

	public EntityVehicleMessage(int id, int type, Vector3 pos, int fbId, Vector3 fbPos) {
		this(id, type, pos.getX(), pos.getY(), pos.getZ(), fbId, fbPos.getX(), fbPos.getY(), fbPos.getZ());
	}

	public EntityVehicleMessage(int id, int type, double x, double y, double z, int fbId, double fbX, double fbY, double fbZ) {
		super(id);
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fireballId = fbId;
		this.fireballX = fbX;
		this.fireballY = fbY;
		this.fireballZ = fbZ;
	}

	public int getType() {
		return type;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public boolean hasFireball() {
		return fireballId != 0;
	}

	public int getFireballId() {
		return fireballId;
	}

	public double getFireballX() {
		return fireballX;
	}

	public double getFireballY() {
		return fireballY;
	}

	public double getFireballZ() {
		return fireballZ;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("type", type)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("fireballId", fireballId)
				.append("fireballX", fireballX)
				.append("fireballY", fireballY)
				.append("fireballZ", fireballZ)
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
		final EntityVehicleMessage other = (EntityVehicleMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.type, other.type)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.fireballId, other.fireballId)
				.append(this.fireballX, other.fireballX)
				.append(this.fireballY, other.fireballY)
				.append(this.fireballZ, other.fireballZ)
				.isEquals();
	}
}
