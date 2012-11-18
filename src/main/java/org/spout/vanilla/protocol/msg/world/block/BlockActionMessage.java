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
package org.spout.vanilla.protocol.msg.world.block;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.VanillaBlockDataChannelMessage;

public final class BlockActionMessage extends VanillaBlockDataChannelMessage {
	private final int x, y, z;
	private final byte firstByte, secondByte;
	private short blockId;

	public BlockActionMessage(Block block, short blockId, byte firstByte, byte secondByte) {
		this(block.getX(), block.getY(), block.getZ(), blockId, firstByte, secondByte);
	}

	public BlockActionMessage(int x, int y, int z, short blockId, byte firstByte, byte secondByte) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.firstByte = firstByte;
		this.secondByte = secondByte;
		this.blockId = blockId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getFirstByte() {
		return firstByte;
	}

	public int getSecondByte() {
		return secondByte;
	}

	public short getBlockId() {
		return blockId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("firstByte", firstByte)
				.append("secondByte", secondByte)
				.append("blockId", blockId)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BlockActionMessage other = (BlockActionMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.firstByte, other.firstByte)
				.append(this.secondByte, other.secondByte)
				.append(this.blockId, other.blockId)
				.isEquals();
	}
}
