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

import java.util.HashMap;
import java.util.Map;

import org.spout.api.protocol.HandlerLookupService;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.common.handler.CustomDataMessageHandler;
import org.spout.api.protocol.common.message.CustomDataMessage;

import org.spout.vanilla.protocol.handler.bootstrap.BootstrapEncryptionKeyResponseMessageHandler;
import org.spout.vanilla.protocol.handler.bootstrap.BootstrapHandshakeMessageHandler;
import org.spout.vanilla.protocol.handler.bootstrap.BootstrapLoginRequestMessageHandler;
import org.spout.vanilla.protocol.handler.bootstrap.BootstrapPingMessageHandler;
import org.spout.vanilla.protocol.handler.AnimationMessageHandler;
import org.spout.vanilla.protocol.handler.ChatMessageHandler;
import org.spout.vanilla.protocol.handler.ClientStatusHandler;
import org.spout.vanilla.protocol.handler.CreativeMessageHandler;
import org.spout.vanilla.protocol.handler.EntityActionMessageHandler;
import org.spout.vanilla.protocol.handler.EntityHeadYawMessageHandler;
import org.spout.vanilla.protocol.handler.EntityInteractionMessageHandler;
import org.spout.vanilla.protocol.handler.GroundMessageHandler;
import org.spout.vanilla.protocol.handler.HeldItemChangeMessageHandler;
import org.spout.vanilla.protocol.handler.KeepAliveMessageHandler;
import org.spout.vanilla.protocol.handler.KickMessageHandler;
import org.spout.vanilla.protocol.handler.PlayerAbilityMessageHandler;
import org.spout.vanilla.protocol.handler.PlayerBlockPlacementMessageHandler;
import org.spout.vanilla.protocol.handler.PlayerDiggingMessageHandler;
import org.spout.vanilla.protocol.handler.PlayerLookMessageHandler;
import org.spout.vanilla.protocol.handler.PlayerPositionLookMessageHandler;
import org.spout.vanilla.protocol.handler.PlayerPositionMessageHandler;
import org.spout.vanilla.protocol.handler.TabCompleteMessageHandler;
import org.spout.vanilla.protocol.handler.UpdateSignHandler;
import org.spout.vanilla.protocol.handler.WindowClickMessageHandler;
import org.spout.vanilla.protocol.handler.WindowCloseMessageHandler;
import org.spout.vanilla.protocol.msg.ChatMessage;
import org.spout.vanilla.protocol.msg.ClientStatusMessage;
import org.spout.vanilla.protocol.msg.CreativeMessage;
import org.spout.vanilla.protocol.msg.EncryptionKeyResponseMessage;
import org.spout.vanilla.protocol.msg.GroundMessage;
import org.spout.vanilla.protocol.msg.TabCompleteMessage;
import org.spout.vanilla.protocol.msg.login.HandshakeMessage;
import org.spout.vanilla.protocol.msg.HeldItemChangeMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.login.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.PlayerAbilityMessage;
import org.spout.vanilla.protocol.msg.PlayerBlockPlacementMessage;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.protocol.msg.PlayerLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.UpdateSignMessage;
import org.spout.vanilla.protocol.msg.entity.EntityActionMessage;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.entity.EntityInteractionMessage;
import org.spout.vanilla.protocol.msg.window.WindowClickMessage;
import org.spout.vanilla.protocol.msg.window.WindowCloseMessage;

public class VanillaHandlerLookupService extends HandlerLookupService {
	protected static final Map<Class<? extends Message>, MessageHandler<?>> handlers = new HashMap<Class<? extends Message>, MessageHandler<?>>();

	public VanillaHandlerLookupService() {
		super();
	}

	static {
		try {
			bind(HandshakeMessage.class, BootstrapHandshakeMessageHandler.class);
			bind(LoginRequestMessage.class, BootstrapLoginRequestMessageHandler.class);
			bind(ChatMessage.class, ChatMessageHandler.class);
			bind(GroundMessage.class, GroundMessageHandler.class);
			bind(PlayerPositionMessage.class, PlayerPositionMessageHandler.class);
			bind(PlayerLookMessage.class, PlayerLookMessageHandler.class);
			bind(PlayerPositionLookMessage.class, PlayerPositionLookMessageHandler.class);
			bind(KickMessage.class, KickMessageHandler.class);
			bind(PlayerDiggingMessage.class, PlayerDiggingMessageHandler.class);
			bind(PlayerBlockPlacementMessage.class, PlayerBlockPlacementMessageHandler.class);
			bind(WindowClickMessage.class, WindowClickMessageHandler.class);
			bind(WindowCloseMessage.class, WindowCloseMessageHandler.class);
			bind(HeldItemChangeMessage.class, HeldItemChangeMessageHandler.class);
			bind(EntityActionMessage.class, EntityActionMessageHandler.class);
			bind(EntityAnimationMessage.class, AnimationMessageHandler.class);
			bind(KeepAliveMessage.class, KeepAliveMessageHandler.class);
			bind(CreativeMessage.class, CreativeMessageHandler.class);
			bind(UpdateSignMessage.class, UpdateSignHandler.class);
			bind(EntityInteractionMessage.class, EntityInteractionMessageHandler.class);
			bind(EntityHeadYawMessage.class, EntityHeadYawMessageHandler.class);
			bind(PlayerAbilityMessage.class, PlayerAbilityMessageHandler.class);
			bind(ServerListPingMessage.class, BootstrapPingMessageHandler.class);
			bind(ClientStatusMessage.class, ClientStatusHandler.class);
			bind(CustomDataMessage.class, CustomDataMessageHandler.class);
			bind(EncryptionKeyResponseMessage.class, BootstrapEncryptionKeyResponseMessageHandler.class);
			bind(TabCompleteMessage.class, TabCompleteMessageHandler.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	protected static <T extends Message> void bind(Class<T> clazz, Class<? extends MessageHandler<T>> handlerClass) throws InstantiationException, IllegalAccessException {
		handlers.put(clazz, handlerClass.newInstance());
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Message> MessageHandler<T> find(Class<T> clazz) {
		return (MessageHandler<T>) handlers.get(clazz);
	}
}
