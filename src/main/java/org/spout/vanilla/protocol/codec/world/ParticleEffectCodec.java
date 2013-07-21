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
package org.spout.vanilla.protocol.codec.world;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.VanillaChannelBufferUtils;
import org.spout.vanilla.protocol.msg.world.ParticleEffectMessage;

public class ParticleEffectCodec extends MessageCodec<ParticleEffectMessage> {
	public ParticleEffectCodec() {
		super(ParticleEffectMessage.class, 0x3F);
	}

	@Override
	public ParticleEffectMessage decode(ChannelBuffer buffer) throws IOException {
		String name = VanillaChannelBufferUtils.readString(buffer);
		float x = buffer.readFloat();
		float y = buffer.readFloat();
		float z = buffer.readFloat();
		float xOffset = buffer.readFloat();
		float yOffset = buffer.readFloat();
		float zOffset = buffer.readFloat();
		float velocity = buffer.readFloat();
		int amount = buffer.readInt();
		return new ParticleEffectMessage(name, x, y, z, xOffset, yOffset, zOffset, velocity, amount, NullRepositionManager.getInstance());
	}

	@Override
	public ChannelBuffer encode(ParticleEffectMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		VanillaChannelBufferUtils.writeString(buffer, message.getName());
		buffer.writeFloat(message.getX());
		buffer.writeFloat(message.getY());
		buffer.writeFloat(message.getZ());
		buffer.writeFloat(message.getXOffset());
		buffer.writeFloat(message.getYOffset());
		buffer.writeFloat(message.getZOffset());
		buffer.writeFloat(message.getVelocity());
		buffer.writeInt(message.getAmount());
		return buffer;
	}
}
