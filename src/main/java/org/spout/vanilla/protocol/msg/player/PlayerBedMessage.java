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
package org.spout.vanilla.protocol.msg.player;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;

import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class PlayerBedMessage extends EntityMessage {
	private final int used, x, y, z;

	public PlayerBedMessage(int id, int used, int x, int y, int z) {
		super(id);
		this.used = used;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public PlayerBedMessage(Entity entity, Block head) {
		this(entity.getId(), 0, head.getX(), head.getY(), head.getZ());
	}

	public int getUsed() {
		return used;
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
		return "UseBedMessage{id=" + this.getEntityId() + ",used=" + used + ",x=" + x + ",y=" + y + ",z=" + z + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerBedMessage other = (PlayerBedMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.used, other.used)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.isEquals();
	}
}
