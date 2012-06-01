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
package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;
import org.spout.api.exception.InvalidControllerException;

import org.spout.vanilla.controller.living.creature.hostile.Slime;

public class SlimeSplitEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private int amount, size;

	public SlimeSplitEvent(Entity e) throws InvalidControllerException {
		super(e);
		if (!(e.getController() instanceof Slime)) {
			throw new InvalidControllerException();
		}
		amount = 0;
		size = 0;
	}

	/**
	 * Gets the amount of slimes to spawn.
	 * @return The amount of slimes to spawn.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of slimes to spawn
	 * @param amount The amount of slimes to spawn.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets the size of slimes to spawn.
	 * @return The size of the slimes to spawn.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of slimes to spawn.
	 * @param size The size of the slimes to spawn.
	 */
	public void setSize(int size) {
		this.size = size;
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
