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
package org.spout.vanilla.protocol.event.entity.player;

import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.protocol.msg.HealthMessage;

public class PlayerHealthEvent extends ProtocolEvent {
	private short health, hunger;
	private float saturation;

	public PlayerHealthEvent(short health, short hunger, float saturation) {
		this.health = health;
		this.hunger = hunger;
		this.saturation = saturation;
	}

	public short getHealth() {
		return health;
	}

	public void setHealth(short health) {
		this.health = health;
	}

	public short getHunger() {
		return hunger;
	}

	public void setHunger(short hunger) {
		this.hunger = hunger;
	}

	public float getFoodSaturation() {
		return saturation;
	}

	public void setFoodSaturation(float saturation) {
		this.saturation = saturation;
	}
}
