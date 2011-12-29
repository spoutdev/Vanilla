package org.getspout.vanilla.protocol.codec;

import java.io.IOException;
import java.util.Map;

import org.getspout.api.io.nbt.Tag;
import org.getspout.api.protocol.MessageCodec;
import org.getspout.unchecked.api.inventory.ItemStack;
import org.getspout.vanilla.protocol.ChannelBufferUtils;
import org.getspout.vanilla.protocol.msg.SetWindowSlotsMessage;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class SetWindowSlotsCodec extends MessageCodec<SetWindowSlotsMessage> {
	public SetWindowSlotsCodec() {
		super(SetWindowSlotsMessage.class, 0x68);
	}

	@Override
	public SetWindowSlotsMessage decode(ChannelBuffer buffer) throws IOException {
		int id = buffer.readUnsignedByte();
		int count = buffer.readUnsignedShort();
		ItemStack[] items = new ItemStack[count];
		for (int slot = 0; slot < count; slot++) {
			int item = buffer.readUnsignedShort();
			if (item == 0xFFFF) {
				items[slot] = null;
			} else {
				int itemCount = buffer.readUnsignedByte();
				int damage = buffer.readUnsignedByte();
				Map<String, Tag> nbtData = null;
				if (ChannelBufferUtils.hasNbtData(item)) {
					nbtData = ChannelBufferUtils.readCompound(buffer);
				}
				items[slot] = new ItemStack(item, itemCount, (short) damage, null, nbtData);
			}
		}
		return new SetWindowSlotsMessage(id, items);
	}

	@Override
	public ChannelBuffer encode(SetWindowSlotsMessage message) throws IOException {
		ItemStack[] items = message.getItems();

		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeByte(message.getId());
		buffer.writeShort(items.length);
		for (int slot = 0; slot < items.length; slot++) {
			ItemStack item = items[slot];
			if (item == null) {
				buffer.writeShort(-1);
			} else {
				buffer.writeShort(item.getTypeId());
				buffer.writeByte(item.getAmount());
				buffer.writeByte(item.getDurability());
				if (ChannelBufferUtils.hasNbtData(item.getTypeId())) {
					ChannelBufferUtils.writeCompound(buffer, item.getNbtData());
				}
			}
		}

		return buffer;
	}
}
