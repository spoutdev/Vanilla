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
package org.spout.vanilla.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.chat.ChatArguments;
import org.spout.api.command.Command;
import org.spout.api.exception.UnknownPacketException;
import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.Protocol;
import org.spout.api.protocol.Session;
import org.spout.api.util.Named;

import org.spout.vanilla.chat.VanillaStyleHandler;
import org.spout.vanilla.protocol.msg.ServerPluginMessage;
import org.spout.vanilla.protocol.msg.player.PlayerChatMessage;
import org.spout.vanilla.protocol.msg.player.conn.PlayerKickMessage;
import org.spout.vanilla.protocol.plugin.RegisterPluginChannelCodec;
import org.spout.vanilla.protocol.plugin.RegisterPluginChannelMessage;
import org.spout.vanilla.protocol.plugin.RegisterPluginChannelMessageHandler;
import org.spout.vanilla.protocol.plugin.UnregisterPluginChannelCodec;
import org.spout.vanilla.protocol.plugin.UnregisterPluginChannelMessageHandler;

public class VanillaProtocol extends Protocol {
	public final static DefaultedKey<String> SESSION_ID = new DefaultedKeyImpl<String>("sessionid", "0000000000000000");
	public final static DefaultedKey<String> HANDSHAKE_USERNAME = new DefaultedKeyImpl<String>("handshake_username", "");
	public final static DefaultedKey<Long> LOGIN_TIME = new DefaultedKeyImpl<Long>("handshake_time", -1L);
	public static final DefaultedKey<ArrayList<String>> REGISTERED_CUSTOM_PACKETS = new DefaultedKey<ArrayList<String>>() {
		private final List<String> defaultRestricted = Arrays.asList("REGISTER", "UNREGISTER");

		public ArrayList<String> getDefaultValue() {
			return new ArrayList<String>(defaultRestricted);
		}

		public String getKeyString() {
			return "registeredPluginChannels";
		}
	};
	public static final int DEFAULT_PORT = 25565;

	public VanillaProtocol() {
		super("Vanilla", DEFAULT_PORT, new VanillaCodecLookupService(), new VanillaHandlerLookupService());
		/* PacketFA wrapped packets */
		registerPacket(RegisterPluginChannelCodec.class, new RegisterPluginChannelMessageHandler());
		registerPacket(UnregisterPluginChannelCodec.class, new UnregisterPluginChannelMessageHandler());
	}

	@Override
	public Message getCommandMessage(Command command, ChatArguments args) {
		if (command.getPreferredName().equals("kick")) {
			return getKickMessage(args);
		} else if (command.getPreferredName().equals("say")) {
			return new PlayerChatMessage(args.asString(VanillaStyleHandler.ID) + "\u00a7r"); // The reset text is a workaround for a change in 1.3 -- Remove if fixed
		} else {
			return new PlayerChatMessage('/' + command.getPreferredName() + ' ' + args.asString(VanillaStyleHandler.ID));
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Message> Message getWrappedMessage(boolean upstream, T dynamicMessage) throws IOException {
		MessageCodec<T> codec = (MessageCodec<T>) getCodecLookupService().find(dynamicMessage.getClass());
		ChannelBuffer buffer = codec.encode(upstream, dynamicMessage);

		return new ServerPluginMessage(getName(codec), buffer.array());
	}

	@Override
	public MessageCodec<?> readHeader(ChannelBuffer buf) throws UnknownPacketException {
		int opcode = buf.readUnsignedByte();
		MessageCodec<?> codec = getCodecLookupService().find(opcode);
		if (codec == null) {
			throw new UnknownPacketException(opcode);
		}
		return codec;
	}

	@Override
	public ChannelBuffer writeHeader(MessageCodec<?> codec, ChannelBuffer data) {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		buffer.writeByte(codec.getOpcode());
		return buffer;
	}

	@Override
	public Message getKickMessage(ChatArguments message) {
		return new PlayerKickMessage(message.asString(VanillaStyleHandler.ID));
	}

	@Override
	public Message getIntroductionMessage(String playerName) {
		//return new PlayerHandshakeMessage(VanillaPlugin.MINECRAFT_PROTOCOL_ID, playerName); //TODO Fix this Raphfrk

		return null;
	}

	public static MessageCodec<?> getCodec(String name, Protocol activeProtocol) {
		for (Pair<Integer, String> item : activeProtocol.getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = activeProtocol.getCodecLookupService().find(item.getLeft());
			if (getName(codec).equalsIgnoreCase(name)) {
				return codec;
			}
		}
		return null;
	}

	public static String getName(MessageCodec<?> codec) {
		if (codec instanceof Named) {
			return ((Named) codec).getName();
		} else {
			return "SPOUT:" + codec.getOpcode();
		}
	}

	@Override
	public void initializeSession(Session session) {
		session.setNetworkSynchronizer(new VanillaNetworkSynchronizer(session));

		List<MessageCodec<?>> dynamicCodecList = new ArrayList<MessageCodec<?>>();
		for (Pair<Integer, String> item : getDynamicallyRegisteredPackets()) {
			MessageCodec<?> codec = getCodecLookupService().find(item.getLeft());
			if (codec != null) {
				dynamicCodecList.add(codec);
			} else {
				throw new IllegalStateException("Dynamic packet class" + item.getRight() + " claims to be registered but is not in our CodecLookupService!");
			}
		}

		session.send(false, new RegisterPluginChannelMessage(dynamicCodecList));
	}
}
