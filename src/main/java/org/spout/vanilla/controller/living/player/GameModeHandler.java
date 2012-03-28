package org.spout.vanilla.controller.living.player;



public abstract class GameModeHandler {
	public GameModeHandler(VanillaPlayer vplr) {
		
	}
	
	public abstract void onTick(float dt);

	public abstract boolean hasInfiniteResources();
}
