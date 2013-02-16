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
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.protocol.msg.player.PlayerAbilityMessage;

public class PlayerAbilityCodec extends MessageCodec<PlayerAbilityMessage> {
	public PlayerAbilityCodec() {
		super(PlayerAbilityMessage.class, 0xCA);
	}

	@Override
	public ChannelBuffer encode(PlayerAbilityMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		byte flag = 0;
		flag = LogicUtil.setBit(flag, 0x1, message.isGodMode());
		flag = LogicUtil.setBit(flag, 0x2, message.isFlying());
		flag = LogicUtil.setBit(flag, 0x4, message.canFly());
		flag = LogicUtil.setBit(flag, 0x8, message.isCreativeMode());
		buffer.writeByte(flag);
		buffer.writeByte(message.getFlyingSpeed());
		buffer.writeByte(message.getWalkingSpeed());
		return buffer;
	}

	@Override
	public PlayerAbilityMessage decode(ChannelBuffer buffer) throws IOException {
		byte flag = buffer.readByte();
		boolean godMode = LogicUtil.getBit(flag, 0x1);
		boolean isFlying = LogicUtil.getBit(flag, 0x2);
		boolean canFly = LogicUtil.getBit(flag, 0x4);
		boolean creativeMode = LogicUtil.getBit(flag, 0x8);
		byte flyingSpeed = buffer.readByte();
		byte walkingSpeed = buffer.readByte();
		return new PlayerAbilityMessage(godMode, isFlying, canFly, creativeMode, flyingSpeed, walkingSpeed);
	}
}
