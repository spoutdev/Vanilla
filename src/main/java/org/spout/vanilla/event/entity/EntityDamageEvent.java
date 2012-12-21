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
import org.spout.api.event.Cancellable;
import org.spout.api.event.Cause;
import org.spout.api.event.HandlerList;

import org.spout.vanilla.event.cause.DamageCause;
import org.spout.vanilla.event.cause.DamageCause.DamageType;
import org.spout.vanilla.event.cause.HealthChangeCause;
import org.spout.vanilla.event.cause.NullDamageCause;

/**
 * Called when an entity is damaged.<br/>
 * Implements {@link Cancellable}. Canceling this prevents the Entity from taking damage.
 */
public class EntityDamageEvent extends EntityHealthChangeEvent {
	private static HandlerList handlers = new HandlerList();
	private boolean hasSendHurtMessage = true;
	private final Cause<?> cause;

	public EntityDamageEvent(Entity e, int damage) {
		super(e, HealthChangeCause.DAMAGE, -damage);
		this.cause = new NullDamageCause(DamageType.UNKNOWN);
	}

	public EntityDamageEvent(Entity e, int damage, Cause<?> cause) {
		super(e, HealthChangeCause.DAMAGE, -damage);
		this.cause = cause;
	}

	public EntityDamageEvent(Entity e, int damage, Cause<?> cause, boolean sendHurtMessage) {
		super(e, HealthChangeCause.DAMAGE, -damage);
		this.cause = cause;
		this.hasSendHurtMessage = sendHurtMessage;
	}

	/**
	 * Returns the object causing the damage, such as a block or entity.
	 * Defaults to null.
	 * @return The source of the damage.
	 */
	public Object getDamager() {
		return cause.getSource();
	}

	/**
	 * Gets the type of damage. Defaults to UNKNOWN.
	 * @return type
	 */
	public DamageType getDamageType() {
		if (cause instanceof DamageCause) {
			return ((DamageCause<?>)cause).getType();
		}
		return DamageType.UNKNOWN;
	}

	/**
	 * Gets the {@link Cause} for this event.
	 */
	public Cause<?> getDamageCause() {
		return cause;
	}

	/**
	 * Returns whether or not a hurt message will be sent.
	 * Defaults to true.
	 * @return boolean
	 */
	public boolean getSendMessage() {
		return hasSendHurtMessage;
	}

	/**
	 * Sets whether or not to send a hurt message.
	 * @param sendMessage
	 */
	public void setSendHurtMessage(boolean sendMessage) {
		this.hasSendHurtMessage = sendMessage;
	}

	/**
	 * Gets the damage dealt to the health component.
	 * @return The damage to the health component.
	 */
	public int getDamage() {
		return -getChange();
	}

	/**
	 * Sets the damage to be dealt to the health component.
	 * @param damage The amount of damage dealt.
	 */
	public void setDamage(int damage) {
		setChange(-damage);
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
