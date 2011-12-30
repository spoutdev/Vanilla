/*
 * This file is part of Vanilla (http://www.getspout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.getspout.vanilla.protocol.msg;

import org.getspout.api.protocol.Message;

public final class BlockChangeMessage extends Message {
	private final int x, y, z, type, metadata;

	public BlockChangeMessage(int x, int y, int z, int type, int metadata) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
		this.metadata = metadata;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getType() {
		return type;
	}

	public int getMetadata() {
		return metadata;
	}

	@Override
	public String toString() {
		return "BlockChangeMessage{x=" + x + ",y=" + y +",z=" + z + ",type=" + type + ",metadata=" + metadata + "}";
	}
}