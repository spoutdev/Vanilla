/*
 * This file is part of Vanilla (http://www.spout.org/).
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
package org.spout.vanilla.protocol.msg;

import org.spout.api.protocol.Message;

public final class SpawnLightningStrikeMessage extends Message {
	private final int id, mode, x, y, z;

	public SpawnLightningStrikeMessage(int id, int x, int y, int z) {
		this(id, 1, x, y, z);
	}

	public SpawnLightningStrikeMessage(int id, int mode, int x, int y, int z) {
		this.id = id;
		this.mode = mode;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getId() {
		return id;
	}

	public int getMode() {
		return mode;
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

	@Override
	public String toString() {
		return "SpawnLightningStrikeMessage{id=" + id + ",mode=" + mode + ",x=" + x + ",y=" + y + ",z=" + z + "}";
	}
}