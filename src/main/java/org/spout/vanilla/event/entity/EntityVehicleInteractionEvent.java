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

import org.spout.vanilla.controller.object.vehicle.Vehicle;

/**
 * Event called when Entities interact with Vehicles. Not yet implemented.
 */
public class EntityVehicleInteractionEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private final Entity entity;
	private final Vehicle vehicle;
	private final EntityVehicleInteractionType type;

	public EntityVehicleInteractionEvent(Entity entity, Vehicle vehicle, EntityVehicleInteractionType interactionType) {
		super(entity);
		this.entity = entity;
		this.vehicle = vehicle;
		this.type = interactionType;
	}

	/**
	 * Gets the entity involved in this interaction.
	 *
	 * @return The entity involved in this interaction.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * Gets the vehicle involved in this interaction.
	 *
	 * @return The vehicle involved in this interaction.
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * Gets the type of interaction between entity and vehicle.
	 *
	 * @return The EntityVehicleInteractionType between entity and vehicle.
	 */
	public EntityVehicleInteractionType getInteractionType() {
		return type;
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
	 * An enum to specify the types of entity/vehicle interactions.
	 */
	public enum EntityVehicleInteractionType {
		/**
		 * When an entity is entering a vehicle.
		 */
		ENTERING,
		/**
		 * When an entity is exiting a vehicle.
		 */
		EXITING,
		/**
		 * When an entity collides with a vehicle (or vice versa).
		 */
		COLLISION,
		/**
		 * A custom interaction type (for use with plugins).
		 */
		CUSTOM
	}
}
