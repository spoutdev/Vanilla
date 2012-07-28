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

import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.protocol.MessageCodec;

import org.spout.nbt.CompoundMap;

import org.spout.vanilla.protocol.ChannelBufferUtils;
import org.spout.vanilla.protocol.msg.PlayerBlockPlacementMessage;

public final class PlayerBlockPlacementCodec extends MessageCodec<PlayerBlockPlacementMessage> {
	public PlayerBlockPlacementCodec() {
		super(PlayerBlockPlacementMessage.class, 0x0F);
	}

	@Override
	public PlayerBlockPlacementMessage decode(ChannelBuffer buffer) throws IOException {
		int x = buffer.readInt();
		int y = buffer.readUnsignedByte();
		int z = buffer.readInt();
		BlockFace direction = BlockFaces.BTEWNS.get(buffer.readUnsignedByte(), BlockFace.THIS);

		int id = buffer.readUnsignedShort();
		if (id == 0xFFFF) {
			return new PlayerBlockPlacementMessage(x, y, z, direction);
		}

		int count = buffer.readUnsignedByte();
		int damage = buffer.readShort();
		CompoundMap nbtData = null;
		if (ChannelBufferUtils.hasNbtData(id)) {
			nbtData = ChannelBufferUtils.readCompound(buffer);
		}
		return new PlayerBlockPlacementMessage(x, y, z, direction, id, count, damage, nbtData);
	}

	@Override
	public ChannelBuffer encode(PlayerBlockPlacementMessage message) throws IOException {
		int id = message.getId();

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getX());
		buffer.writeByte(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(BlockFaces.BTEWNS.indexOf(message.getDirection(), 255));
		buffer.writeShort(id);
		if (id != -1) {
			buffer.writeByte(message.getCount());
			buffer.writeShort(message.getDamage());
			if (ChannelBufferUtils.hasNbtData(id)) {
				ChannelBufferUtils.writeCompound(buffer, message.getNbtData());
			}
		}
		return buffer;
	}
}
