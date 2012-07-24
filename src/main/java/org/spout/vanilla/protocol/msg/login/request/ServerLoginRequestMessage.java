package org.spout.vanilla.protocol.msg.login.request;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.login.LoginRequestMessage;

public final class ServerLoginRequestMessage extends LoginRequestMessage {
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
