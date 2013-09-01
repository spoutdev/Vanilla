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

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.data.GameMode;
import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public final class PlayerGameStateMessage extends VanillaMainChannelMessage {
	public static final byte INVALID_BED = 0;
	public static final byte BEGIN_RAINING = 1;
	public static final byte END_RAINING = 2;
	public static final byte CHANGE_GAME_MODE = 3;
	public static final byte ENTER_CREDITS = 4;
	private final byte reason;
	private final GameMode gameMode;

	public PlayerGameStateMessage(byte reason, GameMode gameMode) {
		this.reason = reason;
		this.gameMode = gameMode;
	}

	public PlayerGameStateMessage(byte reason) {
		this(reason, GameMode.CREATIVE);
	}

	public byte getReason() {
		return reason;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("reason", reason)
				.append("gameMode", gameMode)
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
		final PlayerGameStateMessage other = (PlayerGameStateMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.reason, other.reason)
				.append(this.gameMode, other.gameMode)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.reason)
				.append(this.gameMode)
				.toHashCode();
	}
}
