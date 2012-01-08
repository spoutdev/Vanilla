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
import org.spout.vanilla.protocol.msg.QuickBarMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public class QuickBarCodec extends MessageCodec<QuickBarMessage> {
	public QuickBarCodec() {
		super(QuickBarMessage.class, 0x6B);
	}

	@Override
	public ChannelBuffer encode(QuickBarMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeShort(message.getSlot());
		buffer.writeShort(message.getId());
		buffer.writeByte(message.getAmount());
		buffer.writeShort(message.getDamage());
		if (ChannelBufferUtils.hasNbtData(message.getId())) {
			ChannelBufferUtils.writeCompound(buffer, message.getNbtData());
		}
		return buffer;
	}

	@Override
	public QuickBarMessage decode(ChannelBuffer buffer) throws IOException {
		short slot = buffer.readShort();
		short id = buffer.readShort();

		if (id != -1) {
			short amount = buffer.readByte();
			short damage = buffer.readShort();
			Map<String, Tag> nbtData = null;
			if (ChannelBufferUtils.hasNbtData(id)) nbtData = ChannelBufferUtils.readCompound(buffer);
			return new QuickBarMessage(slot, id, amount, damage, nbtData);
		} else {
			return new QuickBarMessage(slot, id, (short)0, (short)0, null);
		}
	}
}