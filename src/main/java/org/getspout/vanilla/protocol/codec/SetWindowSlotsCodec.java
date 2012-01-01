/*
 * This file is part of Vanilla (http://www.getspout.org/).
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
import java.util.Map;

import org.getspout.api.inventory.ItemStack;
import org.getspout.api.io.nbt.Tag;
import org.getspout.api.material.MaterialData;
import org.getspout.api.protocol.MessageCodec;
import org.getspout.vanilla.protocol.ChannelBufferUtils;
import org.getspout.vanilla.protocol.msg.SetWindowSlotsMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class SetWindowSlotsCodec extends MessageCodec<SetWindowSlotsMessage> {
	public SetWindowSlotsCodec() {
		super(SetWindowSlotsMessage.class, 0x68);
	}

	@Override
	public SetWindowSlotsMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		int count = buffer.readUnsignedShort();
		ItemStack[] items = new ItemStack[count];
		for (int slot = 0; slot < count; slot++) {
			int item = buffer.readUnsignedShort();
			if (item == 0xFFFF) {
				items[slot] = null;
			} else {
				int itemCount = buffer.readUnsignedByte();
				int damage = buffer.readUnsignedByte();
				Map<String, Tag> nbtData = null;
				if (ChannelBufferUtils.hasNbtData(item)) {
					nbtData = ChannelBufferUtils.readCompound(buffer);
				}
				items[slot] = new ItemStack(MaterialData.getMaterial(id, (short) damage), itemCount, (short) damage).setAuxData(nbtData);
			}
		}
		return new SetWindowSlotsMessage(id, items);
	}

	@Override
	public ChannelBuffer encode(SetWindowSlotsMessage message) throws IOException {
		ItemStack[] items = message.getItems();

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getId());
		buffer.writeShort(items.length);
		for (int slot = 0; slot < items.length; slot++) {
			ItemStack item = items[slot];
			if (item == null) {
				buffer.writeShort(-1);
			} else {
				buffer.writeShort(item.getMaterial().getRawId());
				buffer.writeByte(item.getAmount());
				buffer.writeByte(item.getDamage());
				if (ChannelBufferUtils.hasNbtData(item.getMaterial().getRawData())) {
					ChannelBufferUtils.writeCompound(buffer, item.getAuxData());
				}
			}
		}

		return buffer;
	}
}