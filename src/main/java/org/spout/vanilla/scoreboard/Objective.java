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
import java.util.HashMap;
import java.util.Map;

import org.spout.api.util.Named;

import org.spout.vanilla.event.scoreboard.ObjectiveActionEvent;
import org.spout.vanilla.event.scoreboard.ObjectiveDisplayEvent;
import org.spout.vanilla.event.scoreboard.ScoreUpdateEvent;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardObjectiveMessage;

/**
 * Represents a objective belonging to a scoreboard.
 */
public class Objective implements Named {
	/**
	 * An objective with this criteria will have it's score updated when
	 * updated by commands.
	 */
	public static final String CRITERIA_DUMMY = "dummy";
	/**
	 * An objective with this criteria will have it's score updated when a
	 * player dies.
	 */
	public static final String CRITERIA_DEATH_COUNT = "deathCount";
	/**
	 * An objective with this criteria will have it's score updated when a
	 * player kills another player.
	 */
	public static final String CRITERIA_PLAYER_KILL_COUNT = "playerKillCount";
	/**
	 * An objective with this criteria will have it's score updated when a
	 * player kills another living entity.
	 */
	public static final String CRITERIA_TOTAL_KILL_COUNT = "totalKillCount";
	/**
	 * An objective with this criteria will have it's score updated when a
	 * player's health changes.
	 */
	public static final String CRITERIA_HEALTH = "health";
	private final Scoreboard scoreboard;
	private final String name;
	private String displayName = "";
	private final Map<String, Integer> score = new HashMap<String, Integer>();
	private ObjectiveSlot slot;
	private String criteria = CRITERIA_DUMMY;

	protected Objective(Scoreboard scoreboard, String name) {
		this.scoreboard = scoreboard;
		this.name = name;
	}

	/**
	 * Returns the objective associated with this scoreboard.
	 * @return scoreboard attached to this objective
	 */
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	/**
	 * Returns the name displayed on the scoreboard for this objective.
	 * @return display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the name displayed on the scoreboard for this objective.
	 * @param displayName to set
	 * @return this objective
	 */
	public Objective setDisplayName(String displayName) {
		this.displayName = displayName;
		scoreboard.callProtocolEvent(new ObjectiveActionEvent(this, ScoreboardObjectiveMessage.ACTION_UPDATE));
		return this;
	}

	/**
	 * Returns the score mapping.
	 * @return score mapping
	 */
	public Map<String, Integer> getScoreMap() {
		return Collections.unmodifiableMap(score);
	}

	/**
	 * Returns the score of the specified name.
	 * @param name to get score of
	 * @return score of specified name
	 */
	public int getScore(String name) {
		return score.get(name);
	}

	/**
	 * Sets the score for the specified key at the specified integer value.
	 * @param key to set score for
	 * @param value of score
	 * @return this objective
	 */
	public Objective setScore(String key, int value) {
		score.put(key, value);
		scoreboard.callProtocolEvent(new ScoreUpdateEvent(key, value, name, false));
		return this;
	}

	/**
	 * Adds to the current score of the specified name with the specified
	 * value.
	 * @param key to add score to
	 * @param value how much to add
	 * @return
	 */
	public Objective addScore(String key, int value) {
		setScore(key, getScore(key) + value);
		return this;
	}

	/**
	 * Removes a score entry of the specified name.
	 * @param key to remove entry
	 * @return this objective
	 */
	public Objective removeScore(String key) {
		score.remove(key);
		scoreboard.callProtocolEvent(new ScoreUpdateEvent(key, 0, name, true));
		return this;
	}

	/**
	 * Returns the slot this objective is being displayed at.
	 * @return slot this objective is being displayed at
	 */
	public ObjectiveSlot getSlot() {
		return slot;
	}

	/**
	 * Sets the slot this objective is being displayed at.
	 * @param slot to display objective at
	 * @return this objective
	 */
	public Objective setSlot(ObjectiveSlot slot) {
		this.slot = slot;
		scoreboard.callProtocolEvent(new ObjectiveDisplayEvent(name, slot));
		return this;
	}

	/**
	 * Returns the criteria for this objective.
	 * @return criteria of objective
	 */
	public String getCriteria() {
		return criteria;
	}

	/**
	 * Sets the criteria for this objective.
	 * @param criteria to set
	 * @return this objective
	 */
	public Objective setCriteria(String criteria) {
		this.criteria = criteria;
		return this;
	}

	@Override
	public String getName() {
		return name;
	}
}
