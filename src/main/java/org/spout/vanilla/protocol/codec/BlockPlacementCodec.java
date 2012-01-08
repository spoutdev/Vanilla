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
import java.util.Map;

import org.getspout.api.io.nbt.Tag;
import org.getspout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.BlockPlacementMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class BlockPlacementCodec extends MessageCodec<BlockPlacementMessage> {
	public BlockPlacementCodec() {
		super(BlockPlacementMessage.class, 0x0F);
	}

	@Override
	public BlockPlacementMessage decode(ChannelBuffer buffer) throws IOException {
		int x = buffer.readInt();
		int y = buffer.readUnsignedByte();
		int z = buffer.readInt();
		int direction = buffer.readUnsignedByte();
		int id = buffer.readUnsignedShort();
		if (id == 0xFFFF) {
			return new BlockPlacementMessage(x, y, z, direction);
		} else {
			int count = buffer.readUnsignedByte();
			int damage = buffer.readShort();
			Map<String, Tag> nbtData = null;
			if (ChannelBufferUtils.hasNbtData(id)) nbtData = ChannelBufferUtils.readCompound(buffer);
			return new BlockPlacementMessage(x, y, z, direction, id, count, damage, nbtData);
		}
	}

	@Override
	public ChannelBuffer encode(BlockPlacementMessage message) throws IOException {
		int id = message.getId();

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getX());
		buffer.writeByte(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getDirection());
		buffer.writeShort(id);
		if (id != -1) {
			buffer.writeByte(message.getCount());
			buffer.writeShort(message.getDamage());
			if (ChannelBufferUtils.hasNbtData(id)) ChannelBufferUtils.writeCompound(buffer, message.getNbtData());
		}
		return buffer;
	}
}