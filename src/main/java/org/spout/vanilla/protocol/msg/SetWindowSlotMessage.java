/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.msg;

import java.util.Map;

import org.spout.api.io.nbt.Tag;
import org.spout.api.protocol.Message;

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
