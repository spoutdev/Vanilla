/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.netcache;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Sets;

public class ChunkNetCache implements Serializable {
	
	private final byte[] partition = new byte[2048];
	private final Set<Long> hashSet;
	private volatile boolean cacheEnabled = false;
	
	public ChunkNetCache() {
		this(Sets.newSetFromMap(new ConcurrentHashMap<Long, Boolean>()));
	}
	
	public ChunkNetCache(Set<Long> hashSet) {
		this.hashSet = hashSet;
	}
	
	public boolean isCacheEnabled() {
		return cacheEnabled;
	}
	
	public void handleCustomPacket(String channel, byte[] array) {
		if (channel.equals("ChkCache:setHash")) {
			cacheEnabled = true;
			if (array != null) {
				DataInputStream din = new DataInputStream(new ByteArrayInputStream(array));
				try {
					while (true) {
						long hash = din.readLong();
						this.hashSet.add(hash);
					}
				} catch (IOException ee) {
				}
			}
		}
	}

	public byte[] handle(byte[] inflatedBuffer) {
		
		if (!cacheEnabled) {
			return inflatedBuffer;
		}
		
		int dataLength = inflatedBuffer.length;
		int segments = dataLength >> 11;
		if ((dataLength & 0x7FF) != 0) {
			segments++;
		}
		
		int newLength = dataLength + (segments << 3) + 8 + 4 + 1;
		
		byte[] newBuffer = new byte[newLength];
		
		for (int i = 0; i < segments; i++) {
			PartitionChunk.copyFromChunkData(inflatedBuffer, i, partition, inflatedBuffer.length);
			long hash = PartitionChunk.hash(partition);
			if (hashSet.add(hash)) {
				PartitionChunk.copyToChunkData(newBuffer, i, partition, dataLength);
			} else {
				PartitionChunk.setHash(newBuffer, i, hash, dataLength);
			}
		}
		long crc = PartitionChunk.hash(inflatedBuffer);
		PartitionChunk.setHash(newBuffer, 0, crc, newLength - 13);
		PartitionChunk.setInt(newBuffer, 0, dataLength, newLength - 5);
		
		return newBuffer;
		
	}
	
}
