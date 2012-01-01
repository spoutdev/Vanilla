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

import org.getspout.api.inventory.ItemStack;
import org.getspout.api.protocol.Message;

public final class SetWindowSlotsMessage extends Message {
	private final int id;
	private final ItemStack[] items;

	public SetWindowSlotsMessage(int id, ItemStack[] items) {
		this.id = id;
		this.items = items;
	}

	public int getId() {
		return id;
	}

	public ItemStack[] getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "SetWindowSlotsMessage{id=" + id + ",slots=" + Arrays.toString(items) + "}";
	}
}