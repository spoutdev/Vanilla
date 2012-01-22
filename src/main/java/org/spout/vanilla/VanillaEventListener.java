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

import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.player.PlayerConnectEvent;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;
import org.spout.api.protocol.Message;
import org.spout.vanilla.entity.living.player.SurvivalPlayer;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.SpawnPlayerMessage;

public class VanillaEventListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaEventListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerConnect(PlayerConnectEvent event) {
		plugin.getGame().getLogger().info("Player connected: " + event.getPlayerName());
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPlayerJoin(PlayerJoinEvent event) {

		plugin.getGame().broadcastMessage(event.getPlayer().getName() + " has joined the game");
		// Set the player's controller
		// For now, only create Survival Players
		Entity playerEntity = event.getPlayer().getEntity();
		playerEntity.setController(new SurvivalPlayer(event.getPlayer()));
		event.getPlayer().setNetworkSynchronizer(new VanillaNetworkSynchronizer(event.getPlayer(), playerEntity));

		Point point = playerEntity.getLiveTransform().getPosition();
		float pitch = playerEntity.getLiveTransform().getRotation().getAxisAngles().getZ();
		float yaw = playerEntity.getLiveTransform().getRotation().getAxisAngles().getY();

		//Inform existing players of the new player
		Message update = new SpawnPlayerMessage(playerEntity.getId(), event.getPlayer().getName(),
			(int)(point.getX() * 32), (int)(point.getY() * 32), (int)(point.getZ() * 32),
			(int)(yaw  * 256.0F / 360.0F), (int)(pitch * 256.0F / 360.0F), 0);

		for (Player p : plugin.getGame().getOnlinePlayers()) {
			if (!p.equals(event.getPlayer()))
				p.getSession().send(update);
		}

		plugin.getGame().getScheduler().scheduleSyncDelayedTask(plugin, new LoginRunnable(event.getPlayer()), 1L);
	}
}

//Can not do this immediately, player has not been sent world yet. :/
class LoginRunnable implements Runnable {
	Player player;
	public LoginRunnable(Player player) {
		this.player = player;
	}

	public void run() {
		if (player.isOnline()){
			//Inform the new player of existing players
			for (Player p : Spout.getGame().getOnlinePlayers()) {
				if (!p.equals(player)) {
					Point playerPoint = p.getEntity().getLiveTransform().getPosition();
					float playerPitch = p.getEntity().getLiveTransform().getRotation().getAxisAngles().getZ();
					float playerYaw = p.getEntity().getLiveTransform().getRotation().getAxisAngles().getY();

					player.getSession().send(
						new SpawnPlayerMessage(p.getEntity().getId(), p.getName(), (int)(playerPoint.getX() * 32),
						(int)(playerPoint.getY() * 32), (int)(playerPoint.getZ() * 32),
						(int)(playerYaw  * 256.0F / 360.0F), (int)(playerPitch * 256.0F / 360.0F), 0));
				}
			}
		}
	}
}
