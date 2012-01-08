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

public final class HealthMessage extends Message {
	private final int health, food;
	private final float foodSaturation;

	public HealthMessage(int health, int food, float foodSaturation) {
		this.health = health;
		this.food = food;
		this.foodSaturation = foodSaturation;
	}

	public int getHealth() {
		return health;
	}

	public int getFood() {
		return food;
	}

	public float getFoodSaturation() {
		return foodSaturation;
	}

	@Override
	public String toString() {
		return "HealthMessage{health=" + health + ",food=" + food + ",foodSaturation=" + foodSaturation + "}";
	}
}