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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.player.Player;

/**
 * Event called when a player interacts with a bed.
 */
public class PlayerBedInteractionEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Player player;
	private final Block bedBlock;
	private PlayerBedInteractionType type;

	public PlayerBedInteractionEvent(Player player, Block bedBlock, PlayerBedInteractionType type) {
		super(player);
		this.player = player;
		this.bedBlock = bedBlock;
		this.type = type;
	}

	/**
	 * Gets the player interacting with the bed.
	 *
	 * @return The Player interacting with the bed.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the bed block being interacted with.
	 *
	 * @return The bed Block being interacted with.
	 */
	public Block getBedBlock() {
		return bedBlock;
	}

	/**
	 * Gets the type of interaction of this event.
	 *
	 * @return The PlayerBedInteractionType of this event.
	 */
	public PlayerBedInteractionType getType() {
		return type;
	}

	/**
	 * Sets the type of interaction this event should be.
	 *
	 * @param type The PlayerBedInteractionType this event should be.
	 */
	public void setType(PlayerBedInteractionType type) {
		this.type = type;
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
	 * An enum to specify the types of player/bed interactions.
	 */
	public enum PlayerBedInteractionType {
		/**
		 * When a player is entering a bed.
		 */
		ENTERING,
		/**
		 * When a player is exiting a bed.
		 */
		EXITING,
		/**
		 * A custom interaction type (for use with plugins).
		 */
		CUSTOM
	}
}