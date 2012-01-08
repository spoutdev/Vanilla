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

import org.getspout.api.protocol.Message;

public final class SpawnVehicleMessage extends Message {
	private final int id, type, x, y, z, fireballId, fireballX, fireballY, fireballZ;

	public SpawnVehicleMessage(int id, int type, int x, int y, int z) {
		this(id, type, x, y, z, 0, 0, 0, 0);
	}
	public SpawnVehicleMessage(int id, int type, int x, int y, int z, int fbId, int fbX, int fbY, int fbZ) {
		this.id = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.fireballId = fbId;
		this.fireballX = fbX;
		this.fireballY = fbY;
		this.fireballZ = fbZ;
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

		if (hasFireball()) build.append(",fireballX=").append(fireballX).append(",fireballY=").append(fireballY).
				append(",fireballZ=").append(fireballZ);

		build.append("}");
		return build.toString();
	}
}