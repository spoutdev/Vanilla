/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.protocol.msg.window;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.inventory.window.AbstractWindow;
import org.spout.vanilla.inventory.window.WindowType;

public final class WindowOpenMessage extends WindowMessage {
	private final int slots;
	private final String title;
	private final WindowType type;
	private final boolean useTitle;

	public WindowOpenMessage(AbstractWindow window, int slots) {
		this(window.getId(), window.getType(), window.getTitle(), slots, false);
	}

	public WindowOpenMessage(int windowInstanceId, WindowType type, String title, int slots, boolean useTitle) {
		super(windowInstanceId);
		this.type = type;
		this.title = title;
		this.slots = slots;
		this.useTitle = useTitle;
	}

	public WindowType getType() {
		return type;
	}

	public int getSlots() {
		return slots;
	}

	public String getTitle() {
		return title;
	}

	public boolean isUsingTitle() {
		return useTitle;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("id", this.getWindowInstanceId())
				.append("type", this.type)
				.append("slots", this.slots)
				.append("title", this.title)
				.append("usingTitle", this.useTitle)
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
		final WindowOpenMessage other = (WindowOpenMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getWindowInstanceId(), other.getWindowInstanceId())
				.append(this.type, other.type)
				.append(this.slots, other.slots)
				.append(this.title, other.title)
				.append(this.useTitle, other.useTitle)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.getWindowInstanceId())
				.append(this.type)
				.append(this.slots)
				.append(this.title)
				.append(this.useTitle)
				.toHashCode();
	}
}
