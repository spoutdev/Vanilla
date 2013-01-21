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
package org.spout.vanilla.plugin.protocol.handler;

import java.io.IOException;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.vanilla.plugin.protocol.VanillaProtocol;
import org.spout.vanilla.plugin.protocol.msg.ServerPluginMessage;

public class ServerPluginHandler extends MessageHandler<ServerPluginMessage> {
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void handle(boolean upstream, Session session, ServerPluginMessage message) {
		if (session.getDataMap().get(VanillaProtocol.REGISTERED_CUSTOM_PACKETS).contains(message.getType())) {
			final VanillaProtocol protocol = (VanillaProtocol) session.getProtocol();
			
			Message unwrapped = null;
			for (MessageCodec<?> codec : protocol.getCodecLookupService().getCodecs()) {
				if (VanillaProtocol.getName(codec).equals(message.getType())) {
					try {
						unwrapped = message.unwrap(upstream, protocol);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			
			if (unwrapped == null) {
				return;
			}
			
			MessageHandler handler = protocol.getHandlerLookupService().find(unwrapped.getClass());
			handler.handle(upstream, session, unwrapped);
		}
	}
}
