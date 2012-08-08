/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.protocol.msg;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.Message;
import org.spout.api.util.SpoutToStringStyle;

public class ClientSettingsMessage implements Message {
	public static byte VIEW_FAR = 0, VIEW_NORMAL = 1, VIEW_SHORT = 2, VIEW_TINY = 3;
	private String locale;
	private byte viewDistance, chatFlags, difficulty;

	public ClientSettingsMessage(String locale, byte viewDistance, byte chatFlags, byte difficulty) {
		this.locale = locale;
		this.viewDistance = viewDistance;
		this.chatFlags = chatFlags;
		this.difficulty = difficulty;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("locale", locale)
				.append("viewdistance", viewDistance)
				.append("chatflags", chatFlags)
				.append("difficulty", difficulty)
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
		final ClientSettingsMessage other = (ClientSettingsMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.locale, other.locale)
				.append(this.viewDistance, other.viewDistance)
				.append(this.chatFlags, other.chatFlags)
				.append(this.difficulty, other.difficulty)
				.isEquals();
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public byte getViewDistance() {
		return viewDistance;
	}

	public void setViewDistance(byte viewDistance) {
		this.viewDistance = viewDistance;
	}

	public byte getChatFlags() {
		return chatFlags;
	}

	public void setChatFlags(byte chatFlags) {
		this.chatFlags = chatFlags;
	}

	public byte getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(byte difficulty) {
		this.difficulty = difficulty;
	}
}
