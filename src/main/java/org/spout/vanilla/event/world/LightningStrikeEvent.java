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
package org.spout.vanilla.event.world;

import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.world.WorldEvent;
import org.spout.api.geo.World;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.controller.object.misc.Lightning;

/**
 * Event called when lightning strikes. Not yet implemented.
 */
public class LightningStrikeEvent extends WorldEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final World world;
	private final Lightning bolt;
	private Point location;

	public LightningStrikeEvent(World world, Lightning bolt, Point location) {
		super(world);
		this.world = world;
		this.bolt = bolt;
		this.location = location;
	}

	/**
	 * Gets the world this lightning strike is occurring in.
	 *
	 * @return The World this lightning strike is occurring in.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the lightning bolt involved.
	 *
	 * @return The Lightning involved.
	 */
	public Lightning getLightningBolt() {
		return bolt;
	}

	/**
	 * Gets the location the lightning strike is occurring at.
	 *
	 * @return The Point the lightning strike is occurring at.
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Sets the location the lightning strike should occur at.
	 *
	 * @param location The Point the lightning strike should occur at.
	 */
	public void setLocation(Point location) {
		this.location = location;
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