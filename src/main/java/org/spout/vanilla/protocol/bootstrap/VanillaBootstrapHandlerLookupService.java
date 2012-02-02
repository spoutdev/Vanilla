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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.bootstrap;

import org.spout.api.protocol.HandlerLookupService;
import org.spout.vanilla.protocol.bootstrap.handler.BootstrapHandshakeMessageHandler;
import org.spout.vanilla.protocol.bootstrap.handler.BootstrapIdentificationMessageHandler;
import org.spout.vanilla.protocol.bootstrap.handler.BootstrapPingMessageHandler;
import org.spout.vanilla.protocol.msg.HandshakeMessage;
import org.spout.vanilla.protocol.msg.IdentificationMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;

/**
 *
 * @author zml2008
 */
public class VanillaBootstrapHandlerLookupService extends HandlerLookupService {
	public VanillaBootstrapHandlerLookupService() {
		try {
		bind(HandshakeMessage.class, BootstrapHandshakeMessageHandler.class);
		bind(IdentificationMessage.class, BootstrapIdentificationMessageHandler.class);
		bind(ServerListPingMessage.class, BootstrapPingMessageHandler.class);
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}
}
