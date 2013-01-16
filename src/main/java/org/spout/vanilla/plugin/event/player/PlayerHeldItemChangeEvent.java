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
/*
 * This file is part of SpoutAPI.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * SpoutAPI is licensed under the SpoutDev License Version 1.
 *
 * SpoutAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * SpoutAPI is distributed in the hope that it will be useful,
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
/* SpoutAPI is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* In addition, 180 days after any changes are published, you can use the
* software, incorporating those changes, under the terms of the MIT license,
* as described in the SpoutDev License Version 1.
*
* SpoutAPI is distributed in the hope that it will be useful,
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
package org.spout.vanilla.plugin.event.player;

import org.spout.api.entity.Player;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;

/**
 * Event which is called when a player changes the held item
 */
public class PlayerHeldItemChangeEvent extends PlayerEvent {
	private static HandlerList handlers = new HandlerList();
	private final int oldSlot;
	private int newSlot;

	public PlayerHeldItemChangeEvent(Player p, int oldSlot, int newSlot) {
		super(p);
		this.oldSlot = oldSlot;
		this.newSlot = newSlot;
	}

	/**
	 * Gets the previous slot the player had selected.
	 * @return The index of the previous slot.
	 */
	public int getPreviousSlot() {
		return oldSlot;
	}

	/**
	 * Gets the new slot the player selected.
	 * @return The index of the new selected slot.
	 */
	public int getNewSlot() {
		return newSlot;
	}

	/**
	 * Sets the new slot for the player to select. Values should be between 0 and 8.
	 * @param slot index of the new selected slot.
	 */
	public void setNewSlot(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Valid slots are 0-8");
		}
		this.newSlot = slot;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public HandlerList getHandlerList() {
		return handlers;
	}
}
