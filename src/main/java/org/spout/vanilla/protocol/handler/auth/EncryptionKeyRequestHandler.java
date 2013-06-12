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
package org.spout.vanilla.protocol.handler.auth;

import java.math.BigInteger;
import java.security.MessageDigest;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.util.PublicKeyFactory;

import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.ClientSession;
import org.spout.api.security.EncryptionChannelProcessor;
import org.spout.api.security.SecurityHandler;

import org.spout.vanilla.data.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.ClientLoginAuth;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.auth.EncryptionKeyResponseMessage;

public class EncryptionKeyRequestHandler extends MessageHandler<EncryptionKeyRequestMessage> {
	@Override
	public void handleClient(final ClientSession session, final EncryptionKeyRequestMessage message) {
		final byte[] sharedSecret = SecurityHandler.getInstance().getSymetricKey();

		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
				String keyPadding = VanillaConfiguration.ENCRYPT_KEY_PADDING.getString();

				AsymmetricBlockCipher cipher = SecurityHandler.getInstance().getAsymmetricCipher(keyAlgorithm, keyPadding);

				try {
					AsymmetricKeyParameter publicKey = PublicKeyFactory.createKey(message.getSecretArray());
					cipher.init(SecurityHandler.ENCRYPT_MODE, publicKey);

					byte[] encodedSecret = cipher.processBlock(sharedSecret, 0, 16);
					byte[] encodedToken = cipher.processBlock(message.getVerifyTokenArray(), 0, 4);

					String streamCipher = VanillaConfiguration.ENCRYPT_STREAM_ALGORITHM.getString();
					String streamWrapper = VanillaConfiguration.ENCRYPT_STREAM_WRAPPER.getString();

					BufferedBlockCipher toServerCipher = SecurityHandler.getInstance().getSymmetricCipher(streamCipher, streamWrapper);

					CipherParameters symmetricKey = new ParametersWithIV(new KeyParameter(sharedSecret), sharedSecret);

					toServerCipher.init(SecurityHandler.ENCRYPT_MODE, symmetricKey);
					EncryptionChannelProcessor toServerProcessor = new EncryptionChannelProcessor(toServerCipher, 32);

					EncryptionKeyResponseMessage response = new EncryptionKeyResponseMessage(false, encodedSecret, encodedToken);
					response.setProcessor(toServerProcessor);

					session.send(true, true, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		String hash = sha1Hash(new Object[]{message.getSessionId(), sharedSecret, message.getSecretArray()});

		Thread loginAuth = new Thread(new ClientLoginAuth(hash, runnable));
		loginAuth.start();
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
}
