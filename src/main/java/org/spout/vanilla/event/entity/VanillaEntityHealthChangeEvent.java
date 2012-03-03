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
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.event.entity;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityHealthChangeEvent;

public class VanillaEntityHealthChangeEvent extends EntityHealthChangeEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private HealthChangeReason reason;

	public VanillaEntityHealthChangeEvent(Entity e, Source source, int change, HealthChangeReason reason) {
		super(e, source, change);
		this.reason = reason;
	}

	/**
	 * Gets the reason for the change of health.
	 *
	 * @return the reason for the change in health.
	 */
	public HealthChangeReason getReason() {
		return reason;
	}

	/**
	 * Sets the reason for the change of health.
	 *
	 * @param reason the new reason for the change of health.
	 */
	public void setReason(HealthChangeReason reason) {
		this.reason = reason;
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

	/**
	 * An enum to specify the reason behind the change in health.
	 */
	public enum HealthChangeReason {
		/**
		 * Health changed from the difficulty being peaceful.
		 */
		PEACEFUL,
		/**
		 * Health changed from the entity being in contact with an object.
		 */
		CONTACT,
		/**
		 * Health changed from the entity eating.
		 */
		EATING,
		/**
		 * Health changed from the entity being under attack from another
		 * entity.
		 */
		ENTITY_ATTACK,
		/**
		 * Health changed from the entity being collided with a projectile.
		 */
		PROJECTILE,
		/**
		 * Health changed from the entity suffocating.
		 */
		SUFFOCATION,
		/**
		 * Health changed due to a fall.
		 */
		FALL,
		/**
		 * Health changed due to contact with fire.
		 */
		FIRE,
		/**
		 * Health changed due to being on fire.
		 */
		FIRE_TICK,
		/**
		 * Health changed due to being in contact with lava.
		 */
		LAVA,
		/**
		 * Health changed due to being underwater long enough.
		 */
		DROWNING,
		/**
		 * Health changed due to a block's explosion
		 */
		BLOCK_EXPLOSION,
		/**
		 * Health changed due to an entity's explosion.
		 */
		ENTITY_EXPLOSION,
		/**
		 * Health changed due to being in contact with the lower depths of the
		 * void.
		 */
		VOID,
		/**
		 * Health changed due to contact with lightning.
		 */
		LIGHTNING,
		/**
		 * Health changed due to the entity starving.
		 */
		STARVATION,
		/**
		 * Health changed due to a custom reason (normally specified by another
		 * plugin).
		 */
		CUSTOM
	}
}
