package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.Message;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.vanilla.protocol.handler.HandshakeMessageHandler;
import org.getspout.vanilla.protocol.handler.IdentificationMessageHandler;
import org.getspout.vanilla.protocol.handler.PingMessageHandler;
import org.getspout.vanilla.protocol.msg.HandshakeMessage;
import org.getspout.vanilla.protocol.msg.IdentificationMessage;
import org.getspout.vanilla.protocol.msg.PingMessage;

public class VanillaHandlerLookupService extends org.getspout.api.protocol.HandlerLookupService {

		public VanillaHandlerLookupService() {
			super();
		}
		
		static {
			try {
				bind(IdentificationMessage.class, IdentificationMessageHandler.class);
				bind(HandshakeMessage.class, HandshakeMessageHandler.class);
				bind(PingMessage.class, PingMessageHandler.class);
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
