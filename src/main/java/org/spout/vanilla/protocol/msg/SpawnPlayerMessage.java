/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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

import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;

public final class SpawnPlayerMessage extends Message {
	private final int id, x, y, z, yaw, pitch, item;
	private final String name;

	public SpawnPlayerMessage(int id, String name, Vector3 position, int yaw, int pitch, int item) {
		this(id, name, (int) position.getX(), (int) position.getY(), (int) position.getZ(), yaw, pitch, item);
	}

	public SpawnPlayerMessage(int id, String name, int x, int y, int z, int yaw, int pitch, int item) {
		this.id = id;
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.item = item;
	}

	public int getEntityId() {
		return id;
	}

	public String getPlayerName() {
		return name;
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

	public int getYaw() {
		return yaw;
	}

	public int getPitch() {
		return pitch;
	}

	public int getItemId() {
		return item;
	}

	@Override
	public String toString() {
		return "SpawnPlayerMessage{id=" + id + ",name=" + name + ",x=" + x + ",y=" + y + ",z=" + z + ",rotation=" + yaw + ",pitch=" + pitch + ",item=" + item + "}";
	}
}
