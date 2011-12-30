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

import java.util.Arrays;

import org.getspout.api.protocol.Message;

public final class UpdateSignMessage extends Message {
	private final int x, y, z;
	private final String[] message;

	public UpdateSignMessage(int x, int y, int z, String[] message) {
		if (message.length != 4) {
			throw new IllegalArgumentException();
		}

		this.x = x;
		this.y = y;
		this.z = z;
		this.message = message;
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

	public String[] getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "UpdateSignMessage{x=" + x + ",y=" + y + ",z=" + z + ",message=" + Arrays.toString(message) + "}";
	}
}