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
package org.spout.vanilla.event.player.network;

import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.protocol.event.ProtocolEvent;

/**
 * Event which is called when a player enters or leaves a bed
 */
public class PlayerBedEvent extends PlayerEvent implements Cancellable, ProtocolEvent {
	private static HandlerList handlers = new HandlerList();
	private final Block bed;
	private boolean entered;

	public PlayerBedEvent(Player p, Block bed, boolean entered) {
		super(p);
		this.bed = bed;
		this.entered = entered;
	}

	/**
	 * Returns the bed block involved in this event.
	 * @return the bed block involved in this event
	 */
	public Block getBed() {
		return bed;
	}

	/**
	 * Gets if the player entered the bed.
	 * @return True if the bed was entered.
	 */
	public boolean isEntered() {
		return entered;
	}

	/**
	 * Sets if a player has entered the bed.
	 * @param entered The new status of if the player has entered a bed (true or false).
	 */
	public void setEntered(boolean entered) {
		this.entered = entered;
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
