package org.getspout.vanilla.protocol.msg;

import org.getspout.api.protocol.Message;

public class VanillaPingMessage extends Message {
	private final int pingId;

	public VanillaPingMessage(int pingId) {
		this.pingId = pingId;
	}

	public int getPingId() {
		return pingId;
	}

	@Override
	public String toString() {
		return "PingMessage{id=" + pingId + "}";
	}
}