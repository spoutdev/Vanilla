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
package org.spout.vanilla.api.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;

/**
 * Event which is called when an Entity targets something
 */
public class EntityTargetEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();
	private TargetCause cause;
	private Entity target = null;

	public EntityTargetEvent(Entity e, TargetCause cause) {
		super(e);
		this.cause = cause;
	}

	public EntityTargetEvent(Entity e, TargetCause cause, Entity target) {
		this(e, cause);
		this.target = target;
	}

	/**
	 * Gets the cause for targeting.
	 * @return the target cause
	 */
	public TargetCause getCause() {
		return cause;
	}

	/**
	 * Sets the cause for the targeting.
	 * @param cause The reason for the targeting
	 */
	public void setCause(TargetCause cause) {
		this.cause = cause;
	}

	/**
	 * Gets the new target.
	 * @return target or null
	 */
	public Entity getTarget() {
		return target;
	}

	/**
	 * Sets the new target.
	 * @param target
	 */
	public void setTarget(Entity target) {
		this.target = target;
	}

	/**
	 * Whether this is a targeting or untargeting event.
	 * @return true if the entity is targeting, false if it is untargeting
	 */
	public boolean isTargeting() {
		return cause.isTarget();
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

	/**
	 * Represents a target change cause for an EntityTargetEvent.
	 */
	public enum TargetCause {
		TARGET_DIED(false),
		CLOSEST_PLAYER(true),
		TARGET_ATTACKED_ENTITY(true),
		PIG_ZOMBIE_MASSACRE(true),
		FORGOT_TARGET(false),
		OWNER_ATTACKED(true),
		RANDOM_TARGET(true),
		CUSTOM_TARGET(true),
		CUSTOM_UNTARGET(false);
		private boolean target;

		private TargetCause(boolean target) {
			this.target = target;
		}

		/**
		 * Whether this is targeting or untargeting.
		 * @return true if targeting, false if untargeting
		 */
		public boolean isTarget() {
			return target;
		}
	}
}

