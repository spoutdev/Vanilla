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
import org.spout.api.event.HandlerList;

import org.spout.vanilla.event.cause.HealCause;
import org.spout.vanilla.event.cause.HealthChangeCause;

public class EntityHealEvent extends EntityHealthChangeEvent {
	private static HandlerList handlers = new HandlerList();
	private final HealCause cause;

	public EntityHealEvent(Entity e, int heal) {
		super(e, HealthChangeCause.HEAL, heal);
		this.cause = HealCause.UNKNOWN;
	}

	public EntityHealEvent(Entity e, int heal, HealCause cause) {
		super(e, HealthChangeCause.DAMAGE, heal);
		this.cause = cause;
	}

	/**
	 * Gets the type of damage. Defaults to UNKNOWN.
	 * @return type
	 */
	public HealCause getHealCause() {
		return cause;
	}

	/**
	 * Gets the damage dealt to the health component.
	 * @return The damage to the health component.
	 */
	public int getHealAmount() {
		return getChange();
	}

	/**
	 * Sets the damage to be dealt to the health component.
	 * @param damage The amount of damage dealt.
	 */
	public void setHealAmount(int amount) {
		setChange(amount);
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
