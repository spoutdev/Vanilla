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
package org.spout.vanilla.protocol.msg.player;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.material.block.BlockFace;
import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public final class PlayerDiggingMessage extends VanillaMainChannelMessage {
	public static final int STATE_START_DIGGING = 0;
	public static final int STATE_CANCEL_DIGGING = 1;
	public static final int STATE_DONE_DIGGING = 2;
	public static final int STATE_UPDATE_BLOCK = 3;//This should be ignored - the client does not use it, and it could be abused.
	public static final int STATE_DROP_ITEM = 4;
	public static final int STATE_SHOOT_ARROW_EAT_FOOD = 5;
	private final int state, x, y, z;
	private final BlockFace face;

	public PlayerDiggingMessage(int state, int x, int y, int z, BlockFace face, RepositionManager rm) {
		this.state = state;
		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
		this.face = face;
	}

	public int getState() {
		return state;
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

	public BlockFace getFace() {
		return face;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("state", state)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("face", face)
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
		final PlayerDiggingMessage other = (PlayerDiggingMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.state, other.state)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.face, other.face)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.state)
				.append(this.x)
				.append(this.y)
				.append(this.z)
				.append(this.face)
				.toHashCode();
	}
}
