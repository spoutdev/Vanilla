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

import java.io.IOException;

import org.getspout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.StateChangeMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class StateChangeCodec extends MessageCodec<StateChangeMessage> {
	public StateChangeCodec() {
		super(StateChangeMessage.class, 0x46);
	}

	@Override
	public StateChangeMessage decode(ChannelBuffer buffer) throws IOException {
		byte state = buffer.readByte();
		byte something = buffer.readByte();
		return new StateChangeMessage(state, something);
	}

	@Override
	public ChannelBuffer encode(StateChangeMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(3);
		buffer.writeByte(message.getState());
		buffer.writeByte(message.getGameMode());
		return buffer;
	}
}