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

public final class AttachEntityMessage extends Message {
	private final int id, vehicle;

	public AttachEntityMessage(int id, int vehicle) {
		this.id = id;
		this.vehicle = vehicle;
	}

	public int getId() {
		return id;
	}

	public int getVehicle() {
		return vehicle;
	}

	@Override
	public String toString() {
		return "AttachEntityMessage{id=" + id + ",vehicle=" + vehicle + "}";
	}
}