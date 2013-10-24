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

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.Parameter;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class PlayerSpawnMessage extends EntityMessage {
	private final int x, y, z, yaw, pitch, item;
	private final String name;
	private final List<Parameter<?>> parameters;

	public PlayerSpawnMessage(int id, String name, Vector3f position, int yaw, int pitch, int item, List<Parameter<?>> parameters) {
		this(id, name, (int) position.getX(), (int) position.getY(), (int) position.getZ(), yaw, pitch, item, parameters);
	}

	public PlayerSpawnMessage(int id, String name, int x, int y, int z, int yaw, int pitch, int item, List<Parameter<?>> parameters) {
		super(id);
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.item = item;
		this.parameters = parameters;
	}

	public String getName() {
		return name;
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

	public int getId() {
		return item;
	}

	public List<Parameter<?>> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("yaw", yaw)
				.append("pitch", pitch)
				.append("item", item)
				.append("name", name)
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
		final PlayerSpawnMessage other = (PlayerSpawnMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.yaw, other.yaw)
				.append(this.pitch, other.pitch)
				.append(this.item, other.item)
				.append(this.name, other.name)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getEntityId())
				.append(this.x)
				.append(this.y)
				.append(this.z)
				.append(this.yaw)
				.append(this.item)
				.append(this.name)
				.toHashCode();
	}
}
