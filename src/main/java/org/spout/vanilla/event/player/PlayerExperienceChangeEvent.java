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

import org.spout.api.Source;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.player.Player;

/**
 * Event called when a player gains/loses experience.
 */
public class PlayerExperienceChangeEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private final int current;
	private int change;
	private Source source;

	public PlayerExperienceChangeEvent(Player player, int current, int change, Source source) {
		super(player);
		this.player = player;
		this.current = current;
		this.change = change;
		this.source = source;
	}

	/**
	 * Gets the player whose experience is changing.
	 *
	 * @return The Player whose experience is changing.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the player's current amount of experience.
	 *
	 * @return The player's current amount of experience.
	 */
	public int getCurrent() {
		return current;
	}

	/**
	 * Gets the amount of the experience change.
	 *
	 * @return The amount of the experience change.
	 */
	public int getChange() {
		return change;
	}

	/**
	 * Sets the amount of the experience change.
	 *
	 * @param change The amount of the experience change.
	 */
	public void setChange(int change) {
		this.change = change;
	}

	/**
	 * Gets the source of the experience change.
	 *
	 * @return The Source of the experience change.
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * Sets the source of the experience change.
	 *
	 * @param source The Source of the experience change.
	 */
	public void setChange(Source source) {
		this.source = source;
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
