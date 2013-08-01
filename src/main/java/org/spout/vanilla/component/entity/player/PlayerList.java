/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Vanilla is licensed under the Spout License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package org.spout.vanilla.component.entity.player;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.spout.api.Server;
import org.spout.api.entity.Player;

import org.spout.vanilla.component.entity.VanillaEntityComponent;
import org.spout.vanilla.event.player.network.ListPingEvent;

/**
 * Component that handles the Player list (TAB) list.
 */
public class PlayerList extends VanillaEntityComponent {
	private Player player;
	private Server server;
	private final LinkedHashMap<String, Long> players = new LinkedHashMap<String, Long>();
	private final HashSet<String> temp = new HashSet<String>();
	private float timer = 0;
	private boolean force; // If true will force the list on the next tick

	@Override
	public void onAttached() {
		if (!(getOwner() instanceof Player)) {
			throw new IllegalStateException("PlayerList may only be attached to a player.");
		}
		if (!(getOwner().getEngine() instanceof Server)) {
			throw new IllegalStateException("Player list components may only be attached server side");
		}
		server = (Server) getOwner().getEngine();
		player = (Player) getOwner();
	}

	@Override
	public void onTick(float dt) {
		timer -= dt;
		if (timer < 0.0F) {
			pollList();
			float pollPeriod = 5F;
			timer += pollPeriod;
		} else if (force) {
			pollList();
			force = false;
		}
	}

	private void pollList() {
		Player[] online = server.getOnlinePlayers();
		temp.clear();
		for (int i = 0; i < online.length; i++) {
			Ping pingComponent = online[i].get(Ping.class);
			if (pingComponent != null && !player.isInvisible(online[i])) {
				String name = online[i].getDisplayName();
				long ping = (long) (1000.0F * pingComponent.getPing());
				temp.add(name);
				Long oldPing = players.put(name, ping);
				if (oldPing == null || !oldPing.equals(ping)) {
					player.getNetwork().callProtocolEvent(new ListPingEvent(name, ping, true));
				}
			}
		}
		Iterator<String> itr = players.keySet().iterator();
		while (itr.hasNext()) {
			String name = itr.next();
			if (!temp.contains(name)) {
				player.getNetwork().callProtocolEvent(new ListPingEvent(name, 0L, false));
				itr.remove();
			}
		}
	}

	/**
	 * Force a update of the list.
	 */
	public void force() {
		force = true;
	}
}
