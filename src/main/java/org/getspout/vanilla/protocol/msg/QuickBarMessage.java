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

public class QuickBarMessage extends Message {
	private final short slot, id, amount, damage;
	private final Map<String, Tag> nbtData;

	public QuickBarMessage(short slot, short id, short amount, short damage, Map<String, Tag> nbtData) {
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

	public Map<String, Tag> getNbtData() {
		return nbtData;
	}

	@Override
	public String toString() {
		return "QuickBarMessage{slot=" + slot + ",id=" + id + ",amount=" + amount + ",damage=" + damage + ",nbtData=" + nbtData + "}";
	}
}