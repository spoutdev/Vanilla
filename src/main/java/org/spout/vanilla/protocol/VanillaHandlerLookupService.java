/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.protocol.HandlerLookupService;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageHandler;

import org.spout.vanilla.protocol.handler.AnimationMessageHandler;
import org.spout.vanilla.protocol.handler.ChatMessageHandler;
import org.spout.vanilla.protocol.handler.CloseWindowMessageHandler;
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
import org.spout.vanilla.protocol.handler.RespawnMessageHandler;
import org.spout.vanilla.protocol.handler.UpdateSignHandler;
import org.spout.vanilla.protocol.handler.WindowClickMessageHandler;
import org.spout.vanilla.protocol.msg.AnimationMessage;
import org.spout.vanilla.protocol.msg.ChatMessage;
import org.spout.vanilla.protocol.msg.CloseWindowMessage;
import org.spout.vanilla.protocol.msg.CreativeMessage;
import org.spout.vanilla.protocol.msg.EntityActionMessage;
import org.spout.vanilla.protocol.msg.EntityHeadYawMessage;
import org.spout.vanilla.protocol.msg.EntityInteractionMessage;
import org.spout.vanilla.protocol.msg.GroundMessage;
import org.spout.vanilla.protocol.msg.HeldItemChangeMessage;
import org.spout.vanilla.protocol.msg.KeepAliveMessage;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.PlayerAbilityMessage;
import org.spout.vanilla.protocol.msg.PlayerBlockPlacementMessage;
import org.spout.vanilla.protocol.msg.PlayerDiggingMessage;
import org.spout.vanilla.protocol.msg.PlayerLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionLookMessage;
import org.spout.vanilla.protocol.msg.PlayerPositionMessage;
import org.spout.vanilla.protocol.msg.RespawnMessage;
import org.spout.vanilla.protocol.msg.UpdateSignMessage;
import org.spout.vanilla.protocol.msg.WindowClickMessage;

public class VanillaHandlerLookupService extends HandlerLookupService {
	public VanillaHandlerLookupService() {
		super();
	}

	static {
		try {
			bind(ChatMessage.class, ChatMessageHandler.class);
			bind(GroundMessage.class, GroundMessageHandler.class);
			bind(PlayerPositionMessage.class, PlayerPositionMessageHandler.class);
			bind(PlayerLookMessage.class, PlayerLookMessageHandler.class);
			bind(PlayerPositionLookMessage.class, PlayerPositionLookMessageHandler.class);
			bind(KickMessage.class, KickMessageHandler.class);
			bind(PlayerDiggingMessage.class, PlayerDiggingMessageHandler.class);
			bind(PlayerBlockPlacementMessage.class, PlayerBlockPlacementMessageHandler.class);
			bind(WindowClickMessage.class, WindowClickMessageHandler.class);
			bind(CloseWindowMessage.class, CloseWindowMessageHandler.class);
			bind(HeldItemChangeMessage.class, HeldItemChangeMessageHandler.class);
			bind(EntityActionMessage.class, EntityActionMessageHandler.class);
			bind(AnimationMessage.class, AnimationMessageHandler.class);
			bind(KeepAliveMessage.class, KeepAliveMessageHandler.class);
			bind(CreativeMessage.class, CreativeMessageHandler.class);
			bind(RespawnMessage.class, RespawnMessageHandler.class);
			bind(UpdateSignMessage.class, UpdateSignHandler.class);
			bind(EntityInteractionMessage.class, EntityInteractionMessageHandler.class);
			bind(EntityHeadYawMessage.class, EntityHeadYawMessageHandler.class);
			bind(PlayerAbilityMessage.class, PlayerAbilityMessageHandler.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	protected static <T extends Message> void bind(Class<T> clazz, Class<? extends MessageHandler<T>> handlerClass) throws InstantiationException, IllegalAccessException {
		MessageHandler<T> handler = handlerClass.newInstance();
		handlers.put(clazz, handler);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends Message> MessageHandler<T> find(Class<T> clazz) {
		return (MessageHandler<T>) handlers.get(clazz);
	}
}
