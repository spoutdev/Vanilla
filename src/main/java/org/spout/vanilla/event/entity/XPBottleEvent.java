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
package org.spout.vanilla.event.entity;

import org.spout.api.entity.Entity;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;
import org.spout.api.exception.InvalidControllerException;

import org.spout.vanilla.component.substance.object.projectile.XPBottle;

/**
 * Event which is called when a Bottle o' Enchanting is broken and
 * releases experience orbs.
 */
public class XPBottleEvent extends ProjectileHitEvent {
	private static HandlerList handlers = new HandlerList();
	private int experience;
	private boolean showEffect;

	public XPBottleEvent(Entity e, Cause<?> cause, int experience, boolean showEffect) throws InvalidControllerException {
		super(e, cause);
		if (!e.has(XPBottle.class)) {
			throw new InvalidControllerException();
		}
		this.experience = experience;
		this.showEffect = showEffect;
	}

	public XPBottleEvent(Entity e, Cause<?> cause, int experience) {
		this(e, cause, experience, true);
	}

	/**
	 * Gets the amount of experience to be dropped by this
	 * Bottle o' Enchanting breaking.
	 * @return experience
	 */
	public int getExperience() {
		return experience;
	}

	/**
	 * Sets the amount of experience to be dropped by this
	 * Bottle o' Enchanting breaking.
	 * @param experience
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}

	/**
	 * Tells whether the particle effect will be shown when this
	 * Bottle o' Enchanting breaks.
	 * @return boolean
	 */
	public boolean isEffectShown() {
		return showEffect;
	}

	/**
	 * Sets whether the particle effect will be shown when this
	 * Bottle o' Enchanting breaks.
	 * @param boolean
	 */
	public void setEffectShown(boolean showEffect) {
		this.showEffect = showEffect;
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
