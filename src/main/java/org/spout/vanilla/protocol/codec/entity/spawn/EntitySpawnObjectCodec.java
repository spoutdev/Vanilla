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

import org.spout.vanilla.protocol.msg.entity.spawn.EntityObjectMessage;

public final class EntitySpawnObjectCodec extends MessageCodec<EntityObjectMessage> {
	public EntitySpawnObjectCodec() {
		super(EntityObjectMessage.class, 0x17);
	}

	@Override
	public EntityObjectMessage decode(ChannelBuffer buffer) throws IOException {
		int entityId = buffer.readInt();
		byte type = buffer.readByte();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int throwerId = buffer.readInt();
		if (throwerId > 0) {
			short speedX = buffer.readShort();
			short speedY = buffer.readShort();
			short speedZ = buffer.readShort();
			return new EntityObjectMessage(entityId, type, x, y, z, throwerId, speedX, speedY, speedZ);
		}
		return new EntityObjectMessage(entityId, type, x, y, z, throwerId);
	}

	@Override
	public ChannelBuffer encode(EntityObjectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(message.getThrowerId() > 0 ? 27 : 21);
		System.out.println(message);
		buffer.writeInt(message.getEntityId());
		buffer.writeByte(message.getType());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		int throwerId = message.getThrowerId();
		buffer.writeInt(throwerId);
		if (throwerId > 0) {
			buffer.writeShort(message.getSpeedX());
			buffer.writeShort(message.getSpeedY());
			buffer.writeShort(message.getSpeedZ());
		}
		return buffer;
	}
}
