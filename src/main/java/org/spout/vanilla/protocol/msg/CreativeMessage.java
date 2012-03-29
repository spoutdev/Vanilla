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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.msg;

import java.util.List;

import org.spout.api.protocol.Message;

import org.spout.nbt.Tag;

public class CreativeMessage extends Message {
	private final short slot, id, amount, damage;
	private final List<Tag> nbtData;

	public CreativeMessage(short slot, short id, short amount, short damage, List<Tag> nbtData) {
		this.slot = slot;
		this.id = id;
		this.amount = amount;
		this.damage = damage;
		this.nbtData = nbtData;
	}

	public short getSlot() {
		return slot;
	}

	public short getId() {
		return id;
	}

	public short getAmount() {
		return amount;
	}

	public short getDamage() {
		return damage;
	}

	public List<Tag> getNbtData() {
		return nbtData;
	}

	@Override
	public String toString() {
		return "QuickBarMessage{slot=" + slot + ",id=" + id + ",amount=" + amount + ",damage=" + damage + ",nbtData=" + nbtData + "}";
	}
}
