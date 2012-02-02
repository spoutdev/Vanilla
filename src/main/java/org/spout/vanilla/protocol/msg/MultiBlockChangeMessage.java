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
package org.spout.vanilla.protocol.msg;

import java.util.Arrays;

import org.spout.api.protocol.Message;

public final class MultiBlockChangeMessage extends Message {
	private final int chunkX, chunkZ;
	private final short[] coordinates;
	private final byte[] types, metadata;

	public MultiBlockChangeMessage(int chunkX, int chunkZ, short[] coordinates, byte[] types, byte[] metadata) {
		if (coordinates.length != types.length || types.length != metadata.length) {
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
		return coordinates.length;
	}

	public short[] getCoordinates() {
		return coordinates;
	}

	public byte[] getTypes() {
		return types;
	}

	public byte[] getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return "MultiBlockChangeMessage{chunkX=" + chunkX + ",chunkZ=" + chunkZ +
			",coordinates=" + Arrays.toString(coordinates) +
			",types=" + Arrays.toString(types) +
			",metadata=" + Arrays.toString(metadata) + "}";
	}
}
