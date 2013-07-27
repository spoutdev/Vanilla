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
package org.spout.vanilla.protocol.codec.auth;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.msg.auth.EncryptionKeyResponseMessage;

public class EncryptionKeyResponseCodec extends MessageCodec<EncryptionKeyResponseMessage> {
	public EncryptionKeyResponseCodec() {
		super(EncryptionKeyResponseMessage.class, 0xFC);
	}

	@Override
	public EncryptionKeyResponseMessage decode(ChannelBuffer buffer) {
		int length = buffer.readShort() & 0xFFFF;
		byte[] secret = new byte[length];
		buffer.readBytes(secret);
		int validateTokenLength = buffer.readShort() & 0xFFFF;
		byte[] validateToken = new byte[validateTokenLength];
		buffer.readBytes(validateToken);
		return new EncryptionKeyResponseMessage(false, secret, validateToken);
	}

	@Override
	public ChannelBuffer encode(EncryptionKeyResponseMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeShort((short) message.getSecretArray().length);
		buffer.writeBytes(message.getSecretArray());
		buffer.writeShort((short) message.getVerifyTokenArray().length);
		buffer.writeBytes(message.getVerifyTokenArray());
		return buffer;
	}
}
