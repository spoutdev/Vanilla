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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spout.api.geo.discrete.Point;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;
import org.spout.vanilla.component.substance.Painting;
import org.spout.vanilla.protocol.msg.entity.EntityMessage;

public final class EntityPaintingMessage extends EntityMessage {
	private final int x, y, z, direction;
	private final String title;

	public EntityPaintingMessage(int entityId, String title, int x, int y, int z, int direction, RepositionManager rm) {
		super(entityId);
		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
		this.direction = direction;
		this.title = title;
	}

	public EntityPaintingMessage(Painting painting, RepositionManager rm) {
		super(painting.getOwner());
		Point pos = painting.getOwner().getTransform().getPosition();
		x = rm.convertX(pos.getBlockX());
		y = rm.convertY(pos.getBlockY());
		z = rm.convertZ(pos.getBlockZ());
		direction = painting.getNativeFace();
		title = painting.getType().getName();
		System.out.println(this);
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

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getEntityId())
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("direction", direction)
				.append("title", title)
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
		final EntityPaintingMessage other = (EntityPaintingMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getEntityId(), other.getEntityId())
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.direction, other.direction)
				.append(this.title, other.title)
				.isEquals();
	}
}
