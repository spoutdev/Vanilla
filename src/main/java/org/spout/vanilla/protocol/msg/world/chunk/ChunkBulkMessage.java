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
package org.spout.vanilla.protocol.msg.world.chunk;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class ChunkBulkMessage implements Message {
	private final int[] x;
	private final int[] z;
	private final boolean[][] addData;
	private final byte[][][] data;
	private final byte[][] biomeData;

	public ChunkBulkMessage(int[] x, int[] z, boolean[][] hasAdditionalData, byte[][][] data, byte[][] biomeData) {
		int l = x.length;
		if (l != z.length || l != hasAdditionalData.length || l != data.length || l != biomeData.length) {
			throw new IllegalArgumentException("The lengths of all bulk data arrays must be equal");
		}
		this.x = x;
		this.z = z;
		this.addData = hasAdditionalData;
		this.data = data;
		this.biomeData = biomeData;
	}

	public int[] getX() {
		return x;
	}

	public int[] getZ() {
		return z;
	}

	public boolean[][] hasAdditionalData() {
		return addData;
	}

	public byte[][][] getData() {
		return data;
	}

	public byte[][] getBiomeData() {
		return biomeData;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", x)
				.append("z", z)
				.append("hasAdditionalData", addData)
				.append("data", data, false)
				.append("biomeData", data, false)
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
		final ChunkBulkMessage other = (ChunkBulkMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.x, other.x)
				.append(this.z, other.z)
				.append(this.addData, other.addData)
				.append(this.data, other.data)
				.append(this.biomeData, other.biomeData)
				.isEquals();
	}
}
