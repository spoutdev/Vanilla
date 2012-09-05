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
package org.spout.vanilla.components.basic;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.event.entity.EntityHealthChangeEvent;

import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.entity.EntityStatusEvent;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.source.DamageCause;
import org.spout.vanilla.source.HealthChangeCause;

/**
 * Component that adds a health-like attribute to entities.
 */
public class HealthComponent extends EntityComponent {
	//animation
	public boolean hasDeathAnimation = true;
	//damage
	private DamageCause lastDamageCause = DamageCause.UNKNOWN;
	private Entity lastDamager;
	private int deathTicks = 0;

	public HealthComponent() {

	}

	/**
	 * Gets the last cause of the damage
	 * @return the last damager
	 */
	public DamageCause getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Gets the last entity that damages this entity
	 * @return last damager
	 */
	public Entity getLastDamager() {
		return lastDamager;
	}

	/**
	 * Gets the maximum health this entity can have
	 * @return the maximum health
	 */
	public int getMaxHealth() {
		return getDatatable().get(VanillaData.MAX_HEALTH);
	}

	/**
	 * Sets the maximum health this entity can have
	 * @param maxHealth to set to
	 */
	public void setMaxHealth(int maxHealth) {
		getDatatable().put(VanillaData.MAX_HEALTH, maxHealth);
	}

	/**
	 * Sets the initial maximum health and sets the health to this value
	 * @param maxHealth of this health component
	 */
	public void setSpawnHealth(int maxHealth) {
		this.setMaxHealth(maxHealth);
		this.setHealth(maxHealth, HealthChangeCause.SPAWN);
	}

	/**
	 * Gets the health of this entity (hitpoints)
	 * @return the health value
	 */
	public int getHealth() {
		return getDatatable().get(VanillaData.HEALTH);
	}

	/**
	 * Sets the current health value for this entity
	 * @param health hitpoints value to set to
	 * @param source of the change
	 */
	public void setHealth(int health, Source source) {
		EntityHealthChangeEvent event = new EntityHealthChangeEvent(getHolder(), source, health - getHealth());
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (getHealth() + event.getChange() > getMaxHealth()) {
				getDatatable().put(VanillaData.HEALTH, getMaxHealth());
			} else {
				getDatatable().put(VanillaData.HEALTH, getHealth() + event.getChange());
			}
		}
	}

	/**
	 * Sets the health value to 0
	 * @param source of the change
	 */
	public void kill(Source source) {
		setHealth(0, source);
	}

	/**
	 * Returns true if the entity is equal to or less than zero health remaining
	 * @return dead
	 */
	public boolean isDead() {
		return getHealth() <= 0;
	}

	/**
	 * Returns true if the entity is dying
	 * @return dying
	 */
	public boolean isDying() {
		return deathTicks > 0;
	}

	/**
	 * Damages this entity with the given {@link DamageCause}.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 */
	public void damage(int amount) {
		damage(amount, DamageCause.UNKNOWN);
	}

	/**
	 * Damages this entity with the given {@link DamageCause}.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 */
	public void damage(int amount, DamageCause cause) {
		damage(amount, cause, true);
	}

	/**
	 * Damages this entity with the given {@link DamageCause}.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public void damage(int amount, DamageCause cause, boolean sendHurtMessage) {
		damage(amount, cause, null, sendHurtMessage);
	}

	/**
	 * Damages this entity with the given {@link DamageCause} and damager.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 * @param damager entity that damaged this entity
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public void damage(int amount, DamageCause cause, Entity damager, boolean sendHurtMessage) {
		// TODO take potion effects into account
		setHealth(getHealth() - amount, HealthChangeCause.DAMAGE);
		lastDamager = damager;
		lastDamageCause = cause;
		if (sendHurtMessage) {
			getHolder().getNetwork().getHolder().getNetwork().callProtocolEvent(new EntityAnimationEvent(getHolder(), EntityAnimationMessage.ANIMATION_HURT));
			getHolder().getNetwork().callProtocolEvent(new EntityStatusEvent(getHolder(), EntityStatusMessage.ENTITY_HURT));
			//getHurtEffect().playGlobal(getParent().getParent().getPosition());
		}
	}

	public boolean hasDeathAnimation() {
		return hasDeathAnimation;
	}

	public void setDeathAnimation(boolean hasDeathAnimation) {
		this.hasDeathAnimation = hasDeathAnimation;
	}
}
