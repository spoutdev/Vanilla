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
import org.spout.api.protocol.proxy.ConnectionInfo;
import org.spout.api.protocol.proxy.TransformableMessage;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.proxy.VanillaConnectionInfo;

public final class SpawnLightningStrikeMessage extends Message implements TransformableMessage {
	private int id;
	private final int mode, x, y, z;

	public SpawnLightningStrikeMessage(int id, int x, int y, int z) {
		this(id, 1, x, y, z);
	}

	public SpawnLightningStrikeMessage(int id, int mode, int x, int y, int z) {
		this.id = id;
		this.mode = mode;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Message transform(boolean upstream, int connects, ConnectionInfo info, ConnectionInfo auxChannelInfo) {
		if (id == ((VanillaConnectionInfo) info).getEntityId()) {
			id = ((VanillaConnectionInfo) auxChannelInfo).getEntityId();
		} else if (id == ((VanillaConnectionInfo) auxChannelInfo).getEntityId()) {
			id = ((VanillaConnectionInfo) info).getEntityId();
		}
		return this;
	}

	public int getId() {
		return id;
	}

	public int getMode() {
		return mode;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("mode", mode)
				.append("x", x)
				.append("y", y)
				.append("z", z)
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
		final SpawnLightningStrikeMessage other = (SpawnLightningStrikeMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.mode, other.mode)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.isEquals();
	}
}
