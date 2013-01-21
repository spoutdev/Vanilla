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
package org.spout.vanilla.plugin.protocol.codec.world.block;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.util.Named;
import org.spout.vanilla.plugin.protocol.ChannelBufferUtils;
import org.spout.vanilla.plugin.protocol.msg.world.block.CommandBlockMessage;

public class CommandBlockCodec extends MessageCodec<CommandBlockMessage> implements Named {
	public static final String channelName = "MC|AdvCdm";

	public CommandBlockCodec(int opcode) {
		super(CommandBlockMessage.class, opcode);
	}

	@Override
	public ChannelBuffer encode(CommandBlockMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getX());
		buffer.writeInt(message.getY());
		buffer.writeInt(message.getZ());
		ChannelBufferUtils.writeString(buffer, message.getCommandInput());
		return buffer;
	}

	@Override
	public CommandBlockMessage decode(ChannelBuffer buffer) {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		String command = ChannelBufferUtils.readString(buffer);
		return new CommandBlockMessage(x, y, z, command);
	}

	public String getName() {
		return channelName;
	}
}
