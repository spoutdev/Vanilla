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

public class ExperienceOrbMessage extends Message {
	private final int id, x, y, z;
	private final short count;

	public ExperienceOrbMessage(int id, int x, int y, int z, short count) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.count = count;
	}

	public int getId() {
		return id;
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

	public short getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "ExperienceOrbMessage{id=" + id + ",x=" + x + ",y=" + y + ",z=" + z + ",count=" + count + "}";
	}
}