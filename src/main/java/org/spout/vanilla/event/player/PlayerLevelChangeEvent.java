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

import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;

public class PlayerLevelChangeEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private int previousLevel, newLevel;
	private LevelChangeReason reason;

	public PlayerLevelChangeEvent(Player p, int previousLevel, int newLevel, LevelChangeReason reason) {
		super(p);
		this.reason = reason;
		this.previousLevel = previousLevel;
		this.newLevel = newLevel;
	}

	/**
	 * Gets the reason for the change of level.
	 * @return A LevelChangeReason that is the reason for the change in level.
	 */
	public LevelChangeReason getReason() {
		return reason;
	}

	/**
	 * Sets the reason for the change of level.
	 * @param reason A LevelChangeReason that sets the reason for the change of level.
	 */
	public void setReason(LevelChangeReason reason) {
		this.reason = reason;
	}

	/**
	 * Gets the previous level before the level change occurred.
	 * @return an int that is the number of the last level.
	 */
	public int getPreviousLevel() {
		return previousLevel;
	}

	/**
	 * Gets the new level after the level change occurred.
	 * @return an int that is the number of the new level.
	 */
	public int getNewLevel() {
		return newLevel;
	}

	/**
	 * Sets the level of the player regardless of what level was set in the event.
	 * @param customLevel an int that is the custom number of the level to set.
	 */
	public void setLevel(int customLevel) {
		newLevel = customLevel;
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

	/**
	 * An enum to specify the reason behind the level change
	 */
	public enum LevelChangeReason {
		/**
		 * Change in food level
		 */
		FOOD,
		/**
		 * Change in experience level
		 */
		EXP,
		/**
		 * A custom reason (normally a plugin)
		 */
		CUSTOM
	}
}
