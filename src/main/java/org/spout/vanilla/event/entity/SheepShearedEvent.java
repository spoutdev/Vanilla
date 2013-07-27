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
import org.spout.api.event.entity.EntityEvent;
import org.spout.api.exception.InvalidControllerException;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.component.entity.living.passive.Sheep;

/**
 * Event which is called when a player shears a sheep.
 */
public class SheepShearedEvent extends EntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private ItemStack itemStack;

	public SheepShearedEvent(Entity sheared, Entity shearer, ItemStack itemStack) {
		super(sheared);
		if (sheared.get(Sheep.class) == null) {
			throw new InvalidControllerException();
		}
		this.itemStack = itemStack;
	}

	/**
	 * Gets the ItemStack the sheered sheep will drop.
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * Sets the ItemStack the sheered sheep will drop.
	 */
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
}
