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

import org.spout.api.protocol.MessageCodec;
import org.spout.vanilla.protocol.msg.CompressedChunkMessage;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

public final class CompressedChunkCodec extends MessageCodec<CompressedChunkMessage> {
	private static final int COMPRESSION_LEVEL = Deflater.BEST_SPEED;

	public CompressedChunkCodec() {
		super(CompressedChunkMessage.class, 0x33);
	}

	@Override
	public CompressedChunkMessage decode(ChannelBuffer buffer) throws IOException {
		int x = buffer.readInt();
		int y = buffer.readShort();
		int z = buffer.readInt();

		int width = buffer.readByte() + 1;
		int depth = buffer.readByte() + 1;
		int height = buffer.readByte() + 1;

		int compressedSize = buffer.readInt();
		byte[] compressedData = new byte[compressedSize];
		buffer.readBytes(compressedData);

		byte[] data = new byte[(width * depth * height * 5) / 2];

		Inflater inflater = new Inflater();
		inflater.setInput(compressedData);

		try {
			int uncompressed = inflater.inflate(data);
			if (uncompressed == 0) {
				throw new IOException("Not all bytes uncompressed.");
			}
		} catch (DataFormatException e) {
			e.printStackTrace();
			throw new IOException("Bad compressed data.");
		} finally {
			inflater.end();
		}

		return new CompressedChunkMessage(x, y, z, width, height, depth, data);
	}

	@Override
	public ChannelBuffer encode(CompressedChunkMessage message) throws IOException {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();

		buffer.writeInt(message.getX());
		buffer.writeShort(message.getY());
		buffer.writeInt(message.getZ());

		buffer.writeByte(message.getWidth() - 1);
		buffer.writeByte(message.getDepth() - 1);
		buffer.writeByte(message.getHeight() - 1);

		byte[] data = message.getData();
		byte[] compressedData = new byte[(message.getWidth() * message.getDepth() * message.getHeight() * 5) / 2];

		Deflater deflater = new Deflater(COMPRESSION_LEVEL);
		deflater.setInput(data);
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
		buffer.writeBytes(compressedData, 0, compressed);

		return buffer;
	}
}