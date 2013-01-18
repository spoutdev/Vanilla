package org.spout.vanilla.plugin.ai.action;

import org.spout.api.ai.goap.Action;
import org.spout.api.ai.goap.PlannerAgent;
import org.spout.api.ai.goap.WorldState;
import org.spout.api.component.impl.NavigationComponent;
import org.spout.api.entity.Player;

import org.spout.vanilla.plugin.ai.sensor.NearbyPlayersSensor;

/**
 * Very basic Attack action that directs the Entity to go attack the player that is nearby.
 *
 * The attack is simply the Entity colliding with the player. Actual attack damage is done within
 * onCollided.
 */
public class ActionAttack implements Action {
	private static final WorldState EFFECTS = WorldState.createImmutable("hasPlayer", false);
	private final PlannerAgent agent;
	private Player target;

	public ActionAttack(PlannerAgent agent) {
		this.agent = agent;
	}

	@Override
	public void activate() {
		target = agent.getSensor(NearbyPlayersSensor.class).getPlayers().iterator().next();
	}

	@Override
	public boolean evaluateContextPreconditions() {
		return true;
	}

	@Override
	public float getCost() {
		return 1;
	}

	@Override
	public WorldState getEffects() {
		return EFFECTS;
	}

	@Override
	public WorldState getPreconditions() {
		return WorldState.createEmptyState();
	}

	@Override
	public boolean isComplete() {
		final NavigationComponent navi = agent.getEntity().get(NavigationComponent.class); //You will always be in our hearts Navi...
		return target == null || target.isRemoved() || !agent.getSensor(NearbyPlayersSensor.class).detectedPlayer() || (navi != null && !navi.isNavigating());
	}

	@Override
	public void update() {
		final NavigationComponent navi = agent.getEntity().get(NavigationComponent.class);
		if (navi == null) {
			return;
		}
		//Gogo, get that fool!
		navi.setDestination(agent.getEntity().getTransform().getPosition());
	}
}
