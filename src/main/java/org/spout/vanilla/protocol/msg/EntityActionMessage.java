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

public final class EntityActionMessage extends Message implements TransformableMessage {
	public static final int ACTION_CROUCH = 1;
	public static final int ACTION_UNCROUCH = 2;
	public static final int ACTION_LEAVE_BED = 3;
	public static final int ACTION_START_SPRINTING = 4;
	public static final int ACTION_STOP_SPRINTING = 5;
	private int id;
	private final int action;

	public EntityActionMessage(int id, int action) {
		this.id = id;
		this.action = action;
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

	public int getAction() {
		return action;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("action", action)
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
		final EntityActionMessage other = (EntityActionMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.action, other.action)
				.isEquals();
	}
}
