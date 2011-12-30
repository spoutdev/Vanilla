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
import org.getspout.vanilla.protocol.msg.UpdateSignMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class UpdateSignCodec extends MessageCodec<UpdateSignMessage> {
	public UpdateSignCodec() {
		super(UpdateSignMessage.class, 0x82);
	}

	@Override
	public UpdateSignMessage decode(ChannelBuffer buffer) throws IOException {
		int x = buffer.readInt();
		int y = buffer.readShort();
		int z = buffer.readInt();
		String[] message = new String[4];
		for (int i = 0; i < message.length; i++) {
			message[i] = ChannelBufferUtils.readString(buffer);
		}
		return new UpdateSignMessage(x, y, z, message);
	}

	@Override
	public ChannelBuffer encode(UpdateSignMessage message) throws IOException {
		String[] lines = message.getMessage();

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getX());
		buffer.writeShort(message.getY());
		buffer.writeInt(message.getZ());
		for (int i = 0; i < lines.length; i++) {
			ChannelBufferUtils.writeString(buffer, lines[i]);
		}
		return buffer;
	}
}