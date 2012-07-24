package org.spout.vanilla.protocol.msg.login.handshake;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.login.HandshakeMessage;

public class ClientHandshakeMessage extends HandshakeMessage {
	private final byte protoVersion;
	private final String username, hostname;
	private final int port;

	public ClientHandshakeMessage(byte protoVersion, String username, String hostname, int port) {
		this.protoVersion = protoVersion;
		this.username = username;
		this.hostname = hostname;
		this.port = port;
	}

	public byte getProtocolVersion() {
		return protoVersion;
	}

	public String getUsername() {
		return username;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("protocol version", protoVersion)
				.append("username", username)
				.append("hostname", hostname)
				.append("port", port)
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
		final ClientHandshakeMessage other = (ClientHandshakeMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.protoVersion, other.protoVersion)
				.append(this.username, other.username)
				.append(this.hostname, other.hostname)
				.append(this.port, other.port)
				.isEquals();
	}
}
