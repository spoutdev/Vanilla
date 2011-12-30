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

import org.getspout.api.protocol.Message;

public final class SpawnItemMessage extends Message {
	private final int id, x, y, z, rotation, pitch, roll;
	private final int itemId, count;
	private final short damage;

	public SpawnItemMessage(int id, int itemId, int count, short damage, int x, int y, int z, int rotation, int pitch, int roll) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
		this.pitch = pitch;
		this.roll = roll;
		this.itemId = itemId;
		this.count = count;
		this.damage = damage;
	}

	public int getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getRotation() {
		return rotation;
	}

	public int getPitch() {
		return pitch;
	}

	public int getRoll() {
		return roll;
	}

	public int getItemId() {
		return itemId;
	}

	public int getCount() {
		return count;
	}

	public short getDamage() {
		return damage;
	}

	@Override
	public String toString() {
		return "SpawnItemMessage{id=" + id + ",item=[" + itemId + "," + count + "," + damage + "],x=" + x + ",y=" + y + ",z=" + z + ",rotation=" + rotation + ",pitch=" + pitch + ",roll=" + roll + "}";
	}
}