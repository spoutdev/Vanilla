/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla;

import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;
import org.spout.api.event.Result;
import org.spout.api.event.player.PlayerJoinEvent;
import org.spout.api.event.server.permissions.PermissionNodeEvent;

import org.spout.vanilla.component.inventory.window.DefaultWindow;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.component.misc.PickupItemComponent;
import org.spout.vanilla.component.misc.SleepComponent;
import org.spout.vanilla.component.player.PingComponent;
import org.spout.vanilla.component.player.PlayerListComponent;
import org.spout.vanilla.configuration.VanillaConfiguration;

public class VanillaListener implements Listener {
	private final VanillaPlugin plugin;

	public VanillaListener(VanillaPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(order = Order.EARLIEST)
	public void onPermissionNode(PermissionNodeEvent event) {
		if (VanillaConfiguration.OPS.isOp(event.getSubject().getName())) {
			event.setResult(Result.ALLOW);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		player.add(Human.class).setName(player.getName());
		player.add(DefaultWindow.class);
		player.add(PlayerListComponent.class);
		player.add(PingComponent.class);
		player.add(PickupItemComponent.class);
		player.add(SleepComponent.class);
	}
}
