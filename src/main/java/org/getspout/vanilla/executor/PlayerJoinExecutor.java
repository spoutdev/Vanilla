package org.getspout.vanilla.executor;

import org.getspout.api.event.Event;
import org.getspout.api.event.EventException;
import org.getspout.api.event.EventExecutor;
import org.getspout.api.event.player.PlayerJoinEvent;
import org.getspout.vanilla.VanillaPlugin;
import org.getspout.vanilla.entity.living.player.SurvivalPlayer;

public class PlayerJoinExecutor implements EventExecutor {
	VanillaPlugin p;
	public PlayerJoinExecutor(VanillaPlugin p){
		this.p = p;
	}
	private void execute(PlayerJoinEvent event){
		//Set the player's controller
		//For now, only create Survival Players
		event.getPlayer().getEntity().setController(new SurvivalPlayer(event.getPlayer()));
	}
	
	public void execute(Event event) throws EventException {
		execute((PlayerJoinEvent) event);
	}

}
