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
import org.spout.api.event.HandlerList;
import org.spout.api.event.entity.EntityEvent;

import org.spout.vanilla.source.DamageCause;

public class EntityDamageEvent extends EntityEvent {
	private static HandlerList handlers = new HandlerList();
	private int damage;
	private DamageCause damageCause = DamageCause.UNKNOWN;
	private boolean hasSendHurtMessage = true;
	private Entity damager = null;

	public EntityDamageEvent(Entity e, int damage) {
		super(e);
		this.damage = damage;
	}

	public EntityDamageEvent(Entity e, int damage, DamageCause cause) {
		super(e);
		this.damage = damage;
		this.damageCause = cause;
	}

	public EntityDamageEvent(Entity e, int damage, DamageCause cause, boolean sendHurtMessage) {
		super(e);
		this.damage = damage;
		this.damageCause = cause;
		this.hasSendHurtMessage = sendHurtMessage;
	}

	public EntityDamageEvent(Entity e, int damage, DamageCause cause, boolean sendHurtMessage, Entity damager) {
		super(e);
		this.damage = damage;
		this.damageCause = cause;
		this.hasSendHurtMessage = sendHurtMessage;
		this.damager = damager;
	}

	/**
	 * Returns the entity causing the damage.
	 * Defaults to null.
	 * @return The cause of the damage.
	 */
	public Entity getDamager() {
		return damager;
	}

	/**
	 * Sets the entity causing the damage.
	 * @param damager The entity causing the damage.
	 */
	public void setDamager(Entity damager) {
		this.damager = damager;
	}

	/**
	 * Returns whether or not a hurt message will be sent.
	 * Defaults to true.
	 * @return The cause of the damage.
	 */
	public boolean getSendMessage() {
		return hasSendHurtMessage;
	}

	/**
	 * Sets whether or not to send a hurt message.
	 * @param sendMessage Whether or not to send a hurt message.
	 */
	public void setSendHurtMessage(boolean sendMessage) {
		this.hasSendHurtMessage = sendMessage;
	}

	/**
	 * Gets the cause of the damage. Defaults to UNKNOWN.
	 * @return The cause of the damage.
	 */
	public DamageCause getDamageCause() {
		return damageCause;
	}

	/**
	 * Sets the cause of the damage.
	 * @param damageCause The cause of the damage dealt.
	 */
	public void setDamageCause(DamageCause damageCause) {
		this.damageCause = damageCause;
	}

	/**
	 * Gets the damage dealt to the health component.
	 * @return The damage to the health component.
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Sets the damage to be dealt to the health component.
	 * @param damage The amount of damage dealt.
	 */
	public void setDamage(int damage) {
		this.damage = damage;
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