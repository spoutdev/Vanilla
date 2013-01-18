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
package org.spout.vanilla.plugin.ai.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.ai.Sensor;
import org.spout.api.ai.goap.PlannerAgent;
import org.spout.api.ai.goap.WorldState;
import org.spout.api.entity.Player;

/**
 * Simple Sensor that detects Players around the Entity this sensor belongs to.
 *
 * TODO Probably should have some sort of sight limitation on this sensor. Would be a great optimization.
 */
public class NearbyPlayersSensor implements Sensor {
	private final PlannerAgent agent;
	private final WorldState state;

	public NearbyPlayersSensor(PlannerAgent agent) {
		this.agent = agent;
		this.state = WorldState.createEmptyState();
	}

	@Override
	public WorldState generateState() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : agent.getEntity().getWorld().getNearbyPlayers(agent.getEntity(), 10)) {
			players.add(player);
		}
		boolean hasThreat = players.size() > 0;
		state.put("hasNearbyPlayers", hasThreat);
		state.put("players", hasThreat ? players : Collections.emptyList());
		return state;
	}

	/**
	 * Returns all the players this sensor has detected.
	 * @return Players detected
	 */
	public List<Player> getPlayers() {
		final List<Player> targets = state.get("players");
		return targets == null ? new ArrayList<Player>() : targets;
	}

	/**
	 * Returns if the sensor sensed a player nearby.
	 * @return True if player detected, false if not.
	 */
	public boolean detectedPlayer() {
		return (Boolean) state.get("hasNearbyPlayers");
	}
}
