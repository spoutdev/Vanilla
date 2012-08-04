package org.spout.vanilla.controller.runnable.ai;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;

import org.spout.vanilla.controller.living.Creature;

public class AttackRunnable extends LogicRunnable<Creature>{
	public AttackRunnable(Creature parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public boolean shouldRun(float dt) {
		return false;
	}

	@Override
	public void run() {

	}
}
