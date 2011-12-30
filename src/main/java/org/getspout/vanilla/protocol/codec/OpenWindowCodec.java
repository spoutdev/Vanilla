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
import org.getspout.vanilla.protocol.ChannelBufferUtils;
import org.getspout.vanilla.protocol.msg.OpenWindowMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class OpenWindowCodec extends MessageCodec<OpenWindowMessage> {
	public OpenWindowCodec() {
		super(OpenWindowMessage.class, 0x64);
	}

	@Override
	public OpenWindowMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		int type = buffer.readUnsignedByte();
		String title = ChannelBufferUtils.readString(buffer);
		int slots = buffer.readUnsignedByte();
		return new OpenWindowMessage(id, type, title, slots);
	}

	@Override
	public ChannelBuffer encode(OpenWindowMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getId());
		buffer.writeByte(message.getType());
		ChannelBufferUtils.writeString(buffer, message.getTitle());
		buffer.writeByte(message.getSlots());
		return buffer;
	}
}