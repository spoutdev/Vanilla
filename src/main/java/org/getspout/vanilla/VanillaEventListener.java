package org.getspout.vanilla;

import org.getspout.api.event.EventHandler;
import org.getspout.api.event.Listener;
import org.getspout.api.event.Order;
import org.getspout.api.event.player.PlayerConnectEvent;
import org.getspout.api.event.player.PlayerJoinEvent;
import org.getspout.vanilla.entity.living.player.SurvivalPlayer;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;
	
	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(event = PlayerConnectEvent.class, priority = Order.DEFAULT)
	public void onPlayerConnect(PlayerConnectEvent event) {
		plugin.getGame().getLogger().info("Player connected: " + event.getPlayerName());
	}

	@EventHandler(event = PlayerJoinEvent.class, priority = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Set the player's controller
		// For now, only create Survival Players
		event.getPlayer().getEntity().setController(new SurvivalPlayer(event.getPlayer()));
	}
}
