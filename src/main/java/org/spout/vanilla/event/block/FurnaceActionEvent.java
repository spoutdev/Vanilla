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
package org.spout.vanilla.event.block;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.block.BlockEvent;

import org.spout.vanilla.component.block.material.Furnace;

/**
 * Event which is called when a furnace is toggled on or off.
 */
public class FurnaceActionEvent extends BlockEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Furnace furnace;
	private final boolean switchON;

	public FurnaceActionEvent(Furnace furnace, Cause<?> reason, boolean switchON) {
		super(furnace.getBlock(), reason);
		this.furnace = furnace;
		this.switchON = switchON;
	}

	/**
	 * Returns the Furnace which caused the FurnaceActionEvent
	 * @return the furnace
	 */
	public Furnace getFurnace() {
		return furnace;
	}

	/**
	 * Returns if the Furnace should be switched on
	 * @return
	 */
	public boolean isSwitchON() {
		return switchON;
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
