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
package org.spout.vanilla.plugin.protocol.handler.auth;

import java.security.MessageDigest;
import java.util.Random;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.KeyParameter;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.security.SecurityHandler;
import org.spout.vanilla.plugin.configuration.VanillaConfiguration;
import org.spout.vanilla.plugin.protocol.ClientLoginAuth;
import org.spout.vanilla.plugin.protocol.msg.auth.EncryptionKeyRequestMessage;
import org.spout.vanilla.plugin.protocol.msg.auth.EncryptionKeyResponseMessage;

public class EncryptionKeyRequestHandler extends MessageHandler<EncryptionKeyRequestMessage> {
	@Override
	public void handleClient(final Session session, final EncryptionKeyRequestMessage message) {
		System.out.println(message.toString());
		
		final byte[] sharedSecret = generateSymetricKey();
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
				String keyPadding = VanillaConfiguration.ENCRYPT_KEY_PADDING.getString();
				AsymmetricBlockCipher cipher = SecurityHandler.getInstance().getAsymmetricCipher(keyAlgorithm, keyPadding);
				//TODO: need to be changed
				cipher.init(SecurityHandler.ENCRYPT_MODE, new KeyParameter(message.getSecretArray()));
				
				try {
					byte[] encodedSecret = cipher.processBlock(sharedSecret, 0, 16);
					byte[] encodedToken = cipher.processBlock(message.getVerifyTokenArray(), 0, 4);
					
					session.send(true, true, new EncryptionKeyResponseMessage(false, encodedSecret, encodedToken));
				} catch (InvalidCipherTextException e) {
					e.printStackTrace();
				}
			}
		};
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.reset();
			md.update(message.getSessionId().getBytes("ISO_8859_1"));
			md.update(sharedSecret);
			md.update(message.getVerifyTokenArray());
			
			String hash = convertToHex(md.digest());
			
			Thread loginAuth = new Thread(new ClientLoginAuth(hash, runnable));
			loginAuth.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}
	
	private byte[] generateSymetricKey() {
		final byte[] key = new byte[16];
		final Random rand = new Random();
		rand.nextBytes(key);
		return key;
	}
}
