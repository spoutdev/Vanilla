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

import java.util.Arrays;

import org.spout.api.protocol.Message;

public class MapDataMessage extends Message {
	private final short type, id;
	private final byte[] data;

	public MapDataMessage(short type, short id, byte[] data) {
		this.type = type;
		this.id = id;
		this.data = data;
	}

	public short getType() {
		return type;
	}

	public short getId() {
		return id;
	}

	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		return "MapDataMessage{type=" + type + ",id=" + ",data=" + Arrays.toString(data) + "}";
	}
}