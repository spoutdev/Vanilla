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
package org.spout.vanilla.protocol.msg.entity;

import java.util.Arrays;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.proxy.ConnectionInfo;
import org.spout.api.protocol.proxy.TransformableMessage;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

import org.spout.vanilla.protocol.proxy.VanillaConnectionInfo;

public final class EntityDestroyMessage extends VanillaMainChannelMessage implements TransformableMessage {
	private int[] id;

	public EntityDestroyMessage(int[] id) {
		this.id = id;
	}

	@Override
	public Message transform(boolean upstream, int connects, ConnectionInfo info, ConnectionInfo auxChannelInfo) {
		for (int i = 0; i < id.length; i++) {
			if (id[i] == ((VanillaConnectionInfo) info).getEntityId()) {
				id[i] = ((VanillaConnectionInfo) auxChannelInfo).getEntityId();
			} else if (id[i] == ((VanillaConnectionInfo) auxChannelInfo).getEntityId()) {
				id[i] = ((VanillaConnectionInfo) info).getEntityId();
			}
		}

		return this;
	}

	public int[] getId() {
		return id;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", Arrays.toString(id))
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
		final EntityDestroyMessage other = (EntityDestroyMessage) obj;
		return Arrays.equals(this.id, other.id);
	}
}
