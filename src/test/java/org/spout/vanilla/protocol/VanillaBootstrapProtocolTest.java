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
package org.spout.vanilla.protocol;

import org.spout.api.protocol.Message;
import org.spout.api.protocol.common.message.CustomDataMessage;

import org.spout.vanilla.protocol.bootstrap.VanillaBootstrapCodecLookupService;
import org.spout.vanilla.protocol.msg.EncryptionKeyRequestMessage;
import org.spout.vanilla.protocol.msg.EncryptionKeyResponseMessage;
import org.spout.vanilla.protocol.msg.login.HandshakeMessage;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.login.LoginRequestMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;
import org.spout.vanilla.protocol.msg.login.request.ClientLoginRequestMessage;
import org.spout.vanilla.protocol.msg.login.request.ServerLoginRequestMessage;

public class VanillaBootstrapProtocolTest extends BaseProtocolTest {
	private static final VanillaBootstrapCodecLookupService CODEC_LOOKUP = new VanillaBootstrapCodecLookupService();
	private static final Message[] TEST_MESSAGES = new Message[]{
			new ClientLoginRequestMessage(),
			new ServerLoginRequestMessage(0, "", (byte) 0, (byte) 0, (byte) 0, (short) 10),
			new HandshakeMessage((byte) 42, "Spouty", "SpoutTron", 9001),
			new ServerListPingMessage(),
			new EncryptionKeyResponseMessage(new byte[]{(byte) 7, (byte) 4, (byte) 1, (byte) 122}, true),
			new EncryptionKeyRequestMessage("This is a server", new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 10}, true),
			new KickMessage("This is a test"),
			new CustomDataMessage("CHANNEL:ONE", new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4})};

	public VanillaBootstrapProtocolTest() {
		super(CODEC_LOOKUP, TEST_MESSAGES);
	}
}
