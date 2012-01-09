/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla.protocol.codec;

import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.PingMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class PingCodec extends MessageCodec<PingMessage> {
	public PingCodec() {
		super(PingMessage.class, 0x00);
	}

	@Override
	public PingMessage decode(ChannelBuffer buffer) {
		int id = buffer.readInt();
		return new PingMessage(id);
	}

	@Override
	public ChannelBuffer encode(PingMessage message) {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		buffer.writeInt(message.getPingId());
		return buffer;
	}
}