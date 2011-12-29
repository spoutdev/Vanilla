package org.getspout.vanilla.protocol.codec;

import java.io.IOException;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.vanilla.protocol.msg.AnimateEntityMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class AnimateEntityCodec extends MessageCodec<AnimateEntityMessage> {
	public AnimateEntityCodec() {
		super(AnimateEntityMessage.class, 0x12);
	}

	@Override
	public AnimateEntityMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		int animation = buffer.readUnsignedByte();
		return new AnimateEntityMessage(id, animation);
	}

	@Override
	public ChannelBuffer encode(AnimateEntityMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.buffer(5);
		buffer.writeInt(message.getId());
		buffer.writeByte(message.getAnimation());
		return buffer;
	}
}
