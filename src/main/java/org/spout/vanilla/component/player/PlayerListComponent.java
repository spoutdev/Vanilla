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
package org.spout.vanilla.component.player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.component.Component;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.event.player.network.PlayerListEvent;

public class PlayerListComponent extends EntityComponent {
	private Player player;
	private Server server;
	private final LinkedHashMap<String, Long> players = new LinkedHashMap<String, Long>();
	private final HashSet<String> temp = new HashSet<String>();
	private float pollPeriod = 10;
	private float timer = 0;

	static {
		Component.addDependency(PlayerListComponent.class, PingComponent.class);
	}

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("PingComponent may only be attached to a player.");
		}
		if (!(Spout.getEngine() instanceof Server)) {
			throw new IllegalStateException("Player list components may only be attached server side");
		}
		server = (Server) Spout.getEngine();
		player = (Player) getOwner();
	}

	@Override
	public void onTick(float dt) {
		timer -= dt;
		if (timer < 0.0F) {
			pollList();
			timer += pollPeriod;
		}
	}

	private void pollList() {
		Player[] online = server.getOnlinePlayers();
		temp.clear();
		for (int i = 0; i < online.length; i++) {
			String name = online[i].getDisplayName();
			long ping = (long) (1000.0F * online[i].add(PingComponent.class).getPing());
			temp.add(name);
			Long oldPing = players.put(name, ping);
			if (oldPing == null || !oldPing.equals(ping)) {
				player.getNetworkSynchronizer().callProtocolEvent(new PlayerListEvent(name, ping, true));
			}
		}
		Iterator<String> itr = players.keySet().iterator();
		while (itr.hasNext()) {
			String name = itr.next();
			if (!temp.contains(name)) {
				player.getNetworkSynchronizer().callProtocolEvent(new PlayerListEvent(name, 0L, false));
				itr.remove();
			}
		}
	}
}
