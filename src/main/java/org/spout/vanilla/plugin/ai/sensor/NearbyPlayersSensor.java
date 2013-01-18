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
		return state.get("hasNearbyPlayers");
	}
}
