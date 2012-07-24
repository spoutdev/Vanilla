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

import org.spout.api.Spout;
import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.login.request.ClientLoginRequestMessage;
import org.spout.vanilla.protocol.msg.login.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.login.request.ServerLoginRequestMessage;

public final class LoginRequestCodec extends MessageCodec<LoginRequestMessage> {
	public LoginRequestCodec() {
		super(LoginRequestMessage.class, 0x01);
	}

	@Override
	public LoginRequestMessage decodeFromServer(ChannelBuffer buffer) {
		int id = buffer.readInt();
		String worldType = ChannelBufferUtils.readString(buffer);
		byte mode = buffer.readByte();
		byte dimension = buffer.readByte();
		byte difficulty = buffer.readByte();
		short notUsed = buffer.readUnsignedByte();
		short maxPlayers = buffer.readUnsignedByte();
		return new ServerLoginRequestMessage(id, worldType, mode, dimension, difficulty, maxPlayers);
	}

	@Override
	public LoginRequestMessage decodeFromClient(ChannelBuffer buffer) {
		Spout.log("Hey I am a client and I want to connect!");
		return new ClientLoginRequestMessage();
	}

	@Override
	public ChannelBuffer encodeToClient(LoginRequestMessage message) {
		ServerLoginRequestMessage server = (ServerLoginRequestMessage) message;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(server.getId());
		ChannelBufferUtils.writeString(buffer, server.getWorldType());
		buffer.writeByte(server.getGameMode());
		buffer.writeByte(server.getDimension());
		buffer.writeByte(server.getDifficulty());
		buffer.writeByte(0);
		buffer.writeByte(server.getMaxPlayers());
		return buffer;
	}

	@Override
	public ChannelBuffer encodeToServer(LoginRequestMessage message) {
		return ChannelBuffers.dynamicBuffer();
	}
}
