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

import org.spout.api.datatable.ManagedHashMap;
import org.spout.api.datatable.ManagedMap;
import org.spout.api.util.Named;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.scoreboard.TeamActionEvent;
import org.spout.vanilla.protocol.msg.scoreboard.ScoreboardTeamMessage;

/**
 * Represents a team of players associated with a scoreboard.
 */
public class Team implements Named {
	private final Scoreboard scoreboard;
	private final String name;
	private final ManagedMap data;

	private final Set<String> playerNames = new HashSet<String>();

	protected Team(Scoreboard scoreboard, String name) {
		this.data = new ManagedHashMap();
		this.scoreboard = scoreboard;
		this.name = name;
	}

	/**
	 * Returns the name to display for this team.
	 *
	 * @return display name of this team
	 */
	public String getDisplayName() {
		return getData().get(VanillaData.DISPLAY_NAME);
	}

	/**
	 * Sets the name to be displayed for this team
	 *
	 * @param displayName for team
	 * @param updateClients whether to update clients or not
	 */
	public void setDisplayName(String displayName, boolean updateClients) {
		getData().put(VanillaData.DISPLAY_NAME, displayName);
		if (updateClients) {
			update();
		}
	}

	/**
	 * Returns the {@link String} that prefaces the name of each player on this team.
	 *
	 * @return prefix of players on this team
	 */
	public String getPrefix() {
		return getData().get(VanillaData.PREFIX);
	}

	/**
	 * Sets the prefix of each player on this team.
	 *
	 * @param prefix of each player on the team
	 * @param updateClients whether to update clients or not
	 */
	public void setPrefix(String prefix, boolean updateClients) {
		getData().put(VanillaData.PREFIX, prefix);
		if (updateClients) {
			update();
		}
	}

	/**
	 * Returns the {@link String} that come after each player's name.
	 *
	 * @return suffix for players
	 */
	public String getSuffix() {
		return getData().get(VanillaData.SUFFIX);
	}

	/**
	 * Sets the suffix for the players on this team.
	 *
	 * @param suffix for players
	 * @param updateClients whether to update clients or not
	 */
	public void setSuffix(String suffix, boolean updateClients) {
		getData().put(VanillaData.SUFFIX, suffix);
		if (updateClients) {
			update();
		}
	}

	/**
	 * Returns true if players on the same team cannot hurt each other.
	 *
	 * @return true if teammates can hurt each other
	 */
	public boolean isFriendlyFire() {
		return getData().get(VanillaData.FRIENDLY_FIRE);
	}

	/**
	 * Sets if players on the same team can hurt each other.
	 *
	 * @param friendlyFire true if teammates should be able to hurt each other.
	 */
	public void setFriendlyFire(boolean friendlyFire) {
		getData().put(VanillaData.FRIENDLY_FIRE, friendlyFire);
		update();
	}

	/**
	 * Returns true if players on the same team are able to see other invisible teammates.
	 *
	 * @return true if treammates can see each other when invisible
	 */
	public boolean canSeeFriendlyInvisibles() {
		return getData().get(VanillaData.SEE_FRIENDLY_INVISIBLES);
	}

	/**
	 * Sets if teammates should be able to see other invisible teammates.
	 *
	 * @param seeFriendlyInvisibles true if teammates should be able to see other invisible teammates
	 */
	public void setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
		getData().put(VanillaData.SEE_FRIENDLY_INVISIBLES, seeFriendlyInvisibles);
		update();
	}

	/**
	 * Returns a set of all the names of players on this team.
	 *
	 * @return names of players
	 */
	public Set<String> getPlayerNames() {
		return playerNames;
	}

	/**
	 * Adds a name to the team.
	 *
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
	 *
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

	/**
	 * Gets the {@link ManagedMap} containing the data for this Team.
	 *
	 * @return datatable
	 */
	public ManagedMap getData() {
		return data;
	}
}
