package org.getspout.vanilla;

import org.getspout.api.entity.Entity;
import org.getspout.api.event.EventHandler;
import org.getspout.api.event.Listener;
import org.getspout.api.event.Order;
import org.getspout.api.event.player.PlayerConnectEvent;
import org.getspout.api.event.player.PlayerJoinEvent;
import org.getspout.api.geo.discrete.Transform;
import org.getspout.vanilla.entity.living.player.SurvivalPlayer;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;
	
	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(event = PlayerConnectEvent.class)
	public void onPlayerConnect(PlayerConnectEvent event) {
		plugin.getGame().getLogger().info("Player connected: " + event.getPlayerName());
	}

	@EventHandler(event = PlayerJoinEvent.class, order = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Set the player's controller
		// For now, only create Survival Players
		Entity playerEntity = event.getPlayer().getEntity();
		playerEntity.setController(new SurvivalPlayer(event.getPlayer()));
	}
}
