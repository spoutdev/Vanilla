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

public final class EntityActionMessage extends Message {
	public static final int ACTION_SNEAKING = 1;
	public static final int ACTION_STOP_SNEAKING = 2;
	public static final int ACTION_LEAVE_BED = 3;

	private final int id, action;

	public EntityActionMessage(int id, int action) {
		this.id = id;
		this.action = action;
	}

	public int getId() {
		return id;
	}

	public int getAction() {
		return action;
	}

	@Override
	public String toString() {
		return "EntityActionMessage{id=" + id + ",action=" + action + "}";
	}
}