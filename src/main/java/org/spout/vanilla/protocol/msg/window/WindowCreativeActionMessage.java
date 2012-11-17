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
package org.spout.vanilla.protocol.msg.window;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.spout.api.inventory.ItemStack;
import org.spout.api.util.SpoutToStringStyle;
import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public class WindowCreativeActionMessage extends VanillaMainChannelMessage {
	private final short slot;
	private final ItemStack item;

	public WindowCreativeActionMessage(short slot, ItemStack item) {
		this.slot = slot;
		this.item = item;
	}

	public short getSlot() {
		return slot;
	}

	public ItemStack get() {
		return item;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("slot", slot)
				.append("item", item)
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
		final WindowCreativeActionMessage other = (WindowCreativeActionMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.slot, other.slot)
				.append(this.item, other.item)
				.isEquals();
	}
}
