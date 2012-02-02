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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.RotationMessage;

public final class RotationCodec extends MessageCodec<RotationMessage> {
	public RotationCodec() {
		super(RotationMessage.class, 0x0C);
	}

	@Override
	public RotationMessage decode(ChannelBuffer buffer) throws IOException {
		float rotation = buffer.readFloat();
		float pitch = buffer.readFloat();
		boolean onGround = buffer.readByte() == 1;
		return new RotationMessage(rotation, pitch, onGround);
	}

	@Override
	public ChannelBuffer encode(RotationMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(9);
		buffer.writeFloat(message.getRotation());
		buffer.writeFloat(message.getPitch());
		buffer.writeByte(message.isOnGround() ? 1 : 0);
		return buffer;
	}
}
