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
package org.spout.vanilla.entity.component.gamemode;

import org.spout.api.Spout;
import org.spout.api.entity.BasicComponent;
import org.spout.api.entity.Player;
import org.spout.api.geo.discrete.Point;
import org.spout.api.tickable.TickPriority;

import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.ExhaustionLevel;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.entity.VanillaPlayerController;
import org.spout.vanilla.entity.source.DamageCause;
import org.spout.vanilla.entity.source.HealthChangeReason;
import org.spout.vanilla.event.player.PlayerFoodSaturationChangeEvent;
import org.spout.vanilla.event.player.PlayerHungerChangeEvent;

/**
 * Basic component that applies Survival-mode rules to a VanillaPlayerController
 */
public class SurvivalComponent extends BasicComponent<VanillaPlayerController> {
	private int foodTimer = 0;
	private short maxHunger = 20, hunger = maxHunger;
	private float foodSaturation = 5.0f, exhaustion = 0.0f;

	public SurvivalComponent(TickPriority priority) {
		super(priority);
	}

	@Override
	public void onAttached() {
		super.onAttached();
	}

	@Override
	public boolean canTick() {
		if (getParent().isSurvival()) {
			return true;
		}
		return false;
	}

	@Override
	public void onTick(float dt) {
		float distanceMoved = 0f;
		//Current position
		Point position = getParent().getParent().getPosition();
		distanceMoved += position.distanceSquared(getParent().getParent().getLastTransform().getPosition());
		if (distanceMoved >= 1) {
			setExhaustion(getExhaustion() + ExhaustionLevel.WALKING.getAmount());
			distanceMoved = 0.0f;
		}

		if (getParent().isSprinting()) {
			setExhaustion(getExhaustion() + ExhaustionLevel.SPRINTING.getAmount());
		}

		// Track hunger
		foodTimer++;
		if (foodTimer >= 80) {
			updateHealthAndHunger();
			foodTimer = 0;
		}
	}

	private void updateHealthAndHunger() {
		if (getExhaustion() > 4.0) {
			setExhaustion(getExhaustion() - 4.0f);
			if (getFoodSaturation() > 0) {
				setFoodSaturation(Math.max(getFoodSaturation() - 1f, 0));
			} else {
				setHunger((short) Math.max(getHunger() - 1, 0));
			}
		}

		if (getHunger() <= 0 && !getParent().getHealth().isDead()) {
			int maxDrop;
			switch ((Difficulty) ((Player) getParent().getParent()).getWorld().get(VanillaData.DIFFICULTY)) {
				case EASY:
					maxDrop = 10;
					break;
				case NORMAL:
					maxDrop = 1;
					break;
				default:
					maxDrop = 0;
			}
			if (maxDrop < getParent().getHealth().getHealth()) {
				getParent().getHealth().setHealth(Math.max(getParent().getHealth().getHealth() - 1, maxDrop), DamageCause.STARVE);
			}
		} else if (getHunger() >= 18 && getParent().getHealth().getHealth() < 20) {
			getParent().getHealth().setHealth(getParent().getHealth().getHealth() + 1, HealthChangeReason.REGENERATION);
		}
	}

	/**
	 * Returns the food saturation level of the player attached to the entity. The food bar "jitters" when the bar reaches 0.
	 * @return food saturation level
	 */
	public float getFoodSaturation() {
		return this.foodSaturation;
	}

	/**
	 * Sets the food saturation of the entity. The food bar "jitters" when the bar reaches 0.
	 * @param foodSaturation The value to set to
	 */
	public void setFoodSaturation(float foodSaturation) {
		PlayerFoodSaturationChangeEvent event = new PlayerFoodSaturationChangeEvent(getParent().getParent(), foodSaturation);
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (event.getFoodSaturation() > hunger) {
				this.foodSaturation = hunger;
			} else {
				this.foodSaturation = event.getFoodSaturation();
			}
		}
	}

	/**
	 * Returns the hunger of the player attached to the entity.
	 * @return hunger
	 */
	public short getHunger() {
		return hunger;
	}

	/**
	 * Returns the exhaustion of the entity; affects hunger loss.
	 * @return
	 */
	public float getExhaustion() {
		return exhaustion;
	}

	/**
	 * Sets the exhaustion of the entity; affects hunger loss.
	 * @param exhaustion
	 */
	public void setExhaustion(float exhaustion) {
		this.exhaustion = exhaustion;
	}

	/**
	 * Adds a value to the exhaustion of the entity; affects hunger loss.
	 * @param exhaustion to add
	 */
	public void addExhaustion(float exhaustion) {
		this.exhaustion += exhaustion;
	}

	/**
	 * Sets the hunger of the entity.
	 * @param hunger
	 */
	public void setHunger(short hunger) {
		PlayerHungerChangeEvent event = new PlayerHungerChangeEvent(getParent().getParent(), hunger);
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (event.getHunger() > maxHunger) {
				this.hunger = maxHunger;
			} else {
				this.hunger = event.getHunger();
			}
		}
	}
}
