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

import org.spout.api.math.Vector3;
import org.spout.api.protocol.Message;

public final class SpawnVehicleMessage extends Message {
	private final int id, type, x, y, z, fireballId, fireballX, fireballY, fireballZ;

	public SpawnVehicleMessage(int id, int type, Vector3 pos) {
		this(id, type, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ());
	}

	public SpawnVehicleMessage(int id, int type, int x, int y, int z) {
		this(id, type, x, y, z, 0, 0, 0, 0);
	}

	public SpawnVehicleMessage(int id, int type, Vector3 pos, int fbId, Vector3 fbPos) {
		this(id, type, (int) pos.getX(), (int) pos.getY(), (int) pos.getZ(), fbId, (int) fbPos.getX(), (int) fbPos.getY(), (int) fbPos.getZ());
	}

	public SpawnVehicleMessage(int id, int type, int x, int y, int z, int fbId, int fbX, int fbY, int fbZ) {
		this.id = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		fireballId = fbId;
		fireballX = fbX;
		fireballY = fbY;
		fireballZ = fbZ;
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
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

	public boolean hasFireball() {
		return fireballId != 0;
	}

	public int getFireballId() {
		return fireballId;
	}

	public int getFireballX() {
		return fireballX;
	}

	public int getFireballY() {
		return fireballY;
	}

	public int getFireballZ() {
		return fireballZ;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("SpawnVehicleMessage{id=");
		build.append(id).append(",type=").append(type).append(",x=").append(x).append(",y=").append(y);
		build.append(",z=").append(z).append(",fireballId=").append(fireballId);

		if (hasFireball()) {
			build.append(",fireballX=").append(fireballX).append(",fireballY=").append(fireballY).append(",fireballZ=").append(fireballZ);
		}

		build.append("}");
		return build.toString();
	}
}
