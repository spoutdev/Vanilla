package org.spout.vanilla.protocol.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.ClientStatusMessage;

import java.io.IOException;

public final class ClientStatusCodec extends MessageCodec<ClientStatusMessage> {
    public ClientStatusCodec() {
        super(ClientStatusMessage.class, 0xCD);
    }

    @Override
    public ClientStatusMessage decode(ChannelBuffer buffer) throws IOException {
        byte status = buffer.readByte();
        return new ClientStatusMessage(status);
    }

    @Override
    public ChannelBuffer encode(ClientStatusMessage message) throws IOException {
        ChannelBuffer buffer = ChannelBuffers.buffer(1);
        buffer.writeByte(message.getStatus());
        return buffer;
    }
}
