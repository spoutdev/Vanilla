/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
package org.spout.vanilla.protocol.msg.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.nbt.CompoundMap;

import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public class EntityTileDataMessage extends VanillaMainChannelMessage {
	private final int x;
	private final int y;
	private final int z;
	private final int action;
	private int custom1;
	private int custom2;
	private int custom3;
	private CompoundMap data;

	public EntityTileDataMessage(int x, int y, int z, int action, int[] data) {
		this(x, y, z, action, data.length >= 1 ? data[0] : -1, data.length >= 2 ? data[1] : -1, data.length >= 3 ? data[2] : -1);
	}

	public EntityTileDataMessage(int x, int y, int z, int action, int custom1, int custom2, int custom3) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.action = action;
		this.custom1 = custom1;
		this.custom2 = custom2;
		this.custom3 = custom3;
	}

	public EntityTileDataMessage(int x, int y, int z, int action, CompoundMap data) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.action = action;
		this.data = data;
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

	public CompoundMap getData() {
		return data;
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
		final EntityTileDataMessage other = (EntityTileDataMessage) obj;
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
}
