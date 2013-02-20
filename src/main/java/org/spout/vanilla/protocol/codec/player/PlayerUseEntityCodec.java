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
package org.spout.vanilla.protocol.codec.player;

import java.io.IOException;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.msg.player.PlayerUseEntityMessage;

public final class PlayerUseEntityCodec extends MessageCodec<PlayerUseEntityMessage> {
	public PlayerUseEntityCodec() {
		super(PlayerUseEntityMessage.class, 0x07);
	}

	@Override
	public PlayerUseEntityMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int target = buffer.readInt();
		boolean punching = buffer.readByte() != 0;
		return new PlayerUseEntityMessage(id, target, punching);
	}

	@Override
	public ChannelBuffer encode(PlayerUseEntityMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(9);
		buffer.writeInt(message.getEntityId());
		buffer.writeInt(message.getTarget());
		buffer.writeByte(message.isPunching() ? 1 : 0);
		return buffer;
	}
}
