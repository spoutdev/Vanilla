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
package org.spout.vanilla.controller.living.player;

import org.spout.api.entity.Entity;
import org.spout.api.player.Player;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.protocol.event.entity.player.PlayerHealthEvent;

public class SurvivalPlayer extends VanillaPlayer {
	private boolean poisoned = false;
	private short hunger = 20;
	private float foodSaturation = 5.0f, exhaustion = 0.0f;
	private long foodTimer = 0;

	public SurvivalPlayer(Player player) {
		super(player);
	}

	@Override
	public void onTick(float dt) {
		super.onTick(dt);
		exhaustion += 0.1;
		if (sprinting) {
			exhaustion += 0.1;
		}

		// TODO: Check for swimming, jumping, sprint jumping, block breaking, attacking, receiving damage for exhaustion level.

		if (poisoned) {
			exhaustion += 15.0;
		}

		// Track hunger
		foodTimer++;
		if (foodTimer >= 80) {
			updateHealth();
			foodTimer = 0;
		}
	}

	private void updateHealth() {
		short health;
		Entity parent = getParent();
		foodSaturation -= 0.1;
		health = (short) parent.getHealth();
		if (foodSaturation <= 0) {
			hunger--;
		} else {
			health++;
		}

		if (exhaustion >= 4.0) {
			exhaustion = 0;
			if (foodSaturation <= 0) {
				hunger--;
			} else {
				foodSaturation--;
			}
		}

		if (hunger <= 0) {
			health--;
		}

		System.out.println("Performing health/hunger update...");
		System.out.println("Food saturation: " + foodSaturation);
		System.out.println("Hunger: " + hunger);
		System.out.println("Health: " + health);
		System.out.println("Exhaustion: " + exhaustion);
		parent.setHealth(health, new HealthChangeReason(HealthChangeReason.Type.REGENERATION));
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new PlayerHealthEvent(health, hunger, foodSaturation));
	}

	/**
	 * Whether or not the controller is poisoned.
	 *
	 * @return true if poisoned.
	 */
	public boolean isPoisoned() {
		return poisoned;
	}

	/**
	 * Sets whether or not the controller is poisoned.
	 *
	 * @param poisoned
	 */
	public void setPoisoned(boolean poisoned) {
		this.poisoned = poisoned;
	}

	/**
	 * Returns the hunger of the player attached to the controller.
	 *
	 * @return hunger
	 */
	public short getHunger() {
		return hunger;
	}

	/**
	 * Sets the hunger of the controller.
	 *
	 * @param hunger
	 */
	public void setHunger(short hunger) {
		this.hunger = hunger;
	}

	/**
	 * Returns the food saturation level of the player attached to the controller. The food bar "jitters" when the bar reaches 0.
	 *
	 * @return food saturation level
	 */
	public float getFoodSaturation() {
		return foodSaturation;
	}

	/**
	 * Sets the food saturation of the controller. The food bar "jitters" when the bar reaches 0.
	 *
	 * @param foodSaturation
	 */
	public void setFoodSaturation(float foodSaturation) {
		this.foodSaturation = foodSaturation;
	}

	/**
	 * Returns the exhaustion of the controller; affects hunger loss.
	 *
	 * @return
	 */
	public float getExhaustion() {
		return exhaustion;
	}

	/**
	 * Sets the exhaustion of the controller; affects hunger loss.
	 *
	 * @param exhaustion
	 */
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}

	@Override
	public boolean hasInfiniteResources() {
		return false;
	}
}
