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
package org.spout.vanilla.plugin.protocol.msg.player;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.api.protocol.msg.VanillaMainChannelMessage;

public class PlayerExperienceMessage extends VanillaMainChannelMessage {
	private final float barValue;
	private final short level, totalExp;

	public PlayerExperienceMessage(float barValue, short level, short totalExp) {
		this.barValue = barValue;
		this.level = level;
		this.totalExp = totalExp;
	}

	public float getBarValue() {
		return barValue;
	}

	public short getLevel() {
		return level;
	}

	public short getTotalExp() {
		return totalExp;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("barValue", barValue)
				.append("level", level)
				.append("totalExp", totalExp)
				.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PlayerExperienceMessage other = (PlayerExperienceMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.barValue, other.barValue)
				.append(this.level, other.level)
				.append(this.totalExp, other.totalExp)
				.isEquals();
	}
}
