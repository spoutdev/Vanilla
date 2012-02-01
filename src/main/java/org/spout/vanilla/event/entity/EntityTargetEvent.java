/*
 * This file is part of Vanilla (http://www.spout.org/).
 *
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;

/**
 * Called when an entity targets or untargets another entity.
 */
public class EntityTargetEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private TargetReason reason;

	public EntityTargetEvent(Entity e, TargetReason reason) {
		super(e);
		this.reason = reason;
	}

	/**
	 * Gets the reason for the targeting.
	 * @return The target reason.
	 */
	public TargetReason getReason() {
		return reason;
	}

	/**
	 * Sets the reason for the targeting.
	 * @param reason The reason for the targeting.
	 */
	public void setReason(TargetReason reason) {
		this.reason = reason;
	}

	/**
	 * Returns true if the entity has targeted.
	 * @return True it the entity has targeted, false if not.
	 */
	public boolean isTarget() {
		return reason.isTarget();
	}

	/**
	 * Returns true if the entity has untargeted.
	 * @return True is the entity has untargeted, false if not.
	 */
	public boolean isUntarget() {
		return !reason.isTarget();
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

	public enum TargetReason {
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

		private TargetReason(boolean target) {
			this.target = target;
		}

		public boolean isTarget() {
			return target;
		}
	}
}
