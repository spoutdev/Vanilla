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
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.AbstractEntityEvent;

import org.spout.vanilla.event.cause.HealthChangeCause;

/**
 * Called when an entity has a health change.<br/> Implements {@link Cancellable}. Canceling this prevents the Entity's health from changing.
 */
public class EntityHealthChangeEvent extends AbstractEntityEvent implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private int change;
	private final HealthChangeCause cause;

	public EntityHealthChangeEvent(Entity e, HealthChangeCause cause, int change) {
		super(e);
		this.cause = cause;
		this.change = change;
	}

	/**
	 * Gets the cause of the event.
	 *
	 * @return The source that caused this event.
	 */
	public HealthChangeCause getCause() {
		return cause;
	}

	/**
	 * Gets the change in health.
	 *
	 * @return The amount of change.
	 */
	public int getChange() {
		return change;
	}

	/**
	 * Sets the change in health.
	 *
	 * @param change The amount of change.
	 */
	public void setChange(int change) {
		this.change = change;
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
