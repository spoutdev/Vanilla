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
package org.spout.vanilla.protocol.msg.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public class SteerVehicleMessage extends VanillaMainChannelMessage {
	private final float sideways, forward;
	private final boolean jump, unmount;

	public SteerVehicleMessage(float sideways, float forward, boolean jump, boolean unmount) {
		this.sideways = sideways;
		this.forward = forward;
		this.jump = jump;
		this.unmount = unmount;
	}

	public float getSideways() {
		return sideways;
	}

	public boolean isUnmount() {
		return unmount;
	}

	public boolean isJumping() {
		return jump;
	}

	public float getForward() {
		return forward;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("sideways", this.getSideways())
				.append("unmount", this.isUnmount())
				.append("jump", this.isJumping())
				.append("forward", this.getForward())
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
		final SteerVehicleMessage other = (SteerVehicleMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.getSideways(), other.getSideways())
				.append(this.isUnmount(), other.isUnmount())
				.append(this.isJumping(), other.isJumping())
				.append(this.getForward(), other.getForward())
				.isEquals();
	}
}
