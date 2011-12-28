package org.getspout.vanilla.protocol;

import org.getspout.api.protocol.Message;
import org.getspout.api.protocol.MessageHandler;
import org.getspout.vanilla.protocol.handler.VanillaHandshakeMessageHandler;
import org.getspout.vanilla.protocol.handler.VanillaIdentificationMessageHandler;
import org.getspout.vanilla.protocol.handler.VanillaPingMessageHandler;
import org.getspout.vanilla.protocol.msg.VanillaHandshakeMessage;
import org.getspout.vanilla.protocol.msg.VanillaIdentificationMessage;
import org.getspout.vanilla.protocol.msg.VanillaPingMessage;

public class VanillaHandlerLookupService extends org.getspout.api.protocol.HandlerLookupService {

		public VanillaHandlerLookupService() {
			super();
		}
		
		static {
			try {
				bind(VanillaIdentificationMessage.class, VanillaIdentificationMessageHandler.class);
				bind(VanillaHandshakeMessage.class, VanillaHandshakeMessageHandler.class);
				bind(VanillaPingMessage.class, VanillaPingMessageHandler.class);
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
