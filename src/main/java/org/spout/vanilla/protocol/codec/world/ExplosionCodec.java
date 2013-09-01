/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.msg.world.ExplosionMessage;

public final class ExplosionCodec extends MessageCodec<ExplosionMessage> {
	public ExplosionCodec() {
		super(ExplosionMessage.class, 0x3C);
	}

	@Override
	public ExplosionMessage decode(ByteBuf buffer) throws IOException {
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		float radius = buffer.readFloat();
		int records = buffer.readInt();
		byte[] coordinates = new byte[records * 3];
		buffer.readBytes(coordinates);
		buffer.readFloat(); // unknown (x?)
		buffer.readFloat(); // unknown (y?)
		buffer.readFloat(); // unknown (z?)
		return new ExplosionMessage(x, y, z, radius, coordinates, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(ExplosionMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer();
		buffer.writeDouble(message.getX());
		buffer.writeDouble(message.getY());
		buffer.writeDouble(message.getZ());
		buffer.writeFloat(message.getRadius());
		buffer.writeInt(message.getRecords());
		buffer.writeBytes(message.getCoordinates());
		buffer.writeFloat(0.0f); // unknown (x?)
		buffer.writeFloat(0.0f); // unknown (y?)
		buffer.writeFloat(0.0f); // unknown (z?)
		return buffer;
	}
}
