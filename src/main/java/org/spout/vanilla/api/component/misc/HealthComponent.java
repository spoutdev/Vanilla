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
package org.spout.vanilla.api.component.misc;

import org.spout.api.Spout;
import org.spout.api.component.type.EntityComponent;

import org.spout.vanilla.api.data.VanillaData;
import org.spout.vanilla.api.event.cause.DamageCause;
import org.spout.vanilla.api.event.cause.DamageCause.DamageType;
import org.spout.vanilla.api.event.cause.HealCause;
import org.spout.vanilla.api.event.cause.HealthChangeCause;
import org.spout.vanilla.api.event.cause.NullDamageCause;
import org.spout.vanilla.api.event.entity.EntityHealEvent;

/**
 * Component that adds a health-like attribute to resources.entities.
 */
public abstract class HealthComponent extends EntityComponent {
	protected static final int DEATH_TIME_TICKS = 30;

	// Damage
	protected DamageCause<?> lastDamageCause = new NullDamageCause(DamageType.UNKNOWN);
	protected Object lastDamager;

	protected static final float SCALE = 0.75f; // TODO: Apply directly from engine


	@Override
	public boolean canTick() {
		return true;
	}

	/**
	 * Gets the last cause of the damage
	 * @return the last damager
	 */
	public DamageCause<?> getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Gets the last entity that damages this entity
	 * @return last damager
	 */
	public Object getLastDamager() {
		return lastDamager;
	}

	/**
	 * Gets the maximum health this entity can have
	 * @return the maximum health
	 */
	public int getMaxHealth() {
		return getData().get(VanillaData.MAX_HEALTH);
	}

	/**
	 * Sets the maximum health this entity can have
	 * @param maxHealth to set to
	 */
	public void setMaxHealth(int maxHealth) {
		getData().put(VanillaData.MAX_HEALTH, maxHealth);
	}

	/**
	 * Sets the initial maximum health and sets the health to this value
	 * @param maxHealth of this health component
	 */
	public void setSpawnHealth(int maxHealth) {
		this.setMaxHealth(maxHealth);
		//Do not call setHealth yet, network has not been initialized if loading from file
		getData().put(VanillaData.HEALTH, maxHealth);
	}

	/**
	 * Gets the health of this entity (hitpoints)
	 * @return the health value
	 */
	public int getHealth() {
		return getData().get(VanillaData.HEALTH);
	}

	/**
	 * Sets the current health value for this entity
	 * @param health hitpoints value to set to
	 * @param cause of the change
	 */
	public abstract void setHealth(int health, HealthChangeCause cause);

	/**
	 * Heals this entity
	 * @param amount amount the entity will be healed by
	 */
	public void heal(int amount) {
		heal(amount, HealCause.UNKNOWN);
	}

	/**
	 * Heals this entity with the given {@link org.spout.vanilla.api.event.cause.HealCause}
	 * @param amount amount the entity will be healed by
	 * @param cause cause of this entity being healed
	 */
	public void heal(int amount, HealCause cause) {
		EntityHealEvent event = new EntityHealEvent(getOwner(), amount, cause);
		EntityHealEvent healEvent = Spout.getEngine().getEventManager().callEvent(event);
		if (!healEvent.isCancelled()) {
			setHealth(getHealth() + event.getHealAmount(), HealthChangeCause.HEAL);
		}
	}

	/**
	 * Sets the health value to 0
	 * @param cause of the change
	 */
	public void kill(HealthChangeCause cause) {
		setHealth(0, cause);
	}

	/**
	 * Returns true if the entity is equal to or less than zero health remaining
	 * @return dead
	 */
	public boolean isDead() {
		return getHealth() <= 0;
	}

	/**
	 * Returns true if the entity is currently dying, death ticks is greater than 0.
	 * @return true if the entity is dyring
	 */
	public boolean isDying() {
		return getDeathTicks() > 0;
	}

	/**
	 * @return
	 */
	public int getDeathTicks() {
		return getData().get(VanillaData.DEATH_TICKS);
	}

	/**
	 * @param deathTicks
	 */
	public void setDeathTicks(int deathTicks) {
		if (deathTicks > DEATH_TIME_TICKS) {
			deathTicks = DEATH_TIME_TICKS;
		}
		getData().put(VanillaData.DEATH_TICKS, deathTicks);
	}

	/**
	 * Damages this entity
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 */
	public void damage(int amount) {
		damage(amount, new NullDamageCause(DamageType.UNKNOWN));
	}

	/**
	 * Damages this entity with the given {@link org.spout.vanilla.api.event.cause.DamageCause}.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 */
	public void damage(int amount, DamageCause<?> cause) {
		damage(amount, cause, true);
	}

	/**
	 * Damages this entity with the given {@link org.spout.vanilla.api.event.cause.DamageCause} and damager.
	 * @param amount amount the entity will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this entity being damaged
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public abstract void damage(int amount, DamageCause<?> cause, boolean sendHurtMessage);

	/**
	 * True if the specific entity has an animation when it dies.
	 * @return true if animated death
	 */
	public boolean hasDeathAnimation() {
		return getData().get(VanillaData.HAS_DEATH_ANIMATION);
	}

	/**
	 * Sets whether this entity has a death animation or not.
	 * @param hasDeathAnimation
	 */
	public void setDeathAnimation(boolean hasDeathAnimation) {
		getData().put(VanillaData.HAS_DEATH_ANIMATION, hasDeathAnimation);
	}

	/**
	 * True if the entity has infinite health.
	 * @return true if entity has infinite health
	 */
	public boolean hasInfiniteHealth() {
		return getHealth() == -1;
	}
}
