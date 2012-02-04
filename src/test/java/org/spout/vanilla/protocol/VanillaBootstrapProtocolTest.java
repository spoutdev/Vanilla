/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
package org.spout.vanilla.protocol;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.bootstrap.VanillaBootstrapCodecLookupService;
import org.spout.vanilla.protocol.msg.HandshakeMessage;
import org.spout.vanilla.protocol.msg.IdentificationMessage;
import org.spout.vanilla.protocol.msg.KickMessage;
import org.spout.vanilla.protocol.msg.ServerListPingMessage;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 * @author zml2008
 */
public class VanillaBootstrapProtocolTest {
	private static final TIntSet testedOpcodes = new TIntHashSet();
	private static final VanillaBootstrapCodecLookupService CODEC_LOOKUP = new VanillaBootstrapCodecLookupService();
	private static final Message[] TEST_MESSAGES = new Message[] {
			new IdentificationMessage(0, "Tester", 244888L, 0, -1, 0, 128, 20, "MAGICAL"),
			new HandshakeMessage("Player"),
			new ServerListPingMessage(),
			new KickMessage("This is a test")
	};

	@Test
	public void testMessageCodecLookup() {
		for (Message message : TEST_MESSAGES) {
			MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			assertNotNull("Message " + message + " did not have a codec!", codec);
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode = opcode << 8;
			}
			MessageCodec idCodec = CODEC_LOOKUP.find(opcode);
			assertNotNull("No codec for opcode "+ opcode + " in codec lookup!", idCodec);
			testedOpcodes.add(opcode);
		}
	}
	
	@Test
	public void testMessageEncoding() throws IOException {
		for (Message message : TEST_MESSAGES) {
			MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			ChannelBuffer encoded = codec.encode(message);
			Message decoded = codec.decode(encoded);
			assertEquals(message.toString(), decoded.toString());
		}
	}

	@Test
	public void testTestCompleteness() {
		final TIntSet testedOpcodes = new TIntHashSet();
		for (Message message : TEST_MESSAGES) {
			MessageCodec codec = CODEC_LOOKUP.find(message.getClass());
			if (codec != null) {
				int opcode = codec.getOpcode();
				if (!codec.isExpanded()) {
					opcode = opcode << 8;
				}
				testedOpcodes.add(opcode);
			}
		}
		for (MessageCodec<?> codec : CODEC_LOOKUP.getCodecs()) {
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode = opcode << 8;
			}
			assertTrue("Opcode "+ opcode + " (non-expanded: " + (opcode >> 8) + ") not tested", testedOpcodes.contains(opcode));
		}
	}
}
