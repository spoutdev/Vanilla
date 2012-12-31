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

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.util.CharsetUtil;

import org.spout.api.Spout;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.api.math.MathHelper;
import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;
import org.spout.api.util.Parameter;

import org.spout.nbt.CompoundMap;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.Tag;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;

import org.spout.vanilla.material.VanillaMaterials;

public final class ChannelBufferUtils {
	/**
	 * Writes a list of parameters (e.g. mob metadata) to the buffer.
	 * @param buf The buffer.
	 * @param parameters The parameters.
	 */
	@SuppressWarnings("unchecked")
	public static void writeParameters(ChannelBuffer buf, List<Parameter<?>> parameters) throws IOException {
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

	/**
	 * Writes a string to the buffer.
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws IllegalArgumentException if the string is too long <em>after</em> it is encoded.
	 */
	public static void writeString(ChannelBuffer buf, String str) {
		int len = str.length();
		if (len >= 65536) {
			throw new IllegalArgumentException("String too long.");
		}
		buf.writeShort(len);
		for (int i = 0; i < len; ++i) {
			buf.writeChar(str.charAt(i));
		}
	}

	/**
	 * Writes a UTF-8 string to the buffer.
	 * @param buf The buffer.
	 * @param str The string.
	 * @throws UnsupportedEncodingException if the encoding isn't supported.
	 * @throws IllegalArgumentException if the string is too long <em>after</em> it is encoded.
	 */
	public static void writeUtf8String(ChannelBuffer buf, String str) throws UnsupportedEncodingException {
		byte[] bytes = str.getBytes(CharsetUtil.UTF_8);
		if (bytes.length >= 65536) {
			throw new IllegalArgumentException("Encoded UTF-8 string too long.");
		}

		buf.writeShort(bytes.length);
		buf.writeBytes(bytes);
	}

	/**
	 * Reads a string from the buffer.
	 * @param buf The buffer.
	 * @return The string.
	 */
	public static String readString(ChannelBuffer buf) {
		int len = buf.readUnsignedShort();

		char[] characters = new char[len];
		for (int i = 0; i < len; i++) {
			characters[i] = buf.readChar();
		}

		return new String(characters);
	}

	/**
	 * Reads a UTF-8 encoded string from the buffer.
	 * @param buf The buffer.
	 * @return The string.
	 * @throws UnsupportedEncodingException if the encoding isn't supported.
	 */
	public static String readUtf8String(ChannelBuffer buf) throws UnsupportedEncodingException {
		int len = buf.readUnsignedShort();

		byte[] bytes = new byte[len];
		buf.readBytes(bytes);

		return new String(bytes, CharsetUtil.UTF_8);
	}

	public static CompoundMap readCompound(ChannelBuffer buf) {
		int len = buf.readShort();
		if (len > 0) {
			byte[] bytes = new byte[len];
			buf.readBytes(bytes);
			NBTInputStream str = null;
			try {
				str = new NBTInputStream(new ByteArrayInputStream(bytes));
				Tag tag = str.readTag();
				if (tag instanceof CompoundTag) {
					return ((CompoundTag) tag).getValue();
				}
			} catch (IOException e) {
			} finally {
				if (str != null) {
					try {
						str.close();
					} catch (IOException e) {
					}
				}
			}
		}
		return null;
	}

	public static void writeCompound(ChannelBuffer buf, CompoundMap data) {
		if (data == null) {
			buf.writeShort(-1);
			return;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		NBTOutputStream str = null;
		try {
			str = new NBTOutputStream(out);
			str.writeTag(new CompoundTag("", data));
			str.close();
			str = null;
			buf.writeShort(out.size());
			buf.writeBytes(out.toByteArray());
		} catch (IOException e) {
		} finally {
			if (str != null) {
				try {
					str.close();
				} catch (IOException e) {
				}
			}
		}
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

	public static void writeItemStack(ChannelBuffer buffer, ItemStack item) throws IOException {
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

	public static Vector3 readVector3(ChannelBuffer buf) {
		float x = buf.readFloat();
		float y = buf.readFloat();
		float z = buf.readFloat();
		return new Vector3(x, y, z);
	}

	public static void writeVector3(Vector3 vec, ChannelBuffer buf) {
		buf.writeFloat(vec.getX());
		buf.writeFloat(vec.getY());
		buf.writeFloat(vec.getZ());
	}

	public static Vector2 readVector2(ChannelBuffer buf) {
		float x = buf.readFloat();
		float z = buf.readFloat();
		return new Vector2(x, z);
	}

	public static void writeVector2(Vector2 vec, ChannelBuffer buf) {
		buf.writeFloat(vec.getX());
		buf.writeFloat(vec.getY());
	}

	public static Color readColor(ChannelBuffer buf) {
		int argb = buf.readInt();
		return new Color(argb);
	}

	public static void writeColor(Color color, ChannelBuffer buf) {
		buf.writeInt(color.getRGB());
	}

	public static int protocolifyPosition(float pos) {
		return MathHelper.floor(pos * 32);
	}

	public static float deProtocolifyPosition(int pos) {
		return pos / 32F;
	}

	public static int protocolifyPitch(float pitch) {
		return MathHelper.wrapByte(MathHelper.floor((pitch / 360) * 256));
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
	private ChannelBufferUtils() {
	}
}
