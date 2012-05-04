/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
import org.spout.vanilla.protocol.handler.*;
import org.spout.vanilla.protocol.msg.*;

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
			bind(EntityAnimationMessage.class, AnimateEntityMessageHandler.class);
			bind(KeepAliveMessage.class, KeepAliveMessageHandler.class);
			bind(CreativeMessage.class, CreativeMessageHandler.class);
			bind(RespawnMessage.class, RespawnMessageHandler.class);
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
