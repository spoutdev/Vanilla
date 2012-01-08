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
package org.getspout.vanilla.protocol.codec;

import java.io.IOException;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.vanilla.protocol.msg.MapDataMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class MapDataCodec extends MessageCodec<MapDataMessage> {
	public MapDataCodec() {
		super(MapDataMessage.class, 0x83);
	}

	@Override
	public MapDataMessage decode(ChannelBuffer buffer) throws IOException {
		short type = buffer.readShort();
		short id = buffer.readShort();
		short size = buffer.readUnsignedByte();

		byte[] data = new byte[size];
		for (int i = 0; i < data.length; ++i) {
			data[i] = buffer.readByte();
		}

		return new MapDataMessage(type, id, data);
	}

	@Override
	public ChannelBuffer encode(MapDataMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeShort(message.getType());
		buffer.writeShort(message.getId());
		buffer.writeByte(message.getData().length);
		buffer.writeBytes(message.getData());
		return buffer;
	}
}