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

import java.util.List;

import org.getspout.api.protocol.Message;
import org.getspout.api.util.Parameter;

public final class SpawnMobMessage extends Message {
	private final int id, type, x, y, z, rotation, pitch;
	private final List<Parameter<?>> parameters;

	public SpawnMobMessage(int id, int type, int x, int y, int z, int rotation, int pitch, List<Parameter<?>> parameters) {
		this.id = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.rotation = rotation;
		this.pitch = pitch;
		this.parameters = parameters;
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

	public int getRotation() {
		return rotation;
	}

	public int getPitch() {
		return pitch;
	}

	public List<Parameter<?>> getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("SpawnMobMessage{id=").append(id).
				append(",type=").append(type).append(",x=").append(x).append(",y=").
				append(y).append(",z=").append(z).append(",rotation=").
				append(rotation).append(",pitch=").append(pitch).append(",parameters=[");

		for (Parameter<?> parm : parameters) {
			build.append(parm).append(",");
		}

		build.append("]}");
		return build.toString();
	}
}