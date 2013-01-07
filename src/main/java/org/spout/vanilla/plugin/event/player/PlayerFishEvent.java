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
package org.spout.vanilla.plugin.event.player;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.player.PlayerEvent;

/**
 * Event which is called when a player fishes
 */
public class PlayerFishEvent extends PlayerEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private Entity caught = null;
	private FishingStatus status;

	public PlayerFishEvent(Player p, FishingStatus status) {
		super(p);
		caught = null;
		this.status = status;
	}

	public PlayerFishEvent(Player p, FishingStatus status, Entity caught) {
		super(p);
		this.status = status;
		this.caught = caught;
	}

	/**
	 * Gets the entity of the entity caught by the player
	 * @return Controller of the entity caught by the player, null if fishing,
	 *         bobber has gotten stuck in the ground or nothing has been caught
	 */
	public Entity getCaught() {
		return caught;
	}

	/**
	 * Sets the entity of the entity that as caught.
	 * @param caught Controller of the entity that was caught.
	 */
	public void setCaught(Entity caught) {
		this.caught = caught;
	}

	/**
	 * Gets the status of the fishing event
	 * @return The status of fishing.
	 */
	public FishingStatus getStatus() {
		return status;
	}

	/**
	 * Sets the status of the fishing event.
	 * @param status Sets the status of fishing.
	 */
	public void setStatus(FishingStatus status) {
		this.status = status;
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
	 * An enum to specify the state of the fishing
	 */
	public enum FishingStatus {
		/**
		 * When a player is fishing
		 */
		FISHING,
		/**
		 * When a player has successfully caught an entity
		 */
		CAUGHT_ENTITY,
		/**
		 * When a bobber is stuck in the ground
		 */
		IN_GROUND,
		/**
		 * When a player fails to catch anything while fishing usually due to
		 * poor aiming or timing
		 */
		FAILED_ATTEMPT
	}
}
