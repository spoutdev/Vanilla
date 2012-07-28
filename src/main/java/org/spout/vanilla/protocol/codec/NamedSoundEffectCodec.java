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
package org.spout.vanilla.protocol.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.NamedSoundEffectMessage;

public class NamedSoundEffectCodec extends MessageCodec<NamedSoundEffectMessage> {

	public NamedSoundEffectCodec() {
		super(NamedSoundEffectMessage.class, 0x3E);
	}

	@Override
	public NamedSoundEffectMessage decode(ChannelBuffer buffer) throws IOException {
		String soundName = ChannelBufferUtils.readString(buffer);
		int effectPositionX = buffer.readInt();
		int effectPositionY = buffer.readInt();
		int effectPositionZ = buffer.readInt();
		float volume = buffer.readFloat();
		byte pitch = buffer.readByte();
		return new NamedSoundEffectMessage(soundName, effectPositionX, effectPositionY, effectPositionZ, volume, pitch);
	}

	@Override
	public ChannelBuffer encode(NamedSoundEffectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeString(buffer, message.getSoundName());
		buffer.writeInt(message.getEffectPositionX());
		buffer.writeInt(message.getEffectPositionY());
		buffer.writeInt(message.getEffectPositionZ());
		buffer.writeFloat(message.getVolume());
		buffer.writeByte(message.getPitch());
		return buffer;
	}
}
