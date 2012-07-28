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
package org.spout.vanilla.protocol.handler.bootstrap;

import org.spout.api.Spout;
import org.spout.api.event.Event;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.handler.bootstrap.auth.LoginAuth;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;

public class BootstrapLoginRequestMessageHandler extends MessageHandler<LoginRequestMessage> {
	@Override
	public void handleServer(final Session session, final Player player, final LoginRequestMessage message) {
		long start = System.currentTimeMillis();
		Spout.getLogger().info("Handshake response took " + (System.currentTimeMillis() - session.getDataMap().get(VanillaProtocol.LOGIN_TIME)) + " ms");
		if (message.getId() > VanillaPlugin.MINECRAFT_PROTOCOL_ID) {
			session.disconnect(false, new Object[]{"Outdated server!"});
		} else if (message.getId() < VanillaPlugin.MINECRAFT_PROTOCOL_ID) {
			session.disconnect(false, new Object[]{"Outdated client!"});
		} else {
			String handshakeUsername = session.getDataMap().get(VanillaProtocol.HANDSHAKE_USERNAME);
			handshakeUsername = handshakeUsername.split(";")[0];

			if (handshakeUsername.length() == 0 || !handshakeUsername.equals(message.getName())) {
				session.disconnect("Handshake/Login username mismatch");
				return;
			}

			if (VanillaConfiguration.ONLINE_MODE.getBoolean() && !VanillaConfiguration.ENCRYPT_MODE.getBoolean()) {
				final String finalName = message.getName();
				Thread loginAuth = new Thread(new LoginAuth(session, finalName, new PlayerConnectRunnable(session, finalName)), "Login Auth for [" + finalName + "]");
				loginAuth.setDaemon(true);
				loginAuth.start();
			} else {
				playerConnect(session, message.getName());
			}
		}
		System.out.println("Handling login request for " + session + " took " + (System.currentTimeMillis() - start) + " ms");
	}

	public static void playerConnect(Session session, String name) {
		Event event = new PlayerConnectEvent(session, name);
		session.getEngine().getEventManager().callEvent(event);
		if (Spout.getEngine().debugMode()) {
			Spout.getLogger().info("Login took " + (System.currentTimeMillis() - session.getDataMap().get(VanillaProtocol.LOGIN_TIME)) + " ms");
		}
	}

	private static class PlayerConnectRunnable implements Runnable {
		private final Session session;
		private final String name;

		private PlayerConnectRunnable(Session session, String name) {
			this.session = session;
			this.name = name;
		}

		@Override
		public void run() {
			BootstrapLoginRequestMessageHandler.playerConnect(session, name);
		}
	}
}
