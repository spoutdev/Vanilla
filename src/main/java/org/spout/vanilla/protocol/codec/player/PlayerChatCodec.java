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

import java.io.IOException;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.ByteBufUtils;

import org.spout.vanilla.protocol.VanillaByteBufUtils;
import org.spout.vanilla.protocol.msg.player.PlayerChatMessage;

public final class PlayerChatCodec extends MessageCodec<PlayerChatMessage> {
	private static final JsonParser parser = new JsonParser();

	public PlayerChatCodec() {
		super(PlayerChatMessage.class, 0x03);
	}

	@Override
	public PlayerChatMessage decodeFromClient(ByteBuf buffer) throws IOException {
		// As a server we read messages from the client as plain text
		String message = VanillaByteBufUtils.readString(buffer);
		return new PlayerChatMessage(message);
	}

	@Override
	public PlayerChatMessage decodeFromServer(ByteBuf buffer) throws IOException {
		// As a client we read messages from the server using the JSON parser
		String message = VanillaByteBufUtils.readString(buffer);

		return new PlayerChatMessage(parser.parse(message).getAsJsonObject().get("text").getAsString());
	}

	@Override
	public ByteBuf encodeToClient(PlayerChatMessage message) {
		// As a server we send messages to the client in JSON format
		JsonObject json = new JsonObject();
		json.addProperty("text", message.getMessage());
		return bufferMessage(json.toString());
	}

	@Override
	public ByteBuf encodeToServer(PlayerChatMessage message) {
		// As a client we send messages to the server in plain text format
		return bufferMessage(message.getMessage());
	}

	private ByteBuf bufferMessage(String message) {
		ByteBuf buffer = Unpooled.buffer(VanillaByteBufUtils.getStringLength(message));
		VanillaByteBufUtils.writeString(buffer, message);

		return buffer;
	}
}
