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

import java.io.IOException;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import org.jboss.netty.buffer.ChannelBuffer;
import org.junit.Test;

import org.spout.api.protocol.CodecLookupService;
import org.spout.api.protocol.Message;
import org.spout.api.protocol.MessageCodec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class BaseProtocolTest {
	private final CodecLookupService codecLookup;
	private final Message[] testMessages;

	protected BaseProtocolTest(CodecLookupService codecLookup, Message[] testMessages) {
		this.codecLookup = codecLookup;
		this.testMessages = testMessages;
	}

	@Test
	public void testMessageCodecLookup() {
		for (Message message : testMessages) {
			MessageCodec<?> codec = codecLookup.find(message.getClass());
			assertNotNull("Message " + message + " did not have a codec!", codec);
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode <<= 8;
			}
			MessageCodec<?> idCodec = codecLookup.find(opcode);
			assertNotNull("No codec for opcode " + opcode + " in codec lookup!", idCodec);
		}
	}

	@Test
	public void testMessageEncoding() throws IOException {
		for (Message message : testMessages) {
			@SuppressWarnings("rawtypes")
			MessageCodec codec = codecLookup.find(message.getClass());
			@SuppressWarnings("unchecked")
			ChannelBuffer encoded = codec.encode(message);
			Message decoded = codec.decode(encoded);
			assertEquals("Failed for: " + message.getClass().getName(), message, decoded);
		}
	}

	@Test
	public void testTestCompleteness() {
		final TIntSet testedOpcodes = new TIntHashSet();
		for (Message message : testMessages) {
			MessageCodec<?> codec = codecLookup.find(message.getClass());
			if (codec != null) {
				int opcode = codec.getOpcode();
				if (!codec.isExpanded()) {
					opcode <<= 8;
				}
				testedOpcodes.add(opcode);
			}
		}
		for (MessageCodec<?> codec : codecLookup.getCodecs()) {
			int opcode = codec.getOpcode();
			if (!codec.isExpanded()) {
				opcode <<= 8;
			}
			assertTrue("Opcode " + opcode + " (non-expanded: " + (opcode >> 8) + ") not tested", testedOpcodes.contains(opcode));
		}
	}
}
