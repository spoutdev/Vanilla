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

public class UserListItemMessage extends Message {
	private final String name;
	private final boolean addOrRemove;
	private final short ping;

	public UserListItemMessage(String name, boolean addOrRemove, short ping) {
		this.name = name;
		this.addOrRemove = addOrRemove;
		this.ping = ping;
	}

	public String getName() {
		return name;
	}

	public boolean addOrRemove() {
		return addOrRemove;
	}

	public short getPing() {
		return ping;
	}

	@Override
	public String toString() {
		return "UserListItemMessage{name=" + name + ",addOrRemove=" + addOrRemove +  ",ping=" + ping + "}";
	}
}