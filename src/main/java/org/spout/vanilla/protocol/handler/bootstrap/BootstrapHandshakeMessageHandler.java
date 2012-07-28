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

import java.security.SecureRandom;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.security.SecurityHandler;

import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.HandshakeMessage;
import org.spout.vanilla.protocol.msg.LoginRequestMessage;

public class BootstrapHandshakeMessageHandler extends MessageHandler<HandshakeMessage> {
	@Override
	public void handleClient(Session session, Player player, HandshakeMessage message) {
		if (message.getIdentifier().equals("-")) {
			session.send(true, new LoginRequestMessage(player.getName()));
		} else {
			session.disconnect("Online mode not supported for backend servers");
		}
	}

	@Override
	public void handleServer(Session session, Player player, HandshakeMessage message) {
		long start = System.currentTimeMillis();
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_HANDSHAKE) {
			session.getDataMap().put(VanillaProtocol.LOGIN_TIME, System.currentTimeMillis());
			session.getDataMap().put(VanillaProtocol.HANDSHAKE_USERNAME, message.getIdentifier());
			if (VanillaConfiguration.ENCRYPT_MODE.getBoolean()) {
				session.setState(Session.State.EXCHANGE_ENCRYPTION);
				String sessionId = getSessionId();
				session.getDataMap().put(VanillaProtocol.SESSION_ID, sessionId);
				int keySize = VanillaConfiguration.ENCRYPT_KEY_SIZE.getInt();
				String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
				AsymmetricCipherKeyPair keys = SecurityHandler.getInstance().getKeyPair(keySize, keyAlgorithm);
				session.send(false, true, new EncryptionKeyRequestMessage(sessionId, keys.getPublic(), false));
			} else if (VanillaConfiguration.ONLINE_MODE.getBoolean()) {
				session.setState(Session.State.EXCHANGE_IDENTIFICATION);
				String sessionId = getSessionId();
				session.getDataMap().put(VanillaProtocol.SESSION_ID, sessionId);
				session.send(false, true, new HandshakeMessage(sessionId));
			} else {
				session.setState(Session.State.EXCHANGE_IDENTIFICATION);
				String sessionId = "-";
				session.getDataMap().put(VanillaProtocol.SESSION_ID, sessionId);
				session.send(false, true, new HandshakeMessage(sessionId));
			}
		} else {
			session.disconnect(false, new Object[]{"Handshake already exchanged."});
		}
		System.out.println("Handling handshake for " + session + " took " + (System.currentTimeMillis() - start) + " ms");
	}

	private final static SecureRandom random = new SecureRandom();

	static {
		synchronized (random) {
			random.nextBytes(new byte[1]);
		}
	}

	private final String getSessionId() {
		long sessionId;
		synchronized (random) {
			sessionId = random.nextLong();
		}
		StringBuilder sb = new StringBuilder();
		if (sessionId < 0) {
			sessionId = (-sessionId) & 0x7FFFFFFFFFFFFFFFL;
			sb.append("-");
		}
		return sb.append(Long.toString(sessionId, 16)).toString();
	}
}
