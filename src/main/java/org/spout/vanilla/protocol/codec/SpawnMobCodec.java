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
import java.util.List;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.api.util.Parameter;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.SpawnMobMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class SpawnMobCodec extends MessageCodec<SpawnMobMessage> {
	public SpawnMobCodec() {
		super(SpawnMobMessage.class, 0x18);
	}

	@Override
	public SpawnMobMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int type = buffer.readUnsignedByte();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int rotation = buffer.readUnsignedByte();
		int pitch = buffer.readUnsignedByte();
		List<Parameter<?>> parameters = ChannelBufferUtils.readParameters(buffer);
		return new SpawnMobMessage(id, type, x, y, z, rotation, pitch, parameters);
	}

	@Override
	public ChannelBuffer encode(SpawnMobMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getType());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getRotation());
		buffer.writeByte(message.getPitch());
		ChannelBufferUtils.writeParameters(buffer, message.getParameters());
		return buffer;
	}
}