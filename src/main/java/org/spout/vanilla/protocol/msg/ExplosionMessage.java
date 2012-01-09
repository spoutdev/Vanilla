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

import java.util.Arrays;

import org.spout.api.protocol.Message;

public final class ExplosionMessage extends Message {
	private final double x, y, z;
	private final float radius;
	private final byte[] coordinates;

	public ExplosionMessage(double x, double y, double z, float radius, byte[] coordinates) {
		if (coordinates.length % 3 != 0) {
			throw new IllegalArgumentException();
		}

		this.x = x;
		this.y = y;
		this.z = z;
		this.radius = radius;
		this.coordinates = coordinates;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getRadius() {
		return radius;
	}

	public int getRecords() {
		return coordinates.length / 3;
	}

	public byte[] getCoordinates() {
		return coordinates;
	}

	@Override
	public String toString() {
		return "ExplosionMessage{x=" + x + ",y=" + y + ",z=" + z + ",radius=" + radius + ",coordinates=" + Arrays.toString(coordinates) + "}";
	}
}