/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.codec;

import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;

public final class CompressedChunkCodec extends MessageCodec<CompressedChunkMessage> {
	private static final int COMPRESSION_LEVEL = Deflater.BEST_SPEED;
	private static final int MAX_SECTIONS = 16;
	private static final int SIXTEEN_CUBED = 16 * 16 * 16;

	public CompressedChunkCodec() {
		super(CompressedChunkMessage.class, 0x33);
	}

	@Override
	public CompressedChunkMessage decode(ChannelBuffer buffer) throws IOException {
		int x = buffer.readInt();
		int z = buffer.readInt();
		boolean contiguous = buffer.readByte() == 1;

		short primaryBitMap = buffer.readShort();
		short addBitMap = buffer.readShort();
		int compressedSize = buffer.readInt();
		int unused = buffer.readInt();
		byte[] compressedData = new byte[compressedSize];
		buffer.readBytes(compressedData);

		boolean[] hasAdditionalData = new boolean[MAX_SECTIONS];
		byte[][] data = new byte[MAX_SECTIONS][];

		int size = 0;
		for (int i = 0; i < MAX_SECTIONS; ++i) {
			if ((primaryBitMap & 1 << i) != 0) { // This chunk exists! Let's initialize the data for it.
				int sectionSize = SIXTEEN_CUBED * 5 / 2;
				if ((addBitMap & 1 << i) != 0) {
					hasAdditionalData[i] = true;
					sectionSize += SIXTEEN_CUBED / 2;
				}

				data[i] = new byte[sectionSize];
				size += sectionSize;
			}
		}

		byte[] uncompressedData = new byte[size];

		Inflater inflater = new Inflater();
		inflater.setInput(compressedData);

		try {
			int uncompressed = inflater.inflate(uncompressedData);
			if (uncompressed == 0) {
				throw new IOException("Not all bytes uncompressed.");
			}
		} catch (DataFormatException e) {
			e.printStackTrace();
			throw new IOException("Bad compressed data.");
		} finally {
			inflater.end();
		}

		size = 0;
		for (byte[] sectionData : data) {
			if (sectionData != null && sectionData.length + size < uncompressedData.length) {
				System.arraycopy(uncompressedData, size, sectionData, 0, sectionData.length);
				size += sectionData.length;
			}
		}

		return new CompressedChunkMessage(x, z, contiguous, hasAdditionalData, unused, data);
	}

	@Override
	public ChannelBuffer encode(CompressedChunkMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		buffer.writeInt(message.getX());
		buffer.writeInt(message.getZ());
		buffer.writeByte(message.isContiguous() ? 1 : 0);
		short sectionsSentBitmap = 0;
		short additionalDataBitMap = 0;

		byte[][] data = message.getData();

		int uncompressedSize = 0;
		for (int i = 0; i < MAX_SECTIONS; ++i) {
			if (data[i] != null) { // This chunk exists! Let's initialize the data for it.
				sectionsSentBitmap |= 1 << i;
				if (message.hasAdditionalData()[i]) {
					additionalDataBitMap |= 1 << i;
				}
				uncompressedSize += data[i].length;
			}
		}

		buffer.writeShort(sectionsSentBitmap);
		buffer.writeShort(additionalDataBitMap);
		byte[] uncompressedData = new byte[uncompressedSize];
		int index = 0;
		for (byte[] sectionData : data) {
			if (sectionData != null) {
				System.arraycopy(sectionData, 0, uncompressedData, index, sectionData.length);
				index += sectionData.length;
			}
		}

		byte[] compressedData = new byte[uncompressedSize];

		Deflater deflater = new Deflater(COMPRESSION_LEVEL);
		deflater.setInput(uncompressedData);
		deflater.finish();


		int compressed = deflater.deflate(compressedData);
		try {
			if (compressed == 0) {
				throw new IOException("Not all bytes compressed.");
			}
		} finally {
			deflater.end();
		}

		buffer.writeInt(compressed);
		buffer.writeInt(message.getUnused());
		buffer.writeBytes(compressedData, 0, compressed);

		return buffer;
	}
}
