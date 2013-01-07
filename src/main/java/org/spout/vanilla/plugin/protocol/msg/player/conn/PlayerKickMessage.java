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
package org.spout.vanilla.plugin.protocol.msg.player.conn;

import org.apache.commons.lang3.builder.ToStringBuilder;

import org.spout.api.protocol.proxy.RedirectMessage;
import org.spout.api.util.SpoutToStringStyle;

import org.spout.vanilla.plugin.protocol.msg.VanillaMainChannelMessage;

public final class PlayerKickMessage extends VanillaMainChannelMessage implements RedirectMessage {
	private final String reason;
	private String hostname;
	private int port;

	public PlayerKickMessage(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, SpoutToStringStyle.INSTANCE)
				.append("reason", reason)
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
		final PlayerKickMessage other = (PlayerKickMessage) obj;
		return new org.apache.commons.lang3.builder.EqualsBuilder()
				.append(this.reason, other.reason)
				.isEquals();
	}

	private static String redirectDetected(String reason) {
		String hostname = null;
		int portnum = -1;

		if (reason.indexOf("[Serverport]") != 0 && reason.indexOf("[Redirect]") != 0) {
			return null;
		}

		String[] split = reason.split(":");
		if (split.length == 3) {
			hostname = split[1].trim();
			try {
				portnum = Integer.parseInt(split[2].trim());
			} catch (Exception e) {
				portnum = -1;
			}
		} else if (split.length == 2) {
			hostname = split[1].trim();
			portnum = 25565;
		}

		int commaPos = reason.indexOf(",");
		if (commaPos >= 0) {
			return reason.substring(reason.indexOf(":") + 1).trim();
		}

		if (portnum != -1) {
			return hostname + ":" + portnum;
		} else {
			return null;
		}
	}

	@Override
	public boolean isRedirect() {
		String redirect = redirectDetected(reason);
		if (redirect == null) {
			return false;
		}
		String[] split = redirect.split(":");
		hostname = split[0];
		if (split.length < 2) {
			port = 25565;
		} else {
			port = Integer.parseInt(split[1]);
		}

		return true;
	}

	@Override
	public String getHostname() {
		return hostname;
	}

	@Override
	public int getPort() {
		return port;
	}
}
