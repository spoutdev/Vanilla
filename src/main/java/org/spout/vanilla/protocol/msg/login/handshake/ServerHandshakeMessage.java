package org.spout.vanilla.protocol.msg.login.handshake;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.login.HandshakeMessage;

public class ServerHandshakeMessage extends HandshakeMessage {
	private final String hash;

	public ServerHandshakeMessage(String hash) {
		this.hash = hash;
	}

	public String getHash() {
		return hash;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("hash", hash)
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
		final ServerHandshakeMessage other = (ServerHandshakeMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.hash, other.hash)
				.isEquals();
	}
}
