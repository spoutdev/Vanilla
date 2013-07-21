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

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.VanillaChannelBufferUtils;
import org.spout.vanilla.protocol.msg.player.PlayerBlockPlacementMessage;

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

		ItemStack heldItem = VanillaChannelBufferUtils.readItemStack(buffer);

		float dx = ((float) (buffer.readByte() & 0xFF)) / 16.0F;
		float dy = ((float) (buffer.readByte() & 0xFF)) / 16.0F;
		float dz = ((float) (buffer.readByte() & 0xFF)) / 16.0F;

		return new PlayerBlockPlacementMessage(x, y, z, direction, new Vector3(dx, dy, dz), heldItem, NullRepositionManager.getInstance());
	}

	@Override
	public ChannelBuffer encode(PlayerBlockPlacementMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getX());
		buffer.writeByte(message.getY());
		buffer.writeInt(message.getZ());
		buffer.writeByte(BlockFaces.BTEWNS.indexOf(message.getDirection(), 255));
		VanillaChannelBufferUtils.writeItemStack(buffer, message.getHeldItem());
		buffer.writeByte((int) (message.getFace().getX() * 16.0F));
		buffer.writeByte((int) (message.getFace().getY() * 16.0F));
		buffer.writeByte((int) (message.getFace().getZ() * 16.0F));
		return buffer;
	}
}
