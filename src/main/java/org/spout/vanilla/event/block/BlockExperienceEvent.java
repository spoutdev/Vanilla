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
package org.spout.vanilla.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.block.BlockEvent;
import org.spout.api.geo.cuboid.Block;

/**
 * Event which is called when a modification to a block changes the experience level of the player, for example:
 * <ul>
 * <li>    Breaking a block
 * <li>    Removing a smelted item from a furnace
 * </ul>
 * todo implement calling of this event
 */
public class BlockExperienceEvent extends BlockEvent implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	private int experience;

	public BlockExperienceEvent(Block block, int experience, Cause<?> cause) {
		super(block, cause);
		this.experience = experience;
	}

	/**
	 * Get the experience which is dropped by the block after the event
	 * @return the experience to drop
	 */
	public int getExperienceToDrop() {
		return experience;
	}

	/**
	 * Set the new experience to drop by the block after the event
	 * @param experience exp higher 0 will drop experience
	 */
	public void setNewExperienceToDrop(int experience) {
		this.experience = experience;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		super.setCancelled(cancelled);
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
