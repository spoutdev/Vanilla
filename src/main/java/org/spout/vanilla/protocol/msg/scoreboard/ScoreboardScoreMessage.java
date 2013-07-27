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
package org.spout.vanilla.protocol.msg.scoreboard;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.protocol.msg.VanillaMainChannelMessage;

public class ScoreboardScoreMessage extends VanillaMainChannelMessage {
	private final String item, scoreboard;
	private final boolean remove;
	private final int value;

	public ScoreboardScoreMessage(String item, boolean remove, String scoreboard, int value) {
		this.item = item;
		this.remove = remove;
		this.scoreboard = scoreboard;
		this.value = value;
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	public boolean isRemove() {
		return remove;
	}

	public String getItem() {
		return item;
	}

	public String getScoreboard() {
		return scoreboard;
	}

	public int getValue() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ScoreboardScoreMessage other = (ScoreboardScoreMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.item, other.item)
				.append(this.remove, other.remove)
				.append(this.scoreboard, other.scoreboard)
				.append(this.value, other.value)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("item", item)
				.append("remove", remove)
				.append("scoreboard", scoreboard)
				.append("value", value)
				.toString();
	}
}
