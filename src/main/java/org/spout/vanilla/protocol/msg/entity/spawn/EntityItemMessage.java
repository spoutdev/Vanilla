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

public final class EntityItemMessage extends EntityMessage {
	private final int x, y, z, rotation, pitch, roll;
	private final int itemId, count;
	private final short damage;

	public EntityItemMessage(int id, int itemId, int count, short damage, Vector3 pos, int rotation, int pitch, int roll) {
		this(id, itemId, count, damage, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), rotation, pitch, roll);
	}

	public EntityItemMessage(int id, int itemId, int count, short damage, int x, int y, int z, int rotation, int pitch, int roll) {
		super(id);
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
		this.pitch = pitch;
		this.roll = roll;
		this.itemId = itemId;
		this.count = count;
		this.damage = damage;
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

	public int getRotation() {
		return rotation;
	}

	public int getPitch() {
		return pitch;
	}

	public int getRoll() {
		return roll;
	}

	public int getId() {
		return itemId;
	}

	public int getCount() {
		return count;
	}

	public short getDamage() {
		return damage;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("rotation", rotation)
				.append("pitch", pitch)
				.append("roll", roll)
				.append("itemId", itemId)
				.append("count", count)
				.append("damage", damage)
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
		final EntityItemMessage other = (EntityItemMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.rotation, other.rotation)
				.append(this.pitch, other.pitch)
				.append(this.roll, other.roll)
				.append(this.itemId, other.itemId)
				.append(this.count, other.count)
				.append(this.damage, other.damage)
				.isEquals();
	}
}
