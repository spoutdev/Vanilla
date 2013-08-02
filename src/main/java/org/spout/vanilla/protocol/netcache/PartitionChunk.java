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
package org.spout.vanilla.protocol.netcache;

public class PartitionChunk {
	public static void copyToChunkData(byte[] chunkData, int blockNum, byte[] partition, int dataLength) {

		int j = blockNum << 11;

		boolean clear = partition == null;

		if (clear) {
			for (int i = 0; i < 2048 && j < dataLength; i++) {
				chunkData[j++] = 0;
			}
		} else {
			for (int i = 0; i < 2048 && j < dataLength; i++) {
				chunkData[j++] = partition[i];
			}
		}
	}

	public static void copyFromChunkData(byte[] chunkData, int blockNum, byte[] partition, int dataLength) {

		int j = blockNum << 11;

		int i = 0;
		for (i = 0; i < 2048 && j < dataLength; i++) {
			partition[i] = chunkData[j++];
		}
		for (; i < 2048; i++) {
			partition[i] = 0;
		}
	}

	public static long getHash(byte[] chunkData, int blockNum, int base) {
		int p = blockNum * 8 + base;
		long hash = 0;
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		return hash;
	}

	public static void setHash(byte[] chunkData, int blockNum, long hash, int base) {
		int p = blockNum * 8 + base;
		chunkData[p++] = (byte) (hash >> 56);
		chunkData[p++] = (byte) (hash >> 48);
		chunkData[p++] = (byte) (hash >> 40);
		chunkData[p++] = (byte) (hash >> 32);
		chunkData[p++] = (byte) (hash >> 24);
		chunkData[p++] = (byte) (hash >> 16);
		chunkData[p++] = (byte) (hash >> 8);
		chunkData[p++] = (byte) (hash >> 0);
	}

	public static int getInt(byte[] chunkData, int blockNum, int base) {
		int p = blockNum * 4 + base;
		int hash = 0;
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		return hash;
	}

	public static void setInt(byte[] chunkData, int blockNum, int hash, int base) {
		int p = blockNum * 4 + base;
		chunkData[p++] = (byte) (hash >> 24);
		chunkData[p++] = (byte) (hash >> 16);
		chunkData[p++] = (byte) (hash >> 8);
		chunkData[p++] = (byte) (hash >> 0);
	}

	public static long hash(final byte[] a) {
		return hash(a, 0, a.length);
	}

	public static long hash(final byte[] a, final int off, final int len) {
		long h = 1;
		int end = off + len;
		for (int i = off; i < end; i++) {
			h += (h << 5) + (long) a[i];
		}
		return h;
	}
}
