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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.chat.ChatArguments;
import org.spout.api.component.type.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;

import org.spout.vanilla.event.scoreboard.ObjectiveActionEvent;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardObjectiveMessage;

/**
 * Represents a collection of objective attached to a player.
 */
public class Scoreboard extends EntityComponent {
	private Player player;
	private final Set<Objective> objectives = new HashSet<Objective>();

	/**
	 * Creates a new objective with the specified name and display name.
	 *
	 * @param name of objective
	 * @return newly created objective
	 */
	public Objective createObjective(String name) {
		Objective obj = new Objective(this, name);
		objectives.add(obj);
		player.getNetworkSynchronizer().callProtocolEvent(new ObjectiveActionEvent(obj, ScoreboardObjectiveMessage.ACTION_CREATE));
		return obj;
	}

	/**
	 * Removes the objective with the specified name.
	 *
	 * @param name to remove objective
	 */
	public void removeObjective(String name) {
		Objective obj = getObjective(name);
		if (obj == null) {
			throw new IllegalArgumentException("Specified objective name does not exist on this scoreboard.");
		}
		objectives.remove(obj);
		player.getNetworkSynchronizer().callProtocolEvent(new ObjectiveActionEvent(obj, ScoreboardObjectiveMessage.ACTION_REMOVE));
	}

	/**
	 * Returns the objective with the specified name.
	 *
	 * @param name to get objective from
	 * @return objective with specified name
	 */
	public Objective getObjective(String name) {
		for (Objective obj : objectives) {
			if (obj.getName().equals(name)) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * Returns all created objectives on this scoreboard.
	 *
	 * @return all objectives
	 */
	public Set<Objective> getObjectives() {
		return Collections.unmodifiableSet(objectives);
	}

	/**
	 * Updates the score of the specified key for every objective with the
	 * specified criteria to the specified value.
	 *
	 * @param criteria to check for
	 * @param key to find
	 * @param value of points to add
	 */
	public void evaluateCriteria(String key, int value, boolean add, String... criteria) {
		for (Objective obj : objectives) {
			for (String c : criteria) {
				if (obj.getCriteria().equals(c) && obj.getScoreMap().containsKey(key)) {
					if (add) {
						obj.addScore(key, value);
					} else {
						obj.setScore(key, value);
					}
				}
			}
		}
	}

	/**
	 * Returns the player associated with this scoreboard.
	 *
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	@Override
	public void onAttached() {
		Entity owner = getOwner();
		if (!(owner instanceof Player)) {
			throw new IllegalStateException("Scoreboard can only be attached to players.");
		}
		player = (Player) owner;
	}
}
