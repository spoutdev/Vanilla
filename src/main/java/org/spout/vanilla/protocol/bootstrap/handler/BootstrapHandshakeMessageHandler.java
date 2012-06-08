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
package org.spout.vanilla.protocol.bootstrap.handler;

import java.security.Key;
import java.security.SecureRandom;

import org.spout.api.map.DefaultedKey;
import org.spout.api.map.DefaultedKeyImpl;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.security.SecurityHandler;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.msg.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.HandshakeMessage;

public class BootstrapHandshakeMessageHandler extends MessageHandler<HandshakeMessage> {
	
	public final static DefaultedKey<String> SESSION_ID = new DefaultedKeyImpl<String>("sessionid", "0000000000000000");
	
	@Override
	public void handle(Session session, Player player, HandshakeMessage message) {
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_HANDSHAKE) {
			if (VanillaConfiguration.ENCRYPT_MODE.getBoolean()) {
				session.setState(Session.State.EXCHANGE_ENCRYPTION);
				String sessionId = getSessionId();
				session.getDataMap().put(SESSION_ID, sessionId);
				int keySize = VanillaConfiguration.ENCRYPT_KEY_SIZE.getInt();
				String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
				Key publicKey = SecurityHandler.getInstance().getKeyPair(keySize, keyAlgorithm).getPublic();
				session.send(new EncryptionKeyRequestMessage(sessionId, publicKey, false), true);
			} else if (VanillaConfiguration.ONLINE_MODE.getBoolean()) {
				session.setState(Session.State.EXCHANGE_IDENTIFICATION);
				String sessionId = getSessionId();
				session.getDataMap().put(SESSION_ID, sessionId);
				session.send(new HandshakeMessage(sessionId), true);
			} else {
				session.setState(Session.State.EXCHANGE_IDENTIFICATION);
				session.send(new HandshakeMessage("-"), true);
			}
		} else {
			session.disconnect("Handshake already exchanged.", false);
		}
	}
	
	private final static SecureRandom random = new SecureRandom();
	
	static {
		synchronized (random) {
			random.nextBytes(new byte[1]);
		}
	}
	
	private final String getSessionId() {
		long sessionId;
		synchronized(random) {
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
