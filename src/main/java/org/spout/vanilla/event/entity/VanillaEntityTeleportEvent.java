/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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
package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityTeleportEvent;
import org.spout.api.geo.discrete.Point;

/**
 * Event which is called when a Living gets teleported
 */
public class VanillaEntityTeleportEvent extends EntityTeleportEvent {
	private static final HandlerList handlers = new HandlerList();
	private TeleportReason reason;

	public VanillaEntityTeleportEvent(Entity e, Point from, Point to, TeleportReason reason) {
		super(e, from, to);
		this.reason = reason;
	}

	/**
	 * Gets the reason behind the teleport of the entity.
	 *
	 * @return The reason behind the teleport.
	 */
	public TeleportReason getReason() {
		return reason;
	}

	/**
	 * Sets the reason for the teleport of the entity.
	 *
	 * @param reason The new reason for the teleport of the entity.
	 */
	public void setReason(TeleportReason reason) {
		this.reason = reason;
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
	 * An enum to specify the reason behind teleports.
	 */
	public enum TeleportReason {
		/**
		 * Teleportation via a portal
		 */
		PORTAL,
		/**
		 * Teleportation due to a custom reason (normally a plugin).
		 */
		CUSTOM
	}
}
