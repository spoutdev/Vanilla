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
package org.spout.vanilla.component.misc;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.component.components.EntityComponent;
import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.event.entity.EntityHealthChangeEvent;

import org.spout.vanilla.data.Animation;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.entity.EntityDamageEvent;
import org.spout.vanilla.event.entity.EntityStatusEvent;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;
import org.spout.vanilla.source.DamageCause;
import org.spout.vanilla.source.HealthChangeCause;

/**
 * Component that adds a health-like attribute to entities.
 */
public class HealthComponent extends EntityComponent {
	private static final int DEATH_TIME_TICKS = 30;
	//damage
	private DamageCause lastDamageCause = DamageCause.UNKNOWN;
	private Entity lastDamager;

	@Override
	public boolean canTick() {
		return true;
	}

	@Override
	public void onTick(float dt) {
		if (isDying()) {
			setDeathTicks(getDeathTicks() - 1);
			if (getDeathTicks() <= 0) {
				if (!(getOwner() instanceof Player)) {
					getOwner().remove();
				}
			}
		} else if (isDead()) {
			if (hasDeathAnimation()) {
				setDeathTicks(DEATH_TIME_TICKS);
				getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_DEAD));
			} else {
				if (!(getOwner() instanceof Player)) {
					getOwner().remove();
				}
			}
			onDeath();
		}
	}

	/**
	 * Called when the entities' health hits zero and is considered "dead" by Vanilla game standards
	 */
	public void onDeath() {

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
		this.setHealth(maxHealth, HealthChangeCause.SPAWN);
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
	 * @param source of the change
	 */
	public void setHealth(int health, Source source) {
		EntityHealthChangeEvent event = new EntityHealthChangeEvent(getOwner(), source, health - getHealth());
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (getHealth() + event.getChange() > getMaxHealth()) {
				getData().put(VanillaData.HEALTH, getMaxHealth());
			} else {
				getData().put(VanillaData.HEALTH, getHealth() + event.getChange());
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
	 * @return
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
		EntityDamageEvent event = Spout.getEngine().getEventManager().callEvent(new EntityDamageEvent(getOwner(), amount, cause, sendHurtMessage, damager));
		if (event.isCancelled()) {
			return;
		}
		setHealth(getHealth() - event.getDamage(), HealthChangeCause.DAMAGE);
		lastDamager = event.getDamager();
		lastDamageCause = event.getDamageCause();
		if (event.getSendMessage()) {
			getOwner().getNetwork().callProtocolEvent(new EntityAnimationEvent(getOwner(), Animation.DAMAGE_ANIMATION));
			getOwner().getNetwork().callProtocolEvent(new EntityStatusEvent(getOwner(), EntityStatusMessage.ENTITY_HURT));
			//getHurtEffect().playGlobal(getParent().getParent().getPosition());
		}
	}

	public boolean hasDeathAnimation() {
		return getData().get(VanillaData.HAS_DEATH_ANIMATION);
	}

	public void setDeathAnimation(boolean hasDeathAnimation) {
		getData().put(VanillaData.HAS_DEATH_ANIMATION, hasDeathAnimation);
	}

	public boolean hasInfiniteHealth() {
		return getHealth() == -1;
	}
}