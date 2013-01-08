/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.plugin.protocol.msg.player;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.api.protocol.msg.VanillaMainChannelMessage;

public final class PlayerHealthMessage extends VanillaMainChannelMessage {
	private final short health, food;
	private final float foodSaturation;

	public PlayerHealthMessage(short health, short food, float foodSaturation) {
		this.health = health;
		this.food = food;
		this.foodSaturation = foodSaturation;
	}

	public short getHealth() {
		return health;
	}

	public short getFood() {
		return food;
	}

	public float getFoodSaturation() {
		return foodSaturation;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("health", health)
				.append("food", food)
				.append("foodSaturation", foodSaturation)
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
		final PlayerHealthMessage other = (PlayerHealthMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.health, other.health)
				.append(this.food, other.food)
				.append(this.foodSaturation, other.foodSaturation)
				.isEquals();
	}
}
