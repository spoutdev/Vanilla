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
package org.spout.vanilla.protocol.codec.player;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.VanillaChannelBufferUtils;
import org.spout.vanilla.protocol.msg.player.PlayerLocaleViewDistanceMessage;

public class PlayerLocaleViewDistanceCodec extends MessageCodec<PlayerLocaleViewDistanceMessage> {
	public PlayerLocaleViewDistanceCodec() {
		super(PlayerLocaleViewDistanceMessage.class, 0xCC);
	}

	@Override
	public PlayerLocaleViewDistanceMessage decode(ChannelBuffer buffer) throws IOException {
		String locale = VanillaChannelBufferUtils.readString(buffer);
		byte viewDistance = buffer.readByte();
		byte chatFlags = buffer.readByte();
		byte difficulty = buffer.readByte();
		boolean showCape = buffer.readByte() != 0;
		return new PlayerLocaleViewDistanceMessage(locale, viewDistance, chatFlags, difficulty, showCape);
	}

	@Override
	public ChannelBuffer encode(PlayerLocaleViewDistanceMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		VanillaChannelBufferUtils.writeString(buffer, message.getLocale());
		buffer.writeByte(message.getViewDistance());
		buffer.writeByte(message.getChatFlags());
		buffer.writeByte(message.getDifficulty());
		buffer.writeByte(message.showsCape() ? 1 : 0);
		return buffer;
	}
}
