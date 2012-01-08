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
import org.getspout.vanilla.protocol.msg.ExplosionMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class ExplosionCodec extends MessageCodec<ExplosionMessage> {
	public ExplosionCodec() {
		super(ExplosionMessage.class, 0x3C);
	}

	@Override
	public ExplosionMessage decode(ChannelBuffer buffer) throws IOException {
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		float radius = buffer.readFloat();
		int records = buffer.readInt();
		byte[] coordinates = new byte[records * 3];
		buffer.readBytes(coordinates);
		return new ExplosionMessage(x, y, z, radius, coordinates);
	}

	@Override
	public ChannelBuffer encode(ExplosionMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeDouble(message.getX());
		buffer.writeDouble(message.getY());
		buffer.writeDouble(message.getZ());
		buffer.writeFloat(message.getRadius());
		buffer.writeInt(message.getRecords());
		buffer.writeBytes(message.getCoordinates());
		return buffer;
	}
}