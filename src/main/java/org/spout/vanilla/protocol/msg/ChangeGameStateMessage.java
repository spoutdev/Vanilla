/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import org.spout.api.protocol.Message;

import org.spout.vanilla.controller.living.player.GameMode;

public final class ChangeGameStateMessage extends Message {
	public static final byte INVALID_BED = 0;
	public static final byte BEGIN_RAINING = 1;
	public static final byte END_RAINING = 2;
	public static final byte CHANGE_GAME_MODE = 3;
	public static final byte ENTER_CREDITS = 4;
	private final byte reason;
	private GameMode gameMode;

	public ChangeGameStateMessage(byte reason, GameMode gameMode) {
		this.reason = reason;
		this.gameMode = gameMode;
	}

	public ChangeGameStateMessage(byte reason) {
		this(reason, GameMode.CREATIVE);
	}

	public byte getReason() {
		return reason;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	@Override
	public String toString() {
		return "StateChangeMessage{reason=" + reason + ",gamemode=" + gameMode.getId() + "}";
	}
}
