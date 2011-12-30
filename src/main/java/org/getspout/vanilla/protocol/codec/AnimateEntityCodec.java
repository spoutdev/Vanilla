/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
package org.getspout.vanilla.protocol.codec;

import java.io.IOException;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.vanilla.protocol.msg.AnimateEntityMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class AnimateEntityCodec extends MessageCodec<AnimateEntityMessage> {
	public AnimateEntityCodec() {
		super(AnimateEntityMessage.class, 0x12);
	}

	@Override
	public AnimateEntityMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int animation = buffer.readUnsignedByte();
		return new AnimateEntityMessage(id, animation);
	}

	@Override
	public ChannelBuffer encode(AnimateEntityMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getAnimation());
		return buffer;
	}
}