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
import org.getspout.vanilla.protocol.msg.AttachEntityMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class AttachEntityCodec extends MessageCodec<AttachEntityMessage> {
	public AttachEntityCodec() {
		super(AttachEntityMessage.class, 0x27);
	}

	@Override
	public AttachEntityMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int vehicle = buffer.readInt();
		return new AttachEntityMessage(id, vehicle);
	}

	@Override
	public ChannelBuffer encode(AttachEntityMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(8);
		buffer.writeInt(message.getId());
		buffer.writeInt(message.getVehicle());
		return buffer;
	}
}