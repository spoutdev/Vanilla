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

import org.spout.api.protocol.Message;

import java.util.Arrays;

public final class CompressedChunkMessage extends Message {
	private final int x, z;
	private final boolean contiguous;
	private final boolean[] hasAhhitionalData;
	private final int unused;
	private final byte[][] data;

	public CompressedChunkMessage(int x, int z, boolean contiguous, boolean[] hasAdditionalData, int unused, byte[][] data) {
		if (hasAdditionalData.length != data.length || data.length != 16) {
			throw new IllegalArgumentException("Data and hasAdditionalDta must have a length of 16");
		}
		this.x = x;
		this.z = z;
		this.contiguous = contiguous;
		this.hasAhhitionalData = hasAdditionalData;
		this.unused = unused;
		this.data = data;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public boolean[] hasAdditionalData() {
		return hasAhhitionalData;
	}

	public boolean isContiguous() {
		return contiguous;
	}
	
	public int getUnused() {
		return unused;
	}

	public byte[][] getData() {
		return data;
	}

	@Override
	public String toString() {
		return "CompressedChunkMessage{x=" + x + ",z=" + z + ",hasAdditionalData=" + Arrays.toString(hasAhhitionalData) + ",contiguous=" + contiguous + ",unused=" + unused + ",data=" + (data == null ? null : data.length) + "}";
	}
}
