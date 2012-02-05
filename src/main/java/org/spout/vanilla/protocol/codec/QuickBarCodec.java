/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.codec;

import java.io.IOException;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;
import org.spout.nbt.Tag;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.QuickBarMessage;

public class QuickBarCodec extends MessageCodec<QuickBarMessage> {
	public QuickBarCodec() {
		super(QuickBarMessage.class, 0x6B);
	}

	@Override
	public QuickBarMessage decode(ChannelBuffer buffer) throws IOException {
		short slot = buffer.readShort();
		short id = buffer.readShort();

		if (id != -1) {
			short amount = buffer.readByte();
			short damage = buffer.readShort();
			Map<String, Tag> nbtData = null;
			if (ChannelBufferUtils.hasNbtData(id)) {
				nbtData = ChannelBufferUtils.readCompound(buffer);
			}
			return new QuickBarMessage(slot, id, amount, damage, nbtData);
		} else {
			return new QuickBarMessage(slot, id, (short)0, (short)0, null);
		}
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
}
