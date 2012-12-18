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
package org.spout.vanilla.protocol.codec.entity.pos;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.msg.entity.pos.EntityVelocityMessage;

public final class EntityVelocityCodec extends MessageCodec<EntityVelocityMessage> {
	public EntityVelocityCodec() {
		super(EntityVelocityMessage.class, 0x1C);
	}

	@Override
	public EntityVelocityMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int vx = buffer.readUnsignedShort();
		int vy = buffer.readUnsignedShort();
		int vz = buffer.readUnsignedShort();
		return new EntityVelocityMessage(id, vx, vy, vz);
	}

	@Override
	public ChannelBuffer encode(EntityVelocityMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(10);
		buffer.writeInt(message.getEntityId());
		buffer.writeShort(message.getVelocityX());
		buffer.writeShort(message.getVelocityY());
		buffer.writeShort(message.getVelocityZ());
		return buffer;
	}
}
