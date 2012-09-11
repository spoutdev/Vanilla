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
package org.spout.vanilla.protocol.msg.player;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class PlayerBlockPlacementMessage implements Message {
	private final int x, y, z;
	private Vector3 face;
	private BlockFace direction;
	private ItemStack heldItem;

	public PlayerBlockPlacementMessage(int x, int y, int z, BlockFace direction, Vector3 face) {
		this(x, y, z, direction, face, null);
	}

	public PlayerBlockPlacementMessage(int x, int y, int z, BlockFace direction, Vector3 face, ItemStack heldItem) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
		this.direction = direction;
		this.heldItem = heldItem;
	}

	public ItemStack getHeldItem() {
		return this.heldItem;
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

	public BlockFace getDirection() {
		return direction;
	}

	public Vector3 getFace() {
		return this.face;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("direction", direction)
				.append("face", face)
				.append("heldItem", heldItem)
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
		final PlayerBlockPlacementMessage other = (PlayerBlockPlacementMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.direction, other.direction)
				.append(this.face, other.face)
				.append(this.heldItem, other.heldItem)
				.isEquals();
	}
}
