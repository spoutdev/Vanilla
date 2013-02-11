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
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.exception.InvalidControllerException;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.plugin.component.living.neutral.Human;

/**
 * Event which is called when a player respawns
 */
public class PlayerRespawnEvent extends EntitySpawnEvent {
	private static HandlerList handlers = new HandlerList();
	private Point point;

	public PlayerRespawnEvent(Entity e, Point point) {
		super(e, point);
		if (e.get(Human.class) == null) {
			throw new InvalidControllerException();
		}
		this.point = point;
	}

	/**
	 * Gets the player associated in this event.
	 * @return The player of the event.
	 */
	public Player getPlayer() {
		return (Player) getEntity();
	}

	/**
	 * Gets the point where the player respawned.
	 * @return
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * Sets the point where the player respawns.
	 * @param point The new location where spawning will take place.
	 */
	public void setPoint(Point point) {
		this.point = point;
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
