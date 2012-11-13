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
package org.spout.vanilla.component.misc;

import java.util.logging.Logger;

import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.source.HealthChangeCause;

public class HungerComponent extends EntityComponent {
	private Human human;
	private float timer = 4;
	private Point lastPos;

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("HungerComponent may only be attached to players.");
		}
		human = getOwner().add(Human.class);
	}

	@Override
	public void onTick(float dt) {

		/*
		 * The Minecraft hunger system has a few different dynamics:
		 *
		 *      1) hunger - the amount of 'shanks' shown on the client.
		 *         1 points = 1/2 shank
		 *
		 *      2) food saturation - an invisible 'safety net' that is a default
		 *         5 points.
		 *
		 *      3) the timer - decreases by the delta of a tick every tick if
		 *         'hunger' > 17 or if 'food level' <= 0 and heals or deals
		 *         one point of damage respectively
		 *
		 *      4) exhaustion - anywhere in between 0 and 4 and increases with
		 *         certain actions. When the exhaustion reaches 4, it is reset
		 *         and subtracts one point from 'food saturation' if
		 *         'food saturation' > 0 or one point from 'hunger' if
		 *         'food saturation' <= 0 and 'hunger' > 0.
		 *
		 *     Exhaustion actions:
		 *         Walking and sneaking (per block) - 0.01
		 *         Sprinting (per block)            - 0.1
		 *         Swimming (per block)             - 0.015
		 *         Jumping (per block)              - 0.2
		 *         Sprint jump (per block)          - 0.8
		 *         Digging                          - 0.025
		 *         Attacking or being attacked      - 0.3
		 *         Food poisoning                   - 15 total over entire
		 *                                            duration
		 */

		final HealthComponent healthComponent = human.getHealth();
		final int health = healthComponent.getHealth();
		final int hunger = getHunger();

		// Regenerate health

		if (health < 20 && hunger > 17) {
			timer -= dt;
			if (timer <= 0) {
				healthComponent.setHealth(health + 1, HealthChangeCause.REGENERATION);
				timer = 4;
			}
		}

		// Damage health

		if (hunger <= 0) {
			timer -= dt;
			if (timer <= 0) {
				healthComponent.setHealth(health - 1, HealthChangeCause.DAMAGE);
				timer = 4;
			}
		}

		// Exhaustion

		final Point pos = getOwner().getTransform().getPosition();
		if (lastPos == null) {
			lastPos = pos;
			return;
		}
		final float distance = (float) pos.distance(lastPos);
		lastPos = pos; // Set the last position for next run

		float exhaustion = getExhaustion();
		final World world = pos.getWorld();
		final boolean sprinting = human.isSprinting();
		final boolean jumping = human.isJumping();
		final boolean digging = getOwner().add(DiggingComponent.class).isDigging();

		if (digging) {
			// digging
			exhaustion += 0.025f;
		}

		if (distance > 0) {
			if (world.getBlock(pos) == VanillaMaterials.WATER && world.getBlock(lastPos) == VanillaMaterials.WATER) {
				// swimming
				exhaustion += 0.015f * distance;
			} else if (sprinting && jumping) {
				// sprint jumping
				exhaustion += 0.8f * distance;
			} else if (jumping) {
				// jumping
				exhaustion += 0.2f * distance;
			} else if (sprinting) {
				// sprinting
				exhaustion += 0.1f * distance;
			} else {
				// walking
				exhaustion += 0.01f * distance;
			}
		}

		final float foodSaturation = getFoodSaturation();
		if (exhaustion >= 4) {
			if (foodSaturation > 0) {
				setFoodSaturation(foodSaturation - 1);
			} else if (hunger > 0) {
				setHunger(hunger - 1);
			}
			exhaustion = 0;
		}

		setExhaustion(exhaustion);
	}

	public int getHunger() {
		return getData().get(VanillaData.HUNGER);
	}

	public void setHunger(int hunger) {
		getData().put(VanillaData.HUNGER, hunger);
		reload();
	}

	public float getFoodSaturation() {
		return getData().get(VanillaData.FOOD_SATURATION);
	}

	public void setFoodSaturation(float foodSaturation) {
		getData().put(VanillaData.FOOD_SATURATION, foodSaturation);
		reload();
	}

	public float getExhaustion() {
		return getData().get(VanillaData.EXHAUSTION);
	}

	public void setExhaustion(float exhaustion) {
		getData().put(VanillaData.EXHAUSTION, exhaustion);
	}

	public Player getPlayer() {
		return (Player) getOwner();
	}

	public void reload() {
		getPlayer().getNetworkSynchronizer().callProtocolEvent(new PlayerHealthEvent(getPlayer()));
	}
}
