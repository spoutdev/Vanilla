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
package org.spout.vanilla.event.player;

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.player.Player;

/**
 * Event called when a player is poisoned.
 */
public class PlayerPoisonStatusChangeEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private boolean poisoned;

	public PlayerPoisonStatusChangeEvent(Player player, boolean poisoned) {
		super(player);
		this.player = player;
		this.poisoned = poisoned;
	}

	/**
	 * Gets the player being poisoned.
	 *
	 * @return The Player being poisoned.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets whether the player is being poisoned or not.
	 *
	 * @return TRUE if the player is being poisoned, FALSE if the player is having poison removed.
	 */
	public boolean getPoisoned() {
		return this.poisoned;
	}

	/**
	 * Sets whether the player should be poisoned or not.
	 *
	 * @param poisoned TRUE if the player should be poisoned, FALSE if the player should have it removed.
	 */
	public void setPoisoned(boolean poisoned) {
		this.poisoned = poisoned;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
