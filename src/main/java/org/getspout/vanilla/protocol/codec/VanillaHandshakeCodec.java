package org.getspout.vanilla.protocol.codec;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.api.protocol.bootstrap.msg.BootstrapHandshakeMessage;
import org.getspout.api.protocol.notch.ChannelBufferUtils;
import org.getspout.vanilla.protocol.msg.VanillaHandshakeMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class VanillaHandshakeCodec extends MessageCodec<VanillaHandshakeMessage> {
	public VanillaHandshakeCodec() {
		super(VanillaHandshakeMessage.class, 0x02);
	}

	@Override
	public VanillaHandshakeMessage decode(ChannelBuffer buffer) {
		String identifier = ChannelBufferUtils.readString(buffer);
		return new VanillaHandshakeMessage(identifier);
	}

	@Override
	public ChannelBuffer encode(VanillaHandshakeMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		ChannelBufferUtils.writeString(buffer, message.getIdentifier());
		return buffer;
	}
}
