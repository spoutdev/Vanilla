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
import org.getspout.vanilla.protocol.msg.SpawnVehicleMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class SpawnVehicleCodec extends MessageCodec<SpawnVehicleMessage> {
	public SpawnVehicleCodec() {
		super(SpawnVehicleMessage.class, 0x17);
	}

	@Override
	public SpawnVehicleMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int type = buffer.readUnsignedByte();
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		int fireballId = buffer.readInt();
		if (fireballId != 0) {
			int fireballX = buffer.readShort();
			int fireballY = buffer.readShort();
			int fireballZ = buffer.readShort();
			return new SpawnVehicleMessage(id, type, x, y, z, fireballId, fireballX, fireballY, fireballZ);
		}
		return new SpawnVehicleMessage(id, type, x, y, z);
	}

	@Override
	public ChannelBuffer encode(SpawnVehicleMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(message.hasFireball() ? 28 : 22);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getType());
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeInt(message.getFireballId());
		if (message.hasFireball()) {
			buffer.writeShort(message.getFireballX());
			buffer.writeShort(message.getFireballY());
			buffer.writeShort(message.getFireballZ());
		}
		return buffer;
	}
}