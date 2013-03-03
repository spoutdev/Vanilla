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
 * Fired when the redstone power at a block changes
 */
public class RedstoneChangeEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final int prev;
	private int current;

	public RedstoneChangeEvent(Block block, Cause<?> reason, int prev, int current) {
		super(block, reason);
		this.prev = prev;
		this.current = current;
	}

	/**
	 * Gets the redstone power level before the block change occurred.
	 * <p/>
	 * <p>Note: Because RedstoneChangeEvent occurs <i>before</i> the block
	 * change event has finished, you can also inspect the block to find the
	 * previous power.</p>
	 * @return prev power
	 */
	public int getPreviousPower() {
		return prev;
	}

	/**
	 * Gets the new power level the redstone source will have after this
	 * block change finishes.
	 * <p/>
	 * <p>Note: Because RedstoneChangeEvent occurs <i>before</i> the block
	 * change event has finished, neighbor blocks may not yet report that they
	 * are powered. To perform an event after a redstone change, use the scheduler.</p>
	 * @return new power level
	 */
	public int getNewPower() {
		return current;
	}

	/**
	 * Sets the new power level the redstone source will have after this
	 * block change finishes.
	 * <p/>
	 * <p>Note: Because RedstoneChangeEvent occurs <i>before</i> the block
	 * change event has finished, neighbor blocks may not yet report that they
	 * are powered. To perform an event after a redstone change, use the scheduler.</p>
	 * @param newCurrent new power level
	 */
	public void setNewPower(int newCurrent) {
		current = newCurrent;
	}

	/**
	 * Cancels the redstone update
	 */
	@Override
	public void setCancelled(boolean cancel) {
		super.setCancelled(cancel);
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
