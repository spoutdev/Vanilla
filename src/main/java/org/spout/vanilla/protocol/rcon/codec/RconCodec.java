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
package org.spout.vanilla.protocol.rcon.codec;

import java.nio.ByteOrder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.rcon.msg.RconMessage;

/**
 * Base class for MessageCodecs using rcon
 */
public abstract class RconCodec<T extends RconMessage> extends MessageCodec<T> {
	public RconCodec(Class<T> clazz, int opcode) {
		super(clazz, opcode);
	}

	@Override
	public ByteBuf encode(T message) {
		byte[] bytes = message.getPayload().getBytes(CharsetUtil.US_ASCII);
		ByteBuf buffer = Unpooled.buffer(bytes.length + 2);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.writeBytes(bytes);
		buffer.writeByte(0);
		buffer.writeByte(0);
		return buffer;
	}

	@Override
	public T decode(ByteBuf buffer) {
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		ByteBuf expandingBytes = Unpooled.buffer(buffer.writerIndex());
		byte b;
		while ((b = buffer.readByte()) != 0) {
			expandingBytes.writeByte(b);
		}
		assert buffer.readByte() == 0; // Second null byte

		String value = new String(expandingBytes.array(), CharsetUtil.US_ASCII);
		return createMessage(value);
	}

	public abstract T createMessage(String payload);
}
