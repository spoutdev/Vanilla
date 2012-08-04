package org.spout.vanilla.controller.action;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;

import org.spout.vanilla.controller.living.player.VanillaPlayer;

public class CreativeRunnable extends LogicRunnable<VanillaPlayer> {
	public CreativeRunnable(VanillaPlayer parent, LogicPriority priority) {
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
