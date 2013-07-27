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
package org.spout.vanilla.scoreboard;

import java.util.HashSet;
import java.util.Set;

import org.spout.api.util.Named;

import org.spout.vanilla.ChatStyle;
import org.spout.vanilla.event.scoreboard.TeamActionEvent;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardTeamMessage;

/**
 * Represents a team of players associated with a scoreboard.
 */
public class Team implements Named {
	private final Scoreboard scoreboard;
	private final String name;
	private String displayName = "";
	private String prefix = "";
	private String suffix = ChatStyle.RESET.toString();
	private boolean friendlyFire = false;
	private final Set<String> playerNames = new HashSet<String>();

	protected Team(Scoreboard scoreboard, String name) {
		this.scoreboard = scoreboard;
		this.name = name;
	}

	/**
	 * Returns the name to display for this team.
	 * @return display name of this team
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the name to be displayed for this team
	 * @param displayName for team
	 * @return this team
	 */
	public Team setDisplayName(String displayName) {
		this.displayName = displayName;
		update();
		return this;
	}

	/**
	 * Returns the {@link String} that prefaces the name of each player
	 * on this team.
	 * @return prefix of players on this team
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix of each player on this team.
	 * @param prefix of each player on the team
	 * @return this team
	 */
	public Team setPrefix(String prefix) {
		this.prefix = prefix;
		update();
		return this;
	}

	/**
	 * Returns the {@link String} that come after each player's name.
	 * @return suffix for players
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the suffix for the players on this team.
	 * @param suffix for players
	 * @return this team
	 */
	public Team setSuffix(String suffix) {
		this.suffix = suffix;
		update();
		return this;
	}

	/**
	 * Returns true if players on the same team cannot hurt each other.
	 * @return true if teammates can hurt each other
	 */
	public boolean isFriendlyFire() {
		return friendlyFire;
	}

	/**
	 * Sets if players on the same team can hurt each other.
	 * @param friendlyFire true if teammates should be able to hurt each other.
	 * @return this team
	 */
	public Team setFriendlyFire(boolean friendlyFire) {
		this.friendlyFire = friendlyFire;
		update();
		return this;
	}

	/**
	 * Returns a set of all the names of players on this team.
	 * @return names of players
	 */
	public Set<String> getPlayerNames() {
		return playerNames;
	}

	/**
	 * Adds a name to the team.
	 * @param name to add
	 * @return this team
	 */
	public Team addPlayerName(String name) {
		playerNames.add(name);
		scoreboard.callProtocolEvent(new TeamActionEvent(this, ScoreboardTeamMessage.ADD_PLAYERS, name));
		return this;
	}

	/**
	 * Removes a name from the team
	 * @param name to add
	 * @return this team
	 */
	public Team removePlayerName(String name) {
		playerNames.remove(name);
		scoreboard.callProtocolEvent(new TeamActionEvent(this, ScoreboardTeamMessage.REMOVE_PLAYERS, name));
		return this;
	}

	private void update() {
		scoreboard.callProtocolEvent(new TeamActionEvent(this, ScoreboardTeamMessage.ACTION_UPDATE));
	}

	@Override
	public String getName() {
		return name;
	}
}
