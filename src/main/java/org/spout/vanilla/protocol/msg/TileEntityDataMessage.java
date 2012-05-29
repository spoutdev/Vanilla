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

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public class TileEntityDataMessage extends Message {
	private final int x, y, z, action, custom1, custom2, custom3;

	public TileEntityDataMessage(int x, int y, int z, int action, int custom1, int custom2, int custom3) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.action = action;
		this.custom1 = custom1;
		this.custom2 = custom2;
		this.custom3 = custom3;
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

	public int getAction() {
		return action;
	}

	public int getCustom1() {
		return custom1;
	}

	public int getCustom2() {
		return custom2;
	}

	public int getCustom3() {
		return custom3;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("x", x)
				.append("y", y)
				.append("z", z)
				.append("action", action)
				.append("custom1", custom1)
				.append("custom2", custom2)
				.append("custom3", custom3)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final TileEntityDataMessage other = (TileEntityDataMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.action, other.action)
				.append(this.custom1, other.custom1)
				.append(this.custom2, other.custom2)
				.append(this.custom3, other.custom3)
				.isEquals();
	}

	@Override
	public int hashCode() {
		// FIXME: Add a proper hashCode method!
		throw new UnsupportedOperationException("hashCode is not supported.");
	}
}
