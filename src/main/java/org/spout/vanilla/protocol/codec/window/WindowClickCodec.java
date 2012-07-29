/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.protocol.codec.window;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.nbt.CompoundMap;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.window.WindowClickMessage;

public final class WindowClickCodec extends MessageCodec<WindowClickMessage> {
	public WindowClickCodec() {
		super(WindowClickMessage.class, 0x66);
	}

	@Override
	public WindowClickMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		int slot = buffer.readUnsignedShort();
		boolean rightClick = buffer.readUnsignedByte() != 0;
		int transaction = buffer.readUnsignedShort();
		boolean shift = buffer.readUnsignedByte() != 0;
		int item = buffer.readUnsignedShort();
		if (item == 0xFFFF) {
			return new WindowClickMessage(id, slot, rightClick, transaction, shift);
		}

		int count = buffer.readUnsignedByte();
		int damage = buffer.readUnsignedShort();
		CompoundMap nbtData = ChannelBufferUtils.readCompound(buffer);
		return new WindowClickMessage(id, slot, rightClick, transaction, shift, item, count, damage, nbtData);
	}

	@Override
	public ChannelBuffer encode(WindowClickMessage message) throws IOException {
		int item = message.getItem();

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getWindowInstanceId());
		buffer.writeShort(message.getSlot());
		buffer.writeByte(message.isRightClick() ? 1 : 0);
		buffer.writeShort(message.getTransaction());
		buffer.writeByte(message.isShift() ? 1 : 0);
		buffer.writeShort(item);
		if (item != -1) {
			buffer.writeByte(message.getCount());
			buffer.writeShort(message.getDamage());
			if (ChannelBufferUtils.hasNbtData(message.getItem())) {
				ChannelBufferUtils.writeCompound(buffer, message.getNbtData());
			} else {
				buffer.writeShort(-1);
			}
		}
		return buffer;
	}
}
