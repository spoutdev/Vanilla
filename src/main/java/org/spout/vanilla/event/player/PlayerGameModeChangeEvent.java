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

import org.spout.vanilla.data.GameMode;

/**
 * Event called when a player changes game modes.
 */
public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private final GameMode oldMode;
	private GameMode newMode;

	public PlayerGameModeChangeEvent(Player player, GameMode oldMode, GameMode newMode) {
		super(player);
		this.player = player;
		this.oldMode = oldMode;
		this.newMode = newMode;
	}

	/**
	 * Gets the player whose game mode is changing.
	 *
	 * @return The Player whose game mode is changing.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the current game mode the player is in.
	 *
	 * @return The GameMode the player is currently in.
	 */
	public GameMode getOldMode() {
		return oldMode;
	}

	/**
	 * Gets the game mode the player is changing to.
	 *
	 * @return The GameMode the player is changing to.
	 */
	public GameMode getNewMode() {
		return newMode;
	}

	/**
	 * Sets the game mode the player should change to.
	 *
	 * @param newMode The GameMode the player should change to.
	 */
	public void setNewMode(GameMode newMode) {
		this.newMode = newMode;
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
