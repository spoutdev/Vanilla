/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.protocol.msg.world;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.reposition.RepositionManager;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.math.vector.Vector3;
import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public class ParticleEffectMessage extends VanillaMainChannelMessage {
	private final float x, y, z, xOffset, yOffset, zOffset, velocity;
	private final int amount;
	private final String name;

	public ParticleEffectMessage(String name, float x, float y, float z, float xOffset, float yOffset, float zOffset, float velocity, int amount, RepositionManager rm) {
		this.name = name;
		this.velocity = velocity;
		this.amount = amount;

		this.x = rm.convertX(x);
		this.y = rm.convertY(y);
		this.z = rm.convertZ(z);
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
	}

	public ParticleEffectMessage(String name, Vector3 position, Vector3 offset, float velocity, int amount, RepositionManager rm) {
		this(name, position.getX(), position.getY(), position.getZ(), offset.getX(), offset.getY(), offset.getZ(), velocity, amount, rm);
	}

	/**
	 * Name of the effect
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	/**
	 * X offset of the particle effect.
	 *
	 * @return xOffset
	 */
	public float getXOffset() {
		return xOffset;
	}

	/**
	 * Y offset of the particle effect.
	 *
	 * @return yOffset
	 */
	public float getYOffset() {
		return yOffset;
	}

	/**
	 * Z offset of the particle effect.
	 *
	 * @return zOffset
	 */
	public float getZOffset() {
		return zOffset;
	}

	/**
	 * The number of particles to create
	 *
	 * @return amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * The velocity given to the particles
	 *
	 * @return velocity
	 */
	public float getVelocity() {
		return velocity;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("name", this.name)
				.append("x", this.x)
				.append("y", this.y)
				.append("z", this.z)
				.append("xOffset", this.xOffset)
				.append("yOffset", this.yOffset)
				.append("zOffset", this.zOffset)
				.append("velocity", this.velocity)
				.append("amount", this.amount)
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
		final ParticleEffectMessage other = (ParticleEffectMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.name, other.name)
				.append(this.x, other.x)
				.append(this.y, other.y)
				.append(this.z, other.z)
				.append(this.xOffset, other.xOffset)
				.append(this.yOffset, other.yOffset)
				.append(this.zOffset, other.zOffset)
				.append(this.velocity, other.velocity)
				.append(this.amount, other.amount)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new org.apache.commons.lang3.builder.HashCodeBuilder()
				.append(this.name)
				.append(this.x)
				.append(this.y)
				.append(this.z)
				.append(this.xOffset)
				.append(this.yOffset)
				.append(this.zOffset)
				.append(this.velocity)
				.append(this.amount)
				.toHashCode();
	}
}
