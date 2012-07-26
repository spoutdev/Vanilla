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
package org.spout.vanilla.protocol.msg.login.request;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.login.LoginRequestMessage;

public final class ServerLoginRequestMessage extends Message {
	private final int id;
	private final byte dimension, mode, difficulty;
	private final String worldType;
	private final short maxPlayers;

	public ServerLoginRequestMessage(int id, String worldType, byte mode, byte dimension, byte difficulty, short maxPlayers) {
		this.id = id;
		this.worldType = worldType;
		this.mode = mode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
	}

	public int getId() {
		return id;
	}

	public String getWorldType() {
		return worldType;
	}

	public byte getGameMode() {
		return mode;
	}

	public byte getDimension() {
		return dimension;
	}

	public byte getDifficulty() {
		return difficulty;
	}

	public short getMaxPlayers() {
		return maxPlayers;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("worldType", worldType)
				.append("mode", mode)
				.append("dimension", dimension)
				.append("difficulty", difficulty)
				.append("maxPlayers", maxPlayers)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		final ServerLoginRequestMessage other = (ServerLoginRequestMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.worldType, other.worldType)
				.append(this.mode, other.mode)
				.append(this.dimension, other.dimension)
				.append(this.difficulty, other.difficulty)
				.append(this.maxPlayers, other.maxPlayers)
				.isEquals();
	}
}
