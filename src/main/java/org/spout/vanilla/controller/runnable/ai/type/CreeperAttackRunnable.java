package org.spout.vanilla.controller.runnable.ai.type;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;

import org.spout.vanilla.controller.living.creature.hostile.Creeper;

/**
 * The creeper's AI which involves approaching a Player and exploding.
 */
public class CreeperAttackRunnable extends LogicRunnable<Creeper>{
	public CreeperAttackRunnable(Creeper parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public boolean shouldRun(float dt) {
		return false; //TODO Should this extend AttackRunnable and call super?
	}

	@Override
	public void run() {
		//TODO SSSSSSssssssssss boom!
	}
}
