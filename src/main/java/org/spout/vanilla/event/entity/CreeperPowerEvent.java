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
import org.spout.api.exception.InvalidControllerException;
import org.spout.vanilla.entity.living.hostile.Creeper;

/*
 * Called when a creeper gains or looses a powered state.
 */
public class CreeperPowerEvent extends EntityEvent implements Cancellable {
	private static HandlerList handlers = new HandlerList();

	private PoweredReason reason;

	private Entity lightning;

	public CreeperPowerEvent(Entity e, Entity lightning) throws InvalidControllerException {
		super(e);
		if (!(e.getController() instanceof Creeper))
			throw new InvalidControllerException();
		this.reason = PoweredReason.LIGHTNING;
		this.lightning = lightning;
	}

	public CreeperPowerEvent(Entity e, Entity lightning, PoweredReason reason) throws InvalidControllerException {
		super(e);
		if (!(e.getController() instanceof Creeper))
			throw new InvalidControllerException();
		this.reason = reason;
		this.lightning = lightning;
		// TODO Auto-generated constructor stub
	}

	/*
	 * This gets the entity that represents the lightning that caused the change in the creeper's state.
	 * @return an Entity that represents the lightning.
	 */
	public Entity getLightning() {
		return lightning;
	}

	/*
	 * This sets the entity that represents the lightning that caused the change in the creeper's state.
	 * @param lightning an Entity that represents the new cause of the change in the creeper's state.
	 */
	public void setLightning(Entity lightning) {
		this.lightning = lightning;
	}

	/*
	 * This gets the reason behind the change in the creeper's state.
	 * @return a PoweredReason that represents the change in the creeper's state.
	 */
	public PoweredReason getReason() {
		return reason;
	}

	/*
	 * This sets the reason for the change in the creeper's state.
	 * @param reason a PoweredReason that is the reason for the change in the creeper's state.
	 */
	public void setReason(PoweredReason reason) {
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

	/**
	 * An enum to specify the reason behind the change in the creeper's status.
	 */
	public enum PoweredReason {
		/**
		 * Powered by lightning.
		 */
		LIGHTNING,
		/**
		 * Powered by a custom reason (normally a plugin).
		 */
		CUSTOM_ON,
		/**
		 * Powered removed by a custom reason (normally a plugin).
		 */
		CUSTOM_OFF;

		private PoweredReason() {
		}
	}
}