/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public final class OpenWindowMessage extends Message {
	private final int id, type, slots;
	private final String title;

	public OpenWindowMessage(int id, int type, String title, int slots) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.slots = slots;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getSlots() {
		return slots;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", id)
				.append("type", type)
				.append("slots", slots)
				.append("title", title)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final OpenWindowMessage other = (OpenWindowMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.id, other.id)
				.append(this.type, other.type)
				.append(this.slots, other.slots)
				.append(this.title, other.title)
				.isEquals();
	}
}
