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
package org.spout.vanilla.protocol.codec.entity.spawn;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.entity.spawn.EntityPaintingMessage;

public final class EntityPaintingCodec extends MessageCodec<EntityPaintingMessage> {
	public EntityPaintingCodec() {
		super(EntityPaintingMessage.class, 0x19);
	}

	@Override
	public EntityPaintingMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		String title = ChannelBufferUtils.readString(buffer);
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int direction = buffer.readInt();
		return new EntityPaintingMessage(id, title, x, y, z, direction);
	}

	@Override
	public ChannelBuffer encode(EntityPaintingMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getEntityId());
		ChannelBufferUtils.writeString(buffer, message.getTitle());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeInt(message.getDirection());
		return buffer;
	}
}
