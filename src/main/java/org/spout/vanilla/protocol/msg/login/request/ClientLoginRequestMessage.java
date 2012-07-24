package org.spout.vanilla.protocol.msg.login.request;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.login.LoginRequestMessage;

public final class ClientLoginRequestMessage extends LoginRequestMessage {
	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE).toString();
	}
}
