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
package org.spout.vanilla.plugin.event.inventory;

import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.inventory.InventoryEvent;

import org.spout.vanilla.api.inventory.Container;
import org.spout.vanilla.plugin.component.living.passive.Villager;

/**
 * Event which is called when a trade with a villager is ended.
 * todo implement calling of this event
 */
public class VillagerEndTradeEvent extends InventoryEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private final Villager villager;
	private final Cause cause;

	public VillagerEndTradeEvent(Villager villager, Cause<?> reason) {
		super(villager.getInventory(),reason);
		this.villager = villager;
		this.cause = reason;
	}

	/**
	 * Returns the villager which caused this event.
	 *
	 * @return villager
	 */
	public Container getEnchantmentTable() {
		return villager;
	}

	/**
	 * Returns the Cause which caused the VillagerEndTradeEvent
	 * @return cause
	 */
	public Cause<?> getCause() {
		return cause;
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
