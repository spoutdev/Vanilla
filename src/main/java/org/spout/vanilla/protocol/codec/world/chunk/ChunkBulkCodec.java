/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.codec.world.chunk;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.protocol.MessageCodec;
import org.spout.api.protocol.reposition.NullRepositionManager;

import org.spout.vanilla.protocol.msg.world.chunk.ChunkBulkMessage;

public final class ChunkBulkCodec extends MessageCodec<ChunkBulkMessage> {
	private static final int COMPRESSION_LEVEL = Deflater.BEST_SPEED;

	public ChunkBulkCodec() {
		super(ChunkBulkMessage.class, 0x38);
	}

	@Override
	public ChunkBulkMessage decode(ByteBuf buffer) throws IOException {
		int length = buffer.readShort() & 0xFFFF;

		int compressed = buffer.readInt();

		byte[] compressedDataFlat = new byte[compressed];

		buffer.readBytes(compressedDataFlat);

		int[] x = new int[length];
		int[] z = new int[length];
		byte[][][] data = new byte[length][][];
		boolean[][] hasAdd = new boolean[length][];
		byte[][] biomeData = new byte[length][];

		for (int i = 0; i < length; i++) {
			x[i] = buffer.readInt();
			z[i] = buffer.readInt();
			short primaryBitmap = buffer.readShort();
			hasAdd[i] = shortToBooleanArray(buffer.readShort());
			data[i] = shortToByteByteArray(primaryBitmap, hasAdd[i]);
			biomeData[i] = new byte[Chunk.BLOCKS.AREA];
		}

		int flatLength = 0;

		for (int i = 0; i < length; i++) {
			byte[][] columnData = data[i];
			for (int j = 0; j < columnData.length; j++) {
				flatLength += columnData[j].length;
			}
			flatLength += biomeData[i].length;
		}

		byte[] uncompressedDataFlat = new byte[flatLength];

		Inflater inflater = new Inflater();
		inflater.setInput(compressedDataFlat);
		inflater.getRemaining();
		try {
			int uncompressed = inflater.inflate(uncompressedDataFlat);
			if (uncompressed == 0) {
				throw new IOException("Not all bytes uncompressed.");
			} else if (uncompressed != uncompressedDataFlat.length) {
				throw new IOException("Wrong length for compressed data");
			}
		} catch (DataFormatException e) {
			e.printStackTrace();
			throw new IOException("Bad compressed data.");
		} finally {
			inflater.end();
		}

		int pos = 0;
		for (int i = 0; i < length; i++) {
			byte[][] columnData = data[i];
			for (int j = 0; j < columnData.length; j++) {
				int l = columnData[j].length;
				System.arraycopy(uncompressedDataFlat, pos, columnData[j], 0, l);
				pos += l;
			}
			int l = biomeData[i].length;
			System.arraycopy(uncompressedDataFlat, pos, biomeData[i], 0, l);
			pos += l;
		}

		if (pos != flatLength) {
			throw new IllegalStateException("Flat data length miscalculated");
		}

		return new ChunkBulkMessage(x, z, hasAdd, data, biomeData, NullRepositionManager.getInstance());
	}

	@Override
	public ByteBuf encode(ChunkBulkMessage message) throws IOException {
		ByteBuf buffer = Unpooled.buffer();

		int length = message.getX().length;
		buffer.writeShort(length);

		int dataLength = 0;
		byte[][][] uncompressedData = message.getData();
		byte[][] biomeData = message.getBiomeData();
		for (int i = 0; i < length; i++) {
			byte[][] uncompressedColumnData = uncompressedData[i];
			for (int j = 0; j < uncompressedColumnData.length; j++) {
				byte[] uncompressedChunkData = uncompressedColumnData[j];
				if (uncompressedChunkData != null) {
					dataLength += uncompressedChunkData.length;
				}
			}
			dataLength += biomeData[i].length;
		}

		byte[] uncompressedDataFlat = new byte[dataLength];
		int pos = 0;
		for (int i = 0; i < length; i++) {
			byte[][] uncompressedColumnData = uncompressedData[i];
			for (int j = 0; j < uncompressedColumnData.length; j++) {
				byte[] uncompressedChunkData = uncompressedColumnData[j];
				if (uncompressedChunkData != null) {
					int copyLength = uncompressedChunkData.length;
					System.arraycopy(uncompressedChunkData, 0, uncompressedDataFlat, pos, copyLength);
					pos += copyLength;
				}
			}
			System.arraycopy(biomeData[i], 0, uncompressedDataFlat, pos, biomeData[i].length);
			pos += biomeData[i].length;
		}

		if (pos != dataLength) {
			throw new IllegalStateException("Flat data length miscalculated");
		}

		Deflater deflater = new Deflater(COMPRESSION_LEVEL);
		deflater.setInput(uncompressedDataFlat);
		deflater.finish();

		byte[] compressedDataFlat = new byte[uncompressedDataFlat.length + 1024];

		int compressed = deflater.deflate(compressedDataFlat);
		try {
			if (compressed == 0) {
				throw new IOException("Not all bytes compressed.");
			}
		} finally {
			deflater.end();
		}

		buffer.writeInt(compressed);
		buffer.writeBytes(compressedDataFlat, 0, compressed);

		for (int i = 0; i < length; i++) {
			buffer.writeInt(message.getX()[i]);
			buffer.writeInt(message.getZ()[i]);
			buffer.writeShort(byteByteArrayToShort(message.getData()[i]));
			buffer.writeShort(booleanArrayToShort(message.hasAdditionalData()[i]));
		}

		return buffer;
	}

	private static short booleanArrayToShort(boolean[] array) {
		short s = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i]) {
				s |= 1 << i;
			}
		}
		return s;
	}

	private static boolean[] shortToBooleanArray(short s) {
		boolean[] array = new boolean[16];
		for (int i = 0; i < 16; i++) {
			if ((s & (1 << i)) != 0) {
				array[i] = true;
			} else {
				array[i] = false;
			}
		}
		return array;
	}

	private static short byteByteArrayToShort(byte[][] array) {
		short s = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i] != null) {
				s |= 1 << i;
			}
		}
		return s;
	}

	private static byte[][] shortToByteByteArray(short s, boolean[] add) {
		byte[][] array = new byte[16][];
		for (int i = 0; i < 16; i++) {
			if ((s & (1 << i)) != 0) {
				int length = 5 * Chunk.BLOCKS.HALF_VOLUME;
				if (add[i]) {
					length += Chunk.BLOCKS.HALF_VOLUME;
				}
				array[i] = new byte[length];
			} else {
				array[i] = null;
			}
		}
		return array;
	}
}
