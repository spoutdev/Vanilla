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
package org.spout.vanilla.protocol.codec.auth;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.VanillaChannelBufferUtils;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyRequestMessage;

public final class EncryptionKeyRequestCodec extends MessageCodec<EncryptionKeyRequestMessage> {
	public EncryptionKeyRequestCodec() {
		super(EncryptionKeyRequestMessage.class, 0xFD);
	}

	@Override
	public EncryptionKeyRequestMessage decode(ChannelBuffer buffer) {
		String sessionId = VanillaChannelBufferUtils.readString(buffer);
		int length = buffer.readShort() & 0xFFFF;
		byte[] publicKey = new byte[length];
		buffer.readBytes(publicKey);
		int tokenLength = buffer.readShort() & 0xFFFF;
		byte[] token = new byte[tokenLength];
		buffer.readBytes(token);
		return new EncryptionKeyRequestMessage(sessionId, false, publicKey, token);
	}

	@Override
	public ChannelBuffer encode(EncryptionKeyRequestMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		VanillaChannelBufferUtils.writeString(buffer, message.getSessionId());
		byte[] publicKey = message.getSecretArray();
		buffer.writeShort((short) publicKey.length);
		buffer.writeBytes(publicKey);
		buffer.writeShort((short) message.getVerifyTokenArray().length);
		buffer.writeBytes(message.getVerifyTokenArray());
		return buffer;
	}
}
