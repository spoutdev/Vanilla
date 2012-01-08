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

import org.getspout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.EntityRemoveEffectMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class EntityRemoveEffectCodec extends MessageCodec<EntityRemoveEffectMessage> {
	public EntityRemoveEffectCodec() {
		super(EntityRemoveEffectMessage.class, 0x2A);
	}

	@Override
	public ChannelBuffer encode(EntityRemoveEffectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(6);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getEffect());
		return buffer;
	}

	@Override
	public EntityRemoveEffectMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		byte effect = buffer.readByte();
		return new EntityRemoveEffectMessage(id, effect);
	}
}