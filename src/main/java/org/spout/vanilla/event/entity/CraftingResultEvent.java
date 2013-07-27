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

import org.spout.api.entity.Player;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.AbstractEntityEvent;
import org.spout.api.inventory.ItemStack;

/**
 * Event which is called when a furnace finishes smelting an object, or a CraftingTable finishes crafting, or a player finishes crafting TODO implement event in CraftingInventory
 */
public class CraftingResultEvent extends AbstractEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private final Player player;
	private final ItemStack result;

	public CraftingResultEvent(Player player, ItemStack result) {
		super(player);
		this.player = player;
		this.result = result;
	}

	/**
	 * Returns the player of this CraftingResultEvent
	 *
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the result of this CraftingResultEvent
	 *
	 * @return result
	 */
	public ItemStack getResult() {
		return result;
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
