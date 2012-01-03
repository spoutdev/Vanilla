/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.HandlerLookupService;
import org.getspout.api.protocol.Message;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.vanilla.protocol.handler.ActivateItemMessageHandler;
import org.getspout.vanilla.protocol.handler.AnimateEntityMessageHandler;
import org.getspout.vanilla.protocol.handler.BlockPlacementMessageHandler;
import org.getspout.vanilla.protocol.handler.ChatMessageHandler;
import org.getspout.vanilla.protocol.handler.CloseWindowMessageHandler;
import org.getspout.vanilla.protocol.handler.DiggingMessageHandler;
import org.getspout.vanilla.protocol.handler.EntityActionMessageHandler;
import org.getspout.vanilla.protocol.handler.GroundMessageHandler;
import org.getspout.vanilla.protocol.handler.HandshakeMessageHandler;
import org.getspout.vanilla.protocol.handler.IdentificationMessageHandler;
import org.getspout.vanilla.protocol.handler.KickMessageHandler;
import org.getspout.vanilla.protocol.handler.PingMessageHandler;
import org.getspout.vanilla.protocol.handler.PositionMessageHandler;
import org.getspout.vanilla.protocol.handler.PositionRotationMessageHandler;
import org.getspout.vanilla.protocol.handler.QuickBarMessageHandler;
import org.getspout.vanilla.protocol.handler.RespawnMessageHandler;
import org.getspout.vanilla.protocol.handler.RotationMessageHandler;
import org.getspout.vanilla.protocol.handler.ServerListPingMessageHandler;
import org.getspout.vanilla.protocol.handler.WindowClickMessageHandler;
import org.getspout.vanilla.protocol.msg.ActivateItemMessage;
import org.getspout.vanilla.protocol.msg.AnimateEntityMessage;
import org.getspout.vanilla.protocol.msg.BlockPlacementMessage;
import org.getspout.vanilla.protocol.msg.ChatMessage;
import org.getspout.vanilla.protocol.msg.CloseWindowMessage;
import org.getspout.vanilla.protocol.msg.DiggingMessage;
import org.getspout.vanilla.protocol.msg.EntityActionMessage;
import org.getspout.vanilla.protocol.msg.GroundMessage;
import org.getspout.vanilla.protocol.msg.HandshakeMessage;
import org.getspout.vanilla.protocol.msg.IdentificationMessage;
import org.getspout.vanilla.protocol.msg.KickMessage;
import org.getspout.vanilla.protocol.msg.PingMessage;
import org.getspout.vanilla.protocol.msg.PositionMessage;
import org.getspout.vanilla.protocol.msg.PositionRotationMessage;
import org.getspout.vanilla.protocol.msg.QuickBarMessage;
import org.getspout.vanilla.protocol.msg.RespawnMessage;
import org.getspout.vanilla.protocol.msg.RotationMessage;
import org.getspout.vanilla.protocol.msg.ServerListPingMessage;
import org.getspout.vanilla.protocol.msg.WindowClickMessage;

public class VanillaHandlerLookupService extends HandlerLookupService {
	public VanillaHandlerLookupService() {
		super();
	}

	static {
		try {
            bind(IdentificationMessage.class, IdentificationMessageHandler.class);
            bind(HandshakeMessage.class, HandshakeMessageHandler.class);
            bind(ChatMessage.class, ChatMessageHandler.class);
            bind(GroundMessage.class, GroundMessageHandler.class);
            bind(PositionMessage.class, PositionMessageHandler.class);
            bind(RotationMessage.class, RotationMessageHandler.class);
            bind(PositionRotationMessage.class, PositionRotationMessageHandler.class);
            bind(KickMessage.class, KickMessageHandler.class);
            bind(DiggingMessage.class, DiggingMessageHandler.class);
            bind(BlockPlacementMessage.class, BlockPlacementMessageHandler.class);
            bind(WindowClickMessage.class, WindowClickMessageHandler.class);
            bind(CloseWindowMessage.class, CloseWindowMessageHandler.class);
            bind(ActivateItemMessage.class, ActivateItemMessageHandler.class);
            bind(EntityActionMessage.class, EntityActionMessageHandler.class);
            bind(AnimateEntityMessage.class, AnimateEntityMessageHandler.class);
            bind(ServerListPingMessage.class, ServerListPingMessageHandler.class);
            bind(PingMessage.class, PingMessageHandler.class);
            bind(QuickBarMessage.class, QuickBarMessageHandler.class);
            bind(RespawnMessage.class, RespawnMessageHandler.class);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	protected static <T extends Message> void bind(Class<T> clazz, Class<? extends MessageHandler<T>> handlerClass) throws InstantiationException, IllegalAccessException {
		MessageHandler<T> handler = handlerClass.newInstance();
		handlers.put(clazz, handler);
	}

	@SuppressWarnings("unchecked")
	public <T extends Message> MessageHandler<T> find(Class<T> clazz) {
		return (MessageHandler<T>) handlers.get(clazz);
	}
}