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

import org.spout.api.Source;
import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.event.Cancellable;
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;

public class EntityVehicleEvent extends EntityEvent implements Cancellable {

	private static HandlerList handlers = new HandlerList();
	private Controller ridden;
	private Controller rider;
	private VehicleReason reason;

	public EntityVehicleEvent(Entity e, Controller ridden, Controller rider, VehicleReason reason) {
		super(e);
		this.ridden = ridden;
		this.rider = rider;
		this.reason = reason;
	}

	/**
	 * Not exactly sure what this is, but Zidane wanted it
	 * @return Controller
	 */
	public Controller getRidden() {
		return ridden;
	}

	/**
	 * Returns the entity that is the passenger of the vehicle
	 * @return Controller
	 */
	public Controller getRider() {
		return rider;
	}

	/**
	 * Gets the reason for calling this event
	 * @return VehicleReason (reason for event)
	 */
	public VehicleReason getReason() {
		return reason;
	}

	/**
	 * Sets the ridden
	 * @param Controller
	 */
	public void setRidden(Controller ridden) {
		this.ridden = ridden;
	}

	/**
	 * Sets the passenger of the vehicle
	 * @param Controller
	 */
	public void setRider(Controller rider) {
		this.rider = rider;
	}

	/**
	 * Sets the reason for calling the event
	 * @param VehicleReason
	 */
	public void setReason(VehicleReason reason) {
		this.reason = reason;
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

	public enum VehicleReason implements Source {
		/**
		 * Return when vehicle collides with an entity
		 */
		ENTITY_COLLISION,
		/**
		 * Returns when vehicle collides with a block.
		 */
		BLOCK_COLLISION,
		/**
		 * Returns when entity enters vehicle
		 */
		ENTITY_ENTER,
		/**
		 * Returns when entity exits vehicle
		 */
		ENTITY_EXIT,
		/**
		 * Returns when vehicle is placed in world
		 */
		VEHICLE_CREATE,
		/**
		 * Returns when vehicle is damaged
		 */
		VEHICLE_DAMAGE,
		/**
		 * Returns when a vehicle moves.
		 */
		VEHICLE_MOVE;

	}
}

