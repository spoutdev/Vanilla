package org.getspout.vanilla.protocol.codec;

import java.io.IOException;
import java.util.List;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.api.util.Parameter;
import org.getspout.vanilla.protocol.ChannelBufferUtils;
import org.getspout.vanilla.protocol.msg.EntityMetadataMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class EntityMetadataCodec extends MessageCodec<EntityMetadataMessage> {
	public EntityMetadataCodec() {
		super(EntityMetadataMessage.class, 0x28);
	}

	@Override
	public EntityMetadataMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readInt();
		List<Parameter<?>> parameters = ChannelBufferUtils.readParameters(buffer);
		return new EntityMetadataMessage(id, parameters);
	}

	@Override
	public ChannelBuffer encode(EntityMetadataMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getId());
		ChannelBufferUtils.writeParameters(buffer, message.getParameters());
		return buffer;
	}
}
