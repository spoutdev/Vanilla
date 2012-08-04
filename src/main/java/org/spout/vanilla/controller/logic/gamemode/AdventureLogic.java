package org.spout.vanilla.controller.logic.gamemode;

import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;

import org.spout.vanilla.controller.living.player.VanillaPlayer;

/**
 * Basic logic that applies Adventure-mode rules to VanillaPlayers.
 */
public class AdventureLogic extends LogicRunnable<VanillaPlayer> {
	public AdventureLogic(VanillaPlayer parent, LogicPriority priority) {
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
