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

import java.util.Map;

import org.getspout.api.io.nbt.Tag;
import org.getspout.api.protocol.Message;

public final class SetWindowSlotMessage extends Message {
	private final int id, slot, item, count, damage;
	private final Map<String, Tag> nbtData;

	public SetWindowSlotMessage(int id, int slot) {
		this(id, slot, -1, 0, 0, null);
	}

	public SetWindowSlotMessage(int id, int slot, int item, int count, int damage, Map<String, Tag> nbtData) {
		this.id = id;
		this.slot = slot;
		this.item = item;
		this.count = count;
		this.damage = damage;
		this.nbtData = nbtData;
	}

	public int getId() {
		return id;
	}

	public int getSlot() {
		return slot;
	}

	public int getItem() {
		return item;
	}

	public int getCount() {
		return count;
	}

	public int getDamage() {
		return damage;
	}

	public Map<String, Tag> getNbtData() {
		return nbtData;
	}

	@Override
	public String toString() {
		return "SetWindowSlotMessage{id=" + id + ",slot=" + slot + ",item=" + item + ",count=" + count + ",damage=" + damage + ",nbtData=" + nbtData + "}";
	}
}