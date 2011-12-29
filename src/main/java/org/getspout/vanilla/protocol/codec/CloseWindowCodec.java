package org.getspout.vanilla.protocol.codec;

import java.io.IOException;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.vanilla.protocol.msg.CloseWindowMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class CloseWindowCodec extends MessageCodec<CloseWindowMessage> {
	public CloseWindowCodec() {
		super(CloseWindowMessage.class, 0x65);
	}

	@Override
	public CloseWindowMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		return new CloseWindowMessage(id);
	}

	@Override
	public ChannelBuffer encode(CloseWindowMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(1);
		buffer.writeByte(message.getId());
		return buffer;
	}
}
