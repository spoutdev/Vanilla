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
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.AbstractEntityEvent;

import org.spout.vanilla.event.cause.DamageCause;

/**
 * Event which is called when a Living dies
 */
public class EntityDeathEvent extends AbstractEntityEvent {
	private static final HandlerList handlers = new HandlerList();
	private int dropExp;
	private DamageCause<?> lastDamageCause;
	private Object lastDamager;

	public EntityDeathEvent(Entity e) {
		super(e);
	}

	public EntityDeathEvent(Entity e, DamageCause<?> lastDamageCause, Object lastDamager) {
		super(e);
		this.lastDamageCause = lastDamageCause;
		this.lastDamager = lastDamager;
	}

	/**
	 * Gets the last damage cause.
	 *
	 * @return The last damage cause.
	 */
	public DamageCause<?> getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Sets the last damage cause.
	 *
	 * @param lastDamageCause The last damage cause to set.
	 */
	public void setLastDamageCause(DamageCause<?> lastDamageCause) {
		this.lastDamageCause = lastDamageCause;
	}

	/**
	 * Gets the last damager.
	 *
	 * @return the last damager.
	 */
	public Object getLastDamager() {
		return lastDamager;
	}

	/**
	 * Sets the last damager.
	 *
	 * @param lastDamager the last damager to set.
	 */
	public void setLastDamager(Object lastDamager) {
		this.lastDamager = lastDamager;
	}

	/**
	 * Gets the amount of experience to drop.
	 *
	 * @return The amount of experience to drop.
	 */
	public int getDropExp() {
		return dropExp;
	}

	/**
	 * Sets the amount of experience to drop.
	 *
	 * @param dropExp The experience to set.
	 */
	public void setDropExp(int dropExp) {
		this.dropExp = dropExp;
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
