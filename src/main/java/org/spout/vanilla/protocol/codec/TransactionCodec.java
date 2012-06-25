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
package org.spout.vanilla.protocol.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.msg.TransactionMessage;

public final class TransactionCodec extends MessageCodec<TransactionMessage> {
	public TransactionCodec() {
		super(TransactionMessage.class, 0x6A);
	}

	@Override
	public TransactionMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		int transaction = buffer.readUnsignedShort();
		boolean accepted = buffer.readUnsignedByte() != 0;
		return new TransactionMessage(id, transaction, accepted);
	}

	@Override
	public ChannelBuffer encode(TransactionMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(4);
		buffer.writeByte(message.getId());
		buffer.writeShort(message.getTransaction());
		buffer.writeByte(message.isAccepted() ? 1 : 0);
		return buffer;
	}
}
