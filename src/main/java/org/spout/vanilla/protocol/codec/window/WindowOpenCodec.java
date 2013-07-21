/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.protocol.codec.window;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.inventory.window.WindowType;
import org.spout.api.util.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.window.WindowOpenMessage;

public final class WindowOpenCodec extends MessageCodec<WindowOpenMessage> {
	public WindowOpenCodec() {
		super(WindowOpenMessage.class, 0x64);
	}

	@Override
	public WindowOpenMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		WindowType type = WindowType.get(buffer.readUnsignedByte());
		if (type == null) {
			throw new IOException("Read Window Type is invalid");
		}
		String title = ChannelBufferUtils.readString(buffer);
		int slots = buffer.readUnsignedByte();
		boolean useTitle = buffer.readUnsignedByte() != 0;
		return new WindowOpenMessage(id, type, title, slots, useTitle);
	}

	@Override
	public ChannelBuffer encode(WindowOpenMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getWindowInstanceId());
		buffer.writeByte(message.getType().getId());
		ChannelBufferUtils.writeString(buffer, message.getTitle());
		buffer.writeByte(message.getSlots());
		buffer.writeByte(message.isUsingTitle() ? 1 : 0);
		return buffer;
	}
}
