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

public final class SpawnPaintingMessage extends Message {
	private final int id, x, y, z, type;
	private final String title;

	public SpawnPaintingMessage(int id, String title, int x, int y, int z, int type) {
		this.id = id;
		this.title = title;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
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

	public int getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "SpawnPaintingMessage{id=" + id + ",x=" + x + ",y=" + y + ",z=" + z + ",type=" + type + ",title=" + title + "}";
	}
}