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
import org.spout.vanilla.protocol.msg.player.conn.PlayerHandshakeMessage;

public final class PlayerHandshakeCodec extends MessageCodec<PlayerHandshakeMessage> {
	public PlayerHandshakeCodec() {
		super(PlayerHandshakeMessage.class, 0x02);
	}

	@Override
	public PlayerHandshakeMessage decode(ChannelBuffer buffer) {
		byte protoVersion = buffer.readByte();
		String username = VanillaChannelBufferUtils.readString(buffer);
		String hostname = VanillaChannelBufferUtils.readString(buffer);
		int port = buffer.readInt();
		return new PlayerHandshakeMessage(protoVersion, username, hostname, port);
	}

	@Override
	public ChannelBuffer encode(PlayerHandshakeMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getProtocolVersion());
		VanillaChannelBufferUtils.writeString(buffer, message.getUsername());
		VanillaChannelBufferUtils.writeString(buffer, message.getHostname());
		buffer.writeInt(message.getPort());
		return buffer;
	}
}
