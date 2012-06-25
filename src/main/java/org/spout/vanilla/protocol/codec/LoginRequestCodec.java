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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;

public final class LoginRequestCodec extends MessageCodec<LoginRequestMessage> {
	public LoginRequestCodec() {
		super(LoginRequestMessage.class, 0x01);
	}

	@Override
	public LoginRequestMessage decode(ChannelBuffer buffer) {
		int version = buffer.readInt();
		String name = ChannelBufferUtils.readString(buffer);
		String worldType = ChannelBufferUtils.readString(buffer);
		int mode = buffer.readInt();
		int dimension = buffer.readInt();
		int difficulty = buffer.readByte();
		int worldHeight = ChannelBufferUtils.getExpandedHeight(buffer.readUnsignedByte());
		int maxPlayers = buffer.readUnsignedByte();
		return new LoginRequestMessage(version, name, mode, dimension, difficulty, worldHeight, maxPlayers, worldType);
	}

	@Override
	public ChannelBuffer encode(LoginRequestMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getId());
		ChannelBufferUtils.writeString(buffer, message.getName());
		ChannelBufferUtils.writeString(buffer, message.getWorldType());
		buffer.writeInt(message.getGameMode());
		buffer.writeInt(message.getDimension());
		buffer.writeByte(message.getDifficulty());
		buffer.writeByte(ChannelBufferUtils.getShifts(message.getWorldHeight()) - 1);
		buffer.writeByte(message.getMaxPlayers());
		return buffer;
	}
}
