package org.spout.vanilla.controller.action;

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
