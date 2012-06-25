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
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class MultiBlockChangeMessage extends Message {
	private final int chunkX, chunkZ;
	private final short[] coordinates;
	private final short[] types;
	private final byte[] metadata;

	/**
	 * Creates a MultiBlockChangeMessage using the specified data
	 * @param chunkX The x coordinate of the chunk containing these changes
	 * @param chunkZ The z coordinate of the chunk containing these changes
	 * @param coordinates An array of change coordinates. Length should be 3 * types.length, with coordinates in x y z format
	 * @param types An array of block types
	 * @param metadata An array of block metadata. No more than a nibble per entry
	 */
	public MultiBlockChangeMessage(int chunkX, int chunkZ, short[] coordinates, short[] types, byte[] metadata) {
		if (coordinates.length != (types.length * 3) || types.length != metadata.length) {
			throw new IllegalArgumentException();
		}

		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.coordinates = coordinates;
		this.types = types;
		this.metadata = metadata;
	}

	public int getChunkX() {
		return chunkX;
	}

	public int getChunkZ() {
		return chunkZ;
	}

	public int getChanges() {
		return types.length;
	}

	public short[] getCoordinates() {
		return coordinates;
	}

	public short[] getTypes() {
		return types;
	}

	public byte[] getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("chunkX", chunkX)
				.append("chunkZ", chunkZ)
				.append("coordinates", coordinates, false)
				.append("types", types, false)
				.append("metadata", metadata, false)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final MultiBlockChangeMessage other = (MultiBlockChangeMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.chunkX, other.chunkX)
				.append(this.chunkZ, other.chunkZ)
				.append(this.coordinates, other.coordinates)
				.append(this.types, other.types)
				.append(this.metadata, other.metadata)
				.isEquals();
	}
}
