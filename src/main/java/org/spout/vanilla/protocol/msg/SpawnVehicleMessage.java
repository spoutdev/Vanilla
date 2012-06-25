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
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class SpawnVehicleMessage extends Message {
	private final int id, type, x, y, z, fireballId, fireballX, fireballY, fireballZ;

	public SpawnVehicleMessage(int id, int type, Vector3 pos) {
		this(id, type, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}

	public SpawnVehicleMessage(int id, int type, int x, int y, int z) {
		this(id, type, x, y, z, 0, 0, 0, 0);
	}

	public SpawnVehicleMessage(int id, int type, Vector3 pos, int fbId, Vector3 fbPos) {
		this(id, type, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), fbId, (int) fbPos.getX(), (int) fbPos.getY(), (int) fbPos.getZ());
	}

	public SpawnVehicleMessage(int id, int type, int x, int y, int z, int fbId, int fbX, int fbY, int fbZ) {
		this.id = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		fireballId = fbId;
		fireballX = fbX;
		fireballY = fbY;
		fireballZ = fbZ;
	}

	public int getId() {
		return id;
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

	public boolean hasFireball() {
		return fireballId != 0;
	}

	public int getFireballId() {
		return fireballId;
	}

	public int getFireballX() {
		return fireballX;
	}

	public int getFireballY() {
		return fireballY;
	}

	public int getFireballZ() {
		return fireballZ;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
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
		final SpawnVehicleMessage other = (SpawnVehicleMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
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
