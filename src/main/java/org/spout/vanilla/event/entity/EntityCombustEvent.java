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
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.AbstractEntityEvent;

import org.spout.vanilla.event.cause.DamageCause.DamageType;
import org.spout.vanilla.event.cause.NullDamageCause;

public class EntityCombustEvent extends AbstractEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private final Cause<?> cause;
	private int duration;

	public EntityCombustEvent(Entity e, int duration) {
		super(e);
		this.duration = duration;
		this.cause = new NullDamageCause(DamageType.UNKNOWN);
	}

	public EntityCombustEvent(Entity e, int duration, Cause<?> cause) {
		super(e);
		this.duration = duration;
		this.cause = cause;
	}

	/**
	 * Gets the time that the entity should burn for.
	 *
	 * @return The time in seconds.
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * Sets the time that the entity should burn for.
	 *
	 * @param duration The time in seconds.
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/**
	 * Gets the {@link Cause} for this event.
	 */
	public Cause<?> getCombustCause() {
		return cause;
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
