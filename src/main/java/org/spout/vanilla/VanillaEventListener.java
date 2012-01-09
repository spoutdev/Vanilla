/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spout.vanilla;

import org.spout.api.entity.Entity;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;
	
	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(event = PlayerConnectEvent.class)
	public void onPlayerConnect(PlayerConnectEvent event) {
		plugin.getGame().getLogger().info(plugin.getPrefix() + "Player connected: " + event.getPlayerName());
	}

	@EventHandler(event = PlayerJoinEvent.class, order = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// Set the player's controller
		// For now, only create Survival Players
		Entity playerEntity = event.getPlayer().getEntity();
		playerEntity.setController(new SurvivalPlayer(event.getPlayer()));
	}
}
