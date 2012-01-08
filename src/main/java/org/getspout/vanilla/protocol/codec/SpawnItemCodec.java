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
import org.getspout.vanilla.protocol.msg.SpawnItemMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class SpawnItemCodec extends MessageCodec<SpawnItemMessage> {
	public SpawnItemCodec() {
		super(SpawnItemMessage.class, 0x15);
	}

	@Override
	public SpawnItemMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int itemId = buffer.readUnsignedShort();
		int count = buffer.readUnsignedByte();
		int damage = buffer.readUnsignedShort();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int rotation = buffer.readUnsignedByte();
		int pitch = buffer.readUnsignedByte();
		int roll = buffer.readUnsignedByte();
		return new SpawnItemMessage(id, itemId, count, (short)damage, x, y, z, rotation, pitch, roll);
	}

	@Override
	public ChannelBuffer encode(SpawnItemMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(22);
		buffer.writeInt(message.getId());
		buffer.writeShort(message.getItemId());
		buffer.writeByte(message.getCount());
		buffer.writeShort(message.getDamage());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.getRotation());
		buffer.writeByte(message.getPitch());
		buffer.writeByte(message.getRoll());
		return buffer;
	}
}