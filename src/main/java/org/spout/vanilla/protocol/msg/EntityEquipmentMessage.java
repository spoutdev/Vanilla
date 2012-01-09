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

import org.spout.api.protocol.Message;

public final class EntityEquipmentMessage extends Message {
	public static final int HELD_ITEM = 0;
	public static final int BOOTS_SLOT = 1;
	public static final int LEGGINGS_SLOT = 2;
	public static final int CHESTPLATE_SLOT = 3;
	public static final int HELMET_SLOT = 4;

	private final int id, slot, item, damage;

	public EntityEquipmentMessage(int id, int slot, int item, int damage) {
		this.id = id;
		this.slot = slot;
		this.item = item;
		this.damage = damage;
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

	public int getDamage() {
		return damage;
	}

	@Override
	public String toString() {
		return "EntityEquipmentMessage{id=" + id + ",slot=" + slot + ",item=" + item + ",damage" + damage +"}";
	}
}