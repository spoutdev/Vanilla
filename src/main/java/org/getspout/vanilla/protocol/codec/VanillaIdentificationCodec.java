package org.getspout.vanilla.protocol.codec;

import org.getspout.api.protocol.MessageCodec;
import org.getspout.api.protocol.bootstrap.msg.BootstrapIdentificationMessage;
import org.getspout.api.protocol.notch.ChannelBufferUtils;
import org.getspout.vanilla.protocol.msg.VanillaIdentificationMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class VanillaIdentificationCodec extends MessageCodec<VanillaIdentificationMessage> {
	public VanillaIdentificationCodec() {
		super(VanillaIdentificationMessage.class, 0x01);
	}

	@Override
	public VanillaIdentificationMessage decode(ChannelBuffer buffer) {
		int version = buffer.readInt();
		String name = ChannelBufferUtils.readString(buffer);
		long seed = buffer.readLong();
		int mode = buffer.readInt();
		int dimension = buffer.readByte();
		int difficulty = buffer.readByte();
		int worldHeight = ChannelBufferUtils.getExpandedHeight(buffer.readByte());
		int maxPlayers = buffer.readByte();
		return new VanillaIdentificationMessage(version, name, seed, mode, dimension, difficulty, worldHeight, maxPlayers);
	}

	@Override
	public ChannelBuffer encode(VanillaIdentificationMessage message) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeInt(message.getId());
		ChannelBufferUtils.writeString(buffer, message.getName());
		buffer.writeLong(message.getSeed());
		buffer.writeInt(message.getGameMode());
		buffer.writeByte(message.getDimension());
		buffer.writeByte(message.getDifficulty());
		buffer.writeByte(ChannelBufferUtils.getShifts(message.getWorldHeight()) - 1);
		buffer.writeByte(message.getMaxPlayers());
		return buffer;
	}
}
