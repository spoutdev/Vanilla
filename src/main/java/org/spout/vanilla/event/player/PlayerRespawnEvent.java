/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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

import org.spout.api.entity.Entity;
import org.spout.api.entity.PlayerController;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntitySpawnEvent;
import org.spout.api.exception.InvalidControllerException;
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;

public class PlayerRespawnEvent extends EntitySpawnEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private boolean bedRespawn;

	public PlayerRespawnEvent(Entity e, Point point, boolean bedRespawn) {
		super(e, point);
		if (!(e.getController() instanceof PlayerController)) {
			throw new InvalidControllerException();
		}
		this.bedRespawn = bedRespawn;
	}

	/**
	 * Gets the player associated in this event.
	 * @return The player of the event.
	 */
	public Player getPlayer() {
		return (Player) getEntity();
	}

	/**
	 * Returns true if the respawn location is a bed.
	 * @return True if the respawn location is a bed.
	 */
	public boolean isBedRespawn() {
		return bedRespawn;
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
