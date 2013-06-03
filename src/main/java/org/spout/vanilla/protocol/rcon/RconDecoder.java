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
package org.spout.vanilla.protocol.rcon;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import org.jboss.netty.handler.codec.replay.VoidEnum;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;

/**
 * Decoder for the rcon protocol
 */
public class RconDecoder /*extends ReplayingDecoder<VoidEnum>*/ {
	/*
	private final RemoteConnectionSession session;

	public RconDecoder(RemoteConnectionSession session) {
		this.session = session;
	}

	protected Object decode(ChannelHandlerContext channelHandlerContext, Channel channel, ChannelBuffer buffer, VoidEnum state) throws Exception {
		int length = buffer.readInt();
		if (buffer.readableBytes() < length) {
			return null;
		}

		int requestId = buffer.readInt();
		int opcode = buffer.readInt();
		@SuppressWarnings("unchecked")
		MessageCodec<Message> codec = (MessageCodec<Message>) session.getCore().getCodecLookupService().find(opcode);
		if (codec == null) {
			throw new IOException("Unknown opcode: " + opcode + "!");
		}

		if (session.getRequestId() == -1 && requestId != -1) {
			session.setRequestId(requestId);
		}
		return codec.decode(buffer);
	}*/
}
