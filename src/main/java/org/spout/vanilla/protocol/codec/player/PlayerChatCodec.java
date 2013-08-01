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
package org.spout.vanilla.protocol.codec.player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.api.util.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.player.PlayerChatMessage;

public final class PlayerChatCodec extends MessageCodec<PlayerChatMessage> {
	private static final JsonParser parser = new JsonParser();
	public PlayerChatCodec() {
		super(PlayerChatMessage.class, 0x03);
	}

	@Override
	public PlayerChatMessage decode(ChannelBuffer buffer) {
		// TODO: Chat support is much more than just 'text'
		String message = ChannelBufferUtils.readString(buffer);
		return new PlayerChatMessage(parser.parse(message).getAsJsonObject().get("text").getAsString());
	}

	@Override
	public ChannelBuffer encode(PlayerChatMessage message) {
		// TODO: Chat support is much more than just 'text'
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		JsonObject json = new JsonObject();
		json.addProperty("text", message.getMessage());
		ChannelBufferUtils.writeString(buffer, json.toString());
		return buffer;
	}
}
