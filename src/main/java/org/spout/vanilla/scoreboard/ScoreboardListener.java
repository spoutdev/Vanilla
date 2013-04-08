/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.scoreboard;

import org.spout.api.Engine;
import org.spout.api.Server;
import org.spout.api.Spout;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.EventHandler;
import org.spout.api.event.Listener;
import org.spout.api.event.Order;

import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.event.entity.EntityHealthChangeEvent;
import org.spout.vanilla.event.entity.VanillaEntityDeathEvent;

public class ScoreboardListener implements Listener {
	@EventHandler(order = Order.LATEST)
	public void entityDeath(VanillaEntityDeathEvent event) {
		Entity entity = event.getEntity();
		Engine engine = Spout.getEngine();

		// kill count criteria
		Object lastDamager = event.getLastDamager();
		if (lastDamager instanceof Player) {
			Player killer = (Player) lastDamager;
			String killerName = killer.getName();
			String[] criteria = {Objective.CRITERIA_TOTAL_KILL_COUNT, Objective.CRITERIA_PLAYER_KILL_COUNT};
			if (entity instanceof Player) {
				// killed a player? update total and player kill count
				evaluateCriteria(killerName, 1, true, criteria);
			} else {
				// just an entity? only update total kill count
				evaluateCriteria(killerName, 1, true, criteria[0]);
			}
		}

		if (!(entity instanceof Player) || !(engine instanceof Server)) {
			return;
		}

		// player death criteria
		Player player = (Player) entity;
		evaluateCriteria(player.getName(), 1, true, Objective.CRITERIA_DEATH_COUNT);
	}

	@EventHandler(order = Order.LATEST)
	public void entityHealth(EntityHealthChangeEvent event) {
		Entity entity = event.getEntity();
		Engine engine = Spout.getEngine();
		if (!(entity instanceof Player) || !(engine instanceof Server)) {
			return;
		}

		Player player = (Player) entity;
		evaluateCriteria(player.getName(), player.get(Health.class).getHealth() + event.getChange(), false, Objective.CRITERIA_HEALTH);
	}

	private void evaluateCriteria(String key, int value, boolean add, String... criteria) {
		for (Player player : ((Server) Spout.getEngine()).getOnlinePlayers()) {
			Scoreboard scoreboard = player.get(Scoreboard.class);
			if (scoreboard != null) {
				scoreboard.evaluateCriteria(key, value, add, criteria);
			}
		}
	}
}
