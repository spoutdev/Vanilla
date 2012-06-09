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

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.spout.api.player.Player;
import org.spout.api.protocol.MessageHandler;
import org.spout.api.protocol.Session;
import org.spout.api.security.SecurityHandler;
import org.spout.vanilla.configuration.VanillaConfiguration;
import org.spout.vanilla.protocol.msg.EncryptionKeyResponseMessage;

public class BootstrapEncryptionKeyResponseMessageHandler extends MessageHandler<EncryptionKeyResponseMessage> {
	
	@Override
	public void handle(Session session, Player player, EncryptionKeyResponseMessage message) {
		Session.State state = session.getState();
		if (state == Session.State.EXCHANGE_HANDSHAKE) {
			session.disconnect("Handshake not sent", false);
		} else if (state != Session.State.EXCHANGE_ENCRYPTION) {
			session.disconnect("Encryption response not sent", false);
		} else {
			int keySize = VanillaConfiguration.ENCRYPT_KEY_SIZE.getInt();
			String keyAlgorithm = VanillaConfiguration.ENCRYPT_KEY_ALGORITHM.getString();
			String keyPadding = VanillaConfiguration.ENCRYPT_KEY_PADDING.getString();
			AsymmetricBlockCipher cipher = SecurityHandler.getInstance().getAsymmetricCipher(keyAlgorithm, keyPadding);
			
			AsymmetricCipherKeyPair pair = SecurityHandler.getInstance().getKeyPair(keySize, keyAlgorithm);
			cipher.init(SecurityHandler.DECRYPT_MODE, pair.getPrivate());
			byte[] decrypted = SecurityHandler.getInstance().processAll(cipher, message.getEncodedArray());
			
			session.disconnect("Encryption not supported yet");
		}
	}

}
