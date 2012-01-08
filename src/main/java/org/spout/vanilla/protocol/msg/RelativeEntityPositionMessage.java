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

import org.getspout.api.protocol.Message;

public final class RelativeEntityPositionMessage extends Message {
	private final int id, deltaX, deltaY, deltaZ;

	public RelativeEntityPositionMessage(int id, int deltaX, int deltaY, int deltaZ) {
		this.id = id;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.deltaZ = deltaZ;
	}

	public int getId() {
		return id;
	}

	public int getDeltaX() {
		return deltaX;
	}

	public int getDeltaY() {
		return deltaY;
	}

	public int getDeltaZ() {
		return deltaZ;
	}

	@Override
	public String toString() {
		return "RelativeEntityPositionMessage{id=" + id + ",deltaX=" + deltaX + ",deltaY=" + deltaY + ",deltaZ=" + deltaZ + "}";
	}
}