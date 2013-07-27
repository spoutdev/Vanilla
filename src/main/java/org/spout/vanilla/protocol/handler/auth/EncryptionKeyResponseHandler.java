/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.handler.auth;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import org.spout.api.protocol.ClientSession;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ServerSession;
import org.spout.api.protocol.Session;
import org.spout.api.scheduler.TaskPriority;
import org.spout.api.security.EncryptionChannelProcessor;
import org.spout.api.security.SecurityHandler;

import org.spout.vanilla.VanillaPlugin;
import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.LoginAuth;
import org.spout.vanilla.protocol.VanillaProtocol;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyResponseMessage;
import org.spout.vanilla.protocol.msg.player.PlayerStatusMessage;

public class EncryptionKeyResponseHandler extends MessageHandler<EncryptionKeyResponseMessage> {
	@Override
	public void handleClient(final ClientSession session, final EncryptionKeyResponseMessage message) {
		System.out.println("Response: " + message.toString());

		String streamCipher = VanillaConfiguration.ENCRYPT_STREAM_ALGORITHM.getString();
		String streamWrapper = VanillaConfiguration.ENCRYPT_STREAM_WRAPPER.getString();

		BufferedBlockCipher fromServerCipher = SecurityHandler.getInstance().getSymmetricCipher(streamCipher, streamWrapper);

		final byte[] sharedSecret = SecurityHandler.getInstance().getSymetricKey();
		CipherParameters symmetricKey = new ParametersWithIV(new KeyParameter(sharedSecret), sharedSecret);

		fromServerCipher.init(SecurityHandler.DECRYPT_MODE, symmetricKey);

		EncryptionChannelProcessor fromServerProcessor = new EncryptionChannelProcessor(fromServerCipher, 32);
		message.getProcessorHandler().setProcessor(fromServerProcessor);

		session.send(true, new PlayerStatusMessage(PlayerStatusMessage.INITIAL_SPAWN)); // Ready to login;
	}

	@Override
	public void handleServer(final ServerSession session, final EncryptionKeyResponseMessage message) {
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_HANDSHAKE) {
			session.disconnect(false, "Handshake not sent");
		} else if (state != Session.State.EXCHANGE_ENCRYPTION) {
			session.disconnect(false, "Encryption was not requested");
		} else {
			int keySize = VanillaConfiguration.ENCRYPT_KEY_SIZE.getInt();
			String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
			String keyPadding = VanillaConfiguration.ENCRYPT_KEY_PADDING.getString();
			AsymmetricBlockCipher cipher = SecurityHandler.getInstance().getAsymmetricCipher(keyAlgorithm, keyPadding);

			AsymmetricCipherKeyPair pair = SecurityHandler.getInstance().getKeyPair(keySize, keyAlgorithm);
			cipher.init(SecurityHandler.DECRYPT_MODE, pair.getPrivate());
			final byte[] initialVector = SecurityHandler.getInstance().processAll(cipher, message.getSecretArray());

			String sessionId = session.getDataMap().get(VanillaProtocol.SESSION_ID);

			final byte[] validateToken = SecurityHandler.getInstance().processAll(cipher, message.getVerifyTokenArray());

			if (validateToken.length != 4) {
				kickInvalidUser(session);
				return;
			}
			final byte[] savedValidateToken = (byte[]) session.getDataMap().get("verifytoken");

			for (int i = 0; i < validateToken.length; i++) {
				if (validateToken[i] != savedValidateToken[i]) {
					kickInvalidUser(session);
					return;
				}
			}

			byte[] publicKeyEncoded = SecurityHandler.getInstance().encodeKey(pair.getPublic());

			String sha1Hash = sha1Hash(new Object[]{sessionId, initialVector, publicKeyEncoded});
			session.getDataMap().put(VanillaProtocol.SESSION_ID, sha1Hash);

			String handshakeUsername = session.getDataMap().get(VanillaProtocol.HANDSHAKE_USERNAME);
			final String finalName = handshakeUsername.split(";")[0];
			session.getDataMap().put("username", finalName);

			Runnable runnable = new Runnable() {
				public void run() {
					String streamCipher = VanillaConfiguration.ENCRYPT_STREAM_ALGORITHM.getString();
					String streamWrapper = VanillaConfiguration.ENCRYPT_STREAM_WRAPPER.getString();

					BufferedBlockCipher fromClientCipher = SecurityHandler.getInstance().getSymmetricCipher(streamCipher, streamWrapper);
					BufferedBlockCipher toClientCipher = SecurityHandler.getInstance().getSymmetricCipher(streamCipher, streamWrapper);

					CipherParameters symmetricKey = new ParametersWithIV(new KeyParameter(initialVector), initialVector);

					fromClientCipher.init(SecurityHandler.DECRYPT_MODE, symmetricKey);
					toClientCipher.init(SecurityHandler.ENCRYPT_MODE, symmetricKey);

					EncryptionChannelProcessor fromClientProcessor = new EncryptionChannelProcessor(fromClientCipher, 32);
					EncryptionChannelProcessor toClientProcessor = new EncryptionChannelProcessor(toClientCipher, 32);

					EncryptionKeyResponseMessage response = new EncryptionKeyResponseMessage(false, new byte[0], new byte[0]);
					response.setProcessor(toClientProcessor);

					message.getProcessorHandler().setProcessor(fromClientProcessor);

					session.send(true, response);
				}
			};

			if (VanillaConfiguration.ONLINE_MODE.getBoolean()) {
				Thread loginAuth = new Thread(new LoginAuth(session, finalName, runnable));
				loginAuth.start();
			} else {
				VanillaPlugin.getInstance().getEngine().getScheduler().scheduleSyncDelayedTask(VanillaPlugin.getInstance(), runnable, TaskPriority.CRITICAL);
			}
		}
	}

	private static String sha1Hash(Object[] input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();

			for (Object o : input) {
				if (o instanceof String) {
					md.update(((String) o).getBytes("ISO_8859_1"));
				} else if (o instanceof byte[]) {
					md.update((byte[]) o);
				} else {
					return null;
				}
			}

			BigInteger bigInt = new BigInteger(md.digest());

			if (bigInt.compareTo(BigInteger.ZERO) < 0) {
				bigInt = bigInt.negate();
				return "-" + bigInt.toString(16);
			} else {
				return bigInt.toString(16);
			}
		} catch (Exception ioe) {
			return null;
		}
	}

	private static void kickInvalidUser(Session session) {
		session.disconnect(false, "Failed to verify username!");
	}
}
