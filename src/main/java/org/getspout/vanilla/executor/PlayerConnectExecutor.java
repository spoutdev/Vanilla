package org.getspout.vanilla.executor;

import org.getspout.api.event.Event;
import org.getspout.api.event.EventException;
import org.getspout.api.event.EventExecutor;
import org.getspout.api.event.player.PlayerConnectEvent;
import org.getspout.vanilla.VanillaPlugin;

public class PlayerConnectExecutor implements EventExecutor {
	
	private final VanillaPlugin plugin;
	
	public PlayerConnectExecutor(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	public void execute(PlayerConnectEvent event) throws EventException {
		plugin.getGame().getLogger().info("Player connected: " + event.getPlayerName());
	}

	public void execute(Event event) throws EventException {
		if (event instanceof PlayerConnectEvent) {
			execute((PlayerConnectEvent)event);
		}
	}

}
