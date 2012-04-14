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
package org.spout.vanilla.protocol.event.world;

import org.spout.api.protocol.event.ProtocolEvent;
import org.spout.vanilla.controller.living.player.GameMode;

public class StateChangeEvent extends ProtocolEvent {
	private Reason reason;
	private byte gameMode;

	public StateChangeEvent(Reason reason, byte gameMode) {
		this.reason = reason;
		this.gameMode = gameMode;
	}

	public StateChangeEvent(Reason reason, GameMode gameMode) {
		this(reason, gameMode.getId());
	}
	
	public StateChangeEvent(Reason reason) {
		this(reason, (byte) -1);
	}
	
	public Reason getReason() {
		return reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}
	
	public byte getGameMode() {
		return gameMode;
	}

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode.getId();
	}

	public enum Reason {
		INVALID_BED((byte) 0),
		BEGIN_RAINING((byte) 1),
		END_RAINING((byte) 2),
		CHANGE_GAME_MODE((byte) 3),
		ENTER_CREDITS((byte) 4);
		
		private final byte id;
		
		private Reason(byte id) {
			this.id = id;
		}
		
		public byte getId() {
			return id;
		}
	}
}
