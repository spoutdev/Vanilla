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

import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.HealthMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class HealthCodec extends MessageCodec<HealthMessage> {
	public HealthCodec() {
		super(HealthMessage.class, 0x08);
	}

	@Override
	public HealthMessage decode(ChannelBuffer buffer) throws IOException {
		int health = buffer.readShort();
		int food = buffer.readShort();
		float foodSaturation = buffer.readFloat();
		return new HealthMessage(health, food, foodSaturation);
	}

	@Override
	public ChannelBuffer encode(HealthMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(9);
		buffer.writeShort(message.getHealth());
		buffer.writeShort(message.getFood());
		buffer.writeFloat(message.getFoodSaturation());
		return buffer;
	}
}