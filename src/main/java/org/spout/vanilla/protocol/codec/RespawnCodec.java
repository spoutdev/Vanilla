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

import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.RespawnMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class RespawnCodec extends MessageCodec<RespawnMessage> {
	public RespawnCodec() {
		super(RespawnMessage.class, 0x09);
	}

	@Override
	public RespawnMessage decode(ChannelBuffer buffer) throws IOException {
		byte dimension = buffer.readByte();
		byte difficulty = buffer.readByte();
		byte mode = buffer.readByte();
		int worldHeight = ChannelBufferUtils.getExpandedHeight(buffer.readShort());
		long seed = buffer.readLong();
		return new RespawnMessage(dimension, difficulty, mode, worldHeight, seed);
	}

	@Override
	public ChannelBuffer encode(RespawnMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(14);
		buffer.writeByte(message.getDimension());
		buffer.writeByte(message.getDifficulty());
		buffer.writeByte(message.getGameMode());
		buffer.writeShort(ChannelBufferUtils.getShifts(message.getWorldHeight()) - 1);
		buffer.writeLong(message.getSeed());
		return buffer;
	}
}