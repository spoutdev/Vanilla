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

public final class DiggingMessage extends Message {
	public static final int STATE_START_DIGGING = 0;
	public static final int STATE_DONE_DIGGING = 2;
	public static final int STATE_DROP_ITEM = 4;

	private final int state, x, y, z, face;

	public DiggingMessage(int state, int x, int y, int z, int face) {
		this.state = state;
		this.x = x;
		this.y = y;
		this.z = z;
		this.face = face;
	}

	public int getState() {
		return state;
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

	public int getFace() {
		return face;
	}

	@Override
	public String toString() {
		return "DiggingMessage{state=" + state + ",x=" + x + ",y=" + y + ",z=" + z + ",face=" + face + "}";
	}
}