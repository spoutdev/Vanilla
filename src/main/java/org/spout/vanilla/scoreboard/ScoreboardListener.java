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
import org.spout.api.event.entity.EntityDeathEvent;

import org.spout.vanilla.component.entity.misc.Health;
import org.spout.vanilla.event.entity.EntityHealthChangeEvent;

public class ScoreboardListener implements Listener {
	@EventHandler(order = Order.LATEST)
	public void entityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		Engine engine = Spout.getEngine();
		if (!(entity instanceof Player) || !(engine instanceof Server)) {
			return;
		}

		Player player = (Player) entity;
		for (Player p : ((Server) engine).getOnlinePlayers()) {
			Scoreboard scoreboard = p.get(Scoreboard.class);
			if (scoreboard != null) {
				scoreboard.evaluateCriteria(Objective.CRITERIA_DEATH_COUNT, player.getName(), 1, true);
			}
		}
	}

	@EventHandler(order = Order.LATEST)
	public void entityHealth(EntityHealthChangeEvent event) {
		Entity entity = event.getEntity();
		Engine engine = Spout.getEngine();
		if (!(entity instanceof Player) || !(engine instanceof Server)) {
			return;
		}

		Player player = (Player) entity;
		for (Player p : ((Server) engine).getOnlinePlayers()) {
			Scoreboard scoreboard = p.get(Scoreboard.class);
			if (scoreboard != null) {
				scoreboard.evaluateCriteria(Objective.CRITERIA_HEALTH, player.getName(), player.get(Health.class).getHealth() + event.getChange(), false);
			}
		}
	}
}
