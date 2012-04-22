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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.protocol.MessageCodec;

import org.spout.nbt.CompoundMap;

import org.spout.vanilla.material.item.generic.Tool;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.SetWindowSlotsMessage;

public final class SetWindowSlotsCodec extends MessageCodec<SetWindowSlotsMessage> {
	public SetWindowSlotsCodec() {
		super(SetWindowSlotsMessage.class, 0x68);
	}

	@Override
	public SetWindowSlotsMessage decode(ChannelBuffer buffer) throws IOException {
		byte id = buffer.readByte();
		short count = buffer.readShort();
		ItemStack[] items = new ItemStack[count];
		for (int slot = 0; slot < count; slot++) {
			short item = buffer.readShort();
			if (item == 0xFFFF) {
				items[slot] = null;
			} else {
				byte itemCount = buffer.readByte();
				short data = buffer.readShort();
				CompoundMap nbtData = null;
				if (ChannelBufferUtils.hasNbtData(item)) {
					nbtData = ChannelBufferUtils.readCompound(buffer);
				}
				items[slot] = new ItemStack(Material.get(item), data, itemCount).setAuxData(nbtData);
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
		for (ItemStack item : items) {
			if (item == null) {
				buffer.writeShort(-1);
			} else {
				buffer.writeShort(item.getMaterial().getId());
				buffer.writeByte(item.getAmount());
				if (item.getMaterial() instanceof Tool) {
					System.out.println("damage");
					buffer.writeShort(item.getData());
				} else {
					System.out.println("data");
					buffer.writeShort(item.getData());
				}
				if (ChannelBufferUtils.hasNbtData(item.getData())) {
					ChannelBufferUtils.writeCompound(buffer, item.getAuxData());
				}
			}
		}
		return buffer;
	}
}
