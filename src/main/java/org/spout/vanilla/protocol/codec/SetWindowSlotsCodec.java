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

import org.spout.api.inventory.ItemStack;
import org.spout.api.io.nbt.Tag;
import org.spout.api.material.MaterialData;
import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;

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
				items[slot] = new ItemStack(MaterialData.getMaterial((short)id, (byte) damage), itemCount, (short) damage).setAuxData(nbtData);
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
		for(ItemStack item : items) {
			if (item == null) {
				buffer.writeShort(-1);
			} else {
				buffer.writeShort(item.getMaterial().getId());
				buffer.writeByte(item.getAmount());
				buffer.writeByte(item.getDamage());
				if (ChannelBufferUtils.hasNbtData(item.getMaterial().getData())) {
					ChannelBufferUtils.writeCompound(buffer, item.getAuxData());
				}
			}
		}
		return buffer;
	}
}
