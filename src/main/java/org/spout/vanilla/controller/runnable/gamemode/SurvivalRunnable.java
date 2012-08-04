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
package org.spout.vanilla.controller.runnable.gamemode;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;

import org.spout.vanilla.controller.living.player.VanillaPlayer;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.Difficulty;
import org.spout.vanilla.data.ExhaustionLevel;
import org.spout.vanilla.data.VanillaData;

public class SurvivalRunnable extends LogicRunnable<VanillaPlayer> {
	private int foodTimer = 0;

	public SurvivalRunnable(VanillaPlayer parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public boolean shouldRun(float dt) {
		if (parent.isSurvival()) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		float distanceMoved = 0f;
		if ((distanceMoved += getParent().getPreviousPosition().distanceSquared(getParent().getParent().getPosition())) >= 1) {
			getParent().setExhaustion(getParent().getExhaustion() + ExhaustionLevel.WALKING.getAmount());
		}

		if (getParent().isSprinting()) {
			getParent().setExhaustion(getParent().getExhaustion() + ExhaustionLevel.SPRINTING.getAmount());
		}

		// TODO: Check for swimming, jumping, sprint jumping, block breaking, attacking, receiving damage for exhaustion level.
		if (getParent().isPoisoned()) {
			getParent().setExhaustion(getParent().getExhaustion() + ExhaustionLevel.FOOD_POISONING.getAmount());
		}

		// Track hunger
		foodTimer++;
		if (foodTimer >= 80) {
			updateHealthAndHunger();
			foodTimer = 0;
		}
	}

	private void updateHealthAndHunger() {
		if (getParent().getExhaustion() > 4.0) {
			getParent().setExhaustion(getParent().getExhaustion() - 4.0f);
			if (getParent().getFoodSaturation() > 0) {
				getParent().setFoodSaturation(Math.max(getParent().getExhaustion() - 1f, 0));
			} else {
				getParent().setHealth(Math.max(getParent().getHealth() - 1, 0), DamageCause.STARVE); //TODO fix Source here, correct?
			}
		}

		if (getParent().getHunger() <= 0 && getParent().getHealth() > 0) {
			int maxDrop;
			switch ((Difficulty) getParent().getParent().getWorld().get(VanillaData.DIFFICULTY)) {
				case EASY:
					maxDrop = 10;
					break;
				case NORMAL:
					maxDrop = 1;
					break;
				default:
					maxDrop = 0;
			}
			if (maxDrop < getParent().getHealth()) {
				getParent().setHealth(Math.max(getParent().getHealth() - 1, maxDrop), DamageCause.STARVE);
			}
		} else if (getParent().getHunger() >= 18 && getParent().getHealth() < 20) {
			getParent().setHealth(Math.min(getParent().getHealth() + 1, 20), HealthChangeReason.REGENERATION);
		}
	}
}
