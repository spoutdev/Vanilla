package org.getspout.vanilla.protocol.codec;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.api.protocol.bootstrap.msg.BootstrapPingMessage;
import org.getspout.vanilla.protocol.msg.VanillaPingMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class VanillaPingCodec extends MessageCodec<VanillaPingMessage> {
	public VanillaPingCodec() {
		super(VanillaPingMessage.class, 0xFE);
	}

	@Override
	public VanillaPingMessage decode(ChannelBuffer buffer) {
		int id = buffer.readInt();
		return new VanillaPingMessage(id);
	}

	@Override
	public ChannelBuffer encode(VanillaPingMessage message) {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		buffer.writeInt(message.getPingId());
		return buffer;
	}
}
