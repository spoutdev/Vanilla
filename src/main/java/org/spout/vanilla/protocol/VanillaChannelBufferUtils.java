/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.protocol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.math.GenericMath;
import static org.spout.api.util.ChannelBufferUtils.readCompound;
import static org.spout.api.util.ChannelBufferUtils.readString;
import static org.spout.api.util.ChannelBufferUtils.writeCompound;
import static org.spout.api.util.ChannelBufferUtils.writeString;
import org.spout.api.util.Parameter;

import org.spout.nbt.CompoundMap;

import org.spout.vanilla.material.VanillaMaterials;

public final class VanillaChannelBufferUtils {
	/**
	 * Writes a list of parameters (e.g. mob metadata) to the buffer.
	 * @param buf The buffer.
	 * @param parameters The parameters.
	 */
	@SuppressWarnings("unchecked")
	public static void writeParameters(ChannelBuffer buf, List<Parameter<?>> parameters) {
		for (Parameter<?> parameter : parameters) {
			int type = parameter.getType();
			int index = parameter.getIndex();
			if (index > 0x1F) {
				throw new IllegalArgumentException("Index has a maximum of 0x1F!");
			}

			buf.writeByte(type << 5 | index & 0x1F);

			switch (type) {
				case Parameter.TYPE_BYTE:
					buf.writeByte(((Parameter<Byte>) parameter).getValue());
					break;
				case Parameter.TYPE_SHORT:
					buf.writeShort(((Parameter<Short>) parameter).getValue());
					break;
				case Parameter.TYPE_INT:
					buf.writeInt(((Parameter<Integer>) parameter).getValue());
					break;
				case Parameter.TYPE_FLOAT:
					buf.writeFloat(((Parameter<Float>) parameter).getValue());
					break;
				case Parameter.TYPE_STRING:
					writeString(buf, ((Parameter<String>) parameter).getValue());
					break;
				case Parameter.TYPE_ITEM:
					ItemStack item = ((Parameter<ItemStack>) parameter).getValue();
					writeItemStack(buf, item);
					break;
			}
		}

		buf.writeByte(127);
	}

	/**
	 * Reads a list of parameters from the buffer.
	 * @param buf The buffer.
	 * @return The parameters.
	 */
	public static List<Parameter<?>> readParameters(ChannelBuffer buf) throws IOException {
		List<Parameter<?>> parameters = new ArrayList<Parameter<?>>();

		for (int b = buf.readUnsignedByte(); b != 127; b = buf.readUnsignedByte()) {
			int type = (b & 0xE0) >> 5;
			int index = b & 0x1F;

			switch (type) {
				case Parameter.TYPE_BYTE:
					parameters.add(new Parameter<Byte>(type, index, buf.readByte()));
					break;
				case Parameter.TYPE_SHORT:
					parameters.add(new Parameter<Short>(type, index, buf.readShort()));
					break;
				case Parameter.TYPE_INT:
					parameters.add(new Parameter<Integer>(type, index, buf.readInt()));
					break;
				case Parameter.TYPE_FLOAT:
					parameters.add(new Parameter<Float>(type, index, buf.readFloat()));
					break;
				case Parameter.TYPE_STRING:
					parameters.add(new Parameter<String>(type, index, readString(buf)));
					break;
				case Parameter.TYPE_ITEM:
					parameters.add(new Parameter<ItemStack>(type, index, readItemStack(buf)));
					break;
			}
		}

		return parameters;
	}

	public static ItemStack readItemStack(ChannelBuffer buffer) throws IOException {
		short id = buffer.readShort();
		if (id < 0) {
			return null;
		} else {
			Material material = VanillaMaterials.getMaterial(id);
			if (material == null) {
				throw new IOException("Unknown material with id of " + id);
			}
			int count = buffer.readUnsignedByte();
			int damage = buffer.readUnsignedShort();
			CompoundMap nbtData = readCompound(buffer);
			return new ItemStack(material, damage, count).setNBTData(nbtData);
		}
	}

	public static void writeItemStack(ChannelBuffer buffer, ItemStack item) {
		short id = item == null ? (short) -1 : VanillaMaterials.getMinecraftId(item.getMaterial());
		buffer.writeShort(id);
		if (id != -1) {
			buffer.writeByte(item.getAmount());
			buffer.writeShort(item.getData());
			writeCompound(buffer, item.getNBTData());
		}
	}

	public static int getShifts(int height) {
		if (height == 0) {
			return 0;
		}
		int shifts = 0;
		int tempVal = height;
		while (tempVal != 1) {
			tempVal >>= 1;
			++shifts;
		}
		return shifts;
	}

	public static int getExpandedHeight(int shift) {
		if (shift > 0 && shift < 12) {
			return 2 << shift;
		} else if (shift >= 32) {
			return shift;
		}
		return 256;
	}

	public static byte getNativeDirection(BlockFace face) {
		switch (face) {
			case SOUTH:
				return 3;
			case WEST:
				return 0;
			case NORTH:
				return 1;
			case EAST:
				return 2;
			default:
				return -1;
		}
	}

	public static int protocolifyPosition(float pos) {
		return GenericMath.floor(pos * 32);
	}

	public static float deProtocolifyPosition(int pos) {
		return pos / 32F;
	}

	public static int protocolifyPitch(float pitch) {
		return GenericMath.wrapByte(GenericMath.floor((pitch / 360) * 256));
	}

	public static float deProtocolifyPitch(int pitch) {
		return 360f * (pitch / 256f);
	}

	public static int protocolifyYaw(float yaw) {
		return protocolifyPitch(-yaw);
	}

	public static float deProtocolifyYaw(int yaw) {
		return -deProtocolifyPitch(yaw);
	}

	/**
	 * Create a SlotData stucture
	 * @param buf The buffer to decode the Slot field
	 * @return The according SlotData
	 */
	/**
	 * Default private constructor to prevent instantiation.
	 */
	private VanillaChannelBufferUtils() {
	}
}
