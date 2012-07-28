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

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.nbt.CompoundMap;

public final class PlayerBlockPlacementMessage extends Message {
	private final int id, x, y, z, direction, count, damage;
	private final float dx, dy, dz;
	private CompoundMap nbtData;

	public PlayerBlockPlacementMessage(int x, int y, int z, int direction, float dx, float dy, float dz) {
		this(x, y, z, direction, -1, 0, 0, null, dx, dy, dz);
	}

	public PlayerBlockPlacementMessage(int x, int y, int z, int direction, int id, int count, int damage, CompoundMap nbtData, float dx, float dy, float dz) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.direction = direction;
		this.count = count;
		this.damage = damage;
		this.nbtData = nbtData;
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}

	public int getCount() {
		return count;
	}

	public int getDamage() {
		return damage;
	}

	public int getId() {
		return id;
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

	public int getDirection() {
		return direction;
	}

	public CompoundMap getNbtData() {
		return nbtData;
	}
	
	public float getDX() {
		return dx;
	}
	
	public float getDY() {
		return dy;
	}
	
	public float getDZ() {
		return dz;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("direction", direction)
				.append("count", count)
				.append("damage", damage)
				.append("nbtData", nbtData)
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
		final PlayerBlockPlacementMessage other = (PlayerBlockPlacementMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.direction, other.direction)
				.append(this.count, other.count)
				.append(this.damage, other.damage)
				.append(this.nbtData, other.nbtData)
				.isEquals();
	}
}
