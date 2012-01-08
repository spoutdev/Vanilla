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
package org.getspout.vanilla.protocol.msg;

import org.getspout.api.protocol.Message;

public final class LoadChunkMessage extends Message {
	private final int x, z;
	private final boolean loaded;

	public LoadChunkMessage(int x, int z, boolean loaded) {
		this.x = x;
		this.z = z;
		this.loaded = loaded;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public boolean isLoaded() {
		return loaded;
	}

	@Override
	public String toString() {
		return "LoadChunkMessage{x=" + x + ",z=" + z + ",loaded=" + loaded + "}";
	}
}