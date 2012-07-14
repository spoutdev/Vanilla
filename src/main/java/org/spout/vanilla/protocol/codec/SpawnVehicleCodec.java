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

import net.royawesome.jlibnoise.MathHelper;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;

import org.spout.vanilla.protocol.msg.SpawnVehicleMessage;

public final class SpawnVehicleCodec extends MessageCodec<SpawnVehicleMessage> {
	public SpawnVehicleCodec() {
		super(SpawnVehicleMessage.class, 0x17);
	}

	@Override
	public SpawnVehicleMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int type = buffer.readUnsignedByte();
		double x = buffer.readInt() / 32.0;
		double y = buffer.readInt() / 32.0;
		double z = buffer.readInt() / 32.0;
		int fireballId = buffer.readInt();
		if (fireballId != 0) {
			double fireballX = buffer.readShort() / 8000.0;
			double fireballY = buffer.readShort() / 8000.0;
			double fireballZ = buffer.readShort() / 8000.0;
			return new SpawnVehicleMessage(id, type, x, y, z, fireballId, fireballX, fireballY, fireballZ);
		}
		return new SpawnVehicleMessage(id, type, x, y, z);
	}

	@Override
	public ChannelBuffer encode(SpawnVehicleMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(message.hasFireball() ? 27 : 21);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getType());
		buffer.writeInt(MathHelper.floor(message.getX() * 32.0));
		buffer.writeInt(MathHelper.floor(message.getY() * 32.0));
		buffer.writeInt(MathHelper.floor(message.getZ() * 32.0));
		buffer.writeInt(message.getFireballId());
		if (message.hasFireball()) {
			buffer.writeShort((int) (message.getFireballX() * 8000.0));
			buffer.writeShort((int) (message.getFireballY() * 8000.0));
			buffer.writeShort((int) (message.getFireballZ() * 8000.0));
		}
		return buffer;
	}
}
