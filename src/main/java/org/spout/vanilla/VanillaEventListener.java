/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla;

import org.spout.api.Game;
import org.spout.api.entity.Entity;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.player.Player;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;
	private final Game game;

	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
		game = this.plugin.getGame();
	}

	//TODO Any reason to broadcast a player connects besides for debug? Perhaps only to the console and not players.
	@EventHandler
	public void onPlayerConnect(PlayerConnectEvent event) {
		game.getLogger().info("Player connected: " + event.getPlayerName());
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		game.broadcastMessage(player.getName() + " has joined the game");
		// Set the player's controller
		// For now, only create Survival Players
		Entity playerEntity = player.getEntity();
		playerEntity.setController(new SurvivalPlayer(player));
		player.setNetworkSynchronizer(new VanillaNetworkSynchronizer(player, playerEntity));
	}
}
