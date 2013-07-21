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
package org.spout.vanilla.protocol.codec.player.conn;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.VanillaChannelBufferUtils;
import org.spout.vanilla.protocol.msg.player.conn.PlayerLoginRequestMessage;

public final class PlayerLoginRequestCodec extends MessageCodec<PlayerLoginRequestMessage> {
	public PlayerLoginRequestCodec() {
		super(PlayerLoginRequestMessage.class, 0x01);
	}

	@Override
	public PlayerLoginRequestMessage decodeFromServer(ChannelBuffer buffer) {
		int id = buffer.readInt();
		String worldType = VanillaChannelBufferUtils.readString(buffer);
		byte mode = buffer.readByte();
		byte dimension = buffer.readByte();
		byte difficulty = buffer.readByte();
		buffer.readUnsignedByte(); //not used?
		short maxPlayers = buffer.readUnsignedByte();
		return new PlayerLoginRequestMessage(id, worldType, mode, dimension, difficulty, maxPlayers);
	}

	/* This is needed for tests to complete. It is not actually used.
	 * See the commented-out code below this function */
	@Override
	public PlayerLoginRequestMessage decodeFromClient(ChannelBuffer buffer) {
		int id = buffer.readInt();
		String worldType = VanillaChannelBufferUtils.readString(buffer);
		byte mode = buffer.readByte();
		byte dimension = buffer.readByte();
		byte difficulty = buffer.readByte();
		buffer.readUnsignedByte(); //not used?
		short maxPlayers = buffer.readUnsignedByte();
		return new PlayerLoginRequestMessage(id, worldType, mode, dimension, difficulty, maxPlayers);
	}

	@Override
	public ChannelBuffer encodeToClient(PlayerLoginRequestMessage message) {
		PlayerLoginRequestMessage server = message;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(server.getId());
		VanillaChannelBufferUtils.writeString(buffer, server.getWorldType());
		buffer.writeByte(server.getGameMode());
		buffer.writeByte(server.getDimension());
		buffer.writeByte(server.getDifficulty());
		buffer.writeByte(0);
		buffer.writeByte(server.getMaxPlayers());
		return buffer;
	}

	@Override
	public ChannelBuffer encodeToServer(PlayerLoginRequestMessage message) {
		PlayerLoginRequestMessage server = message;
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(server.getId());
		VanillaChannelBufferUtils.writeString(buffer, server.getWorldType());
		buffer.writeByte(server.getGameMode());
		buffer.writeByte(server.getDimension());
		buffer.writeByte(server.getDifficulty());
		buffer.writeByte(0);
		buffer.writeByte(server.getMaxPlayers());
		return buffer;
	}
}
