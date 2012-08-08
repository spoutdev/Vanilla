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
package org.spout.vanilla.controller.component.basic;

import org.spout.api.Source;
import org.spout.api.Spout;
import org.spout.api.event.entity.EntityHealthChangeEvent;
import org.spout.api.geo.discrete.Point;
import org.spout.api.player.Player;
import org.spout.api.tickable.LogicPriority;
import org.spout.api.tickable.LogicRunnable;
import org.spout.vanilla.controller.VanillaEntityController;
import org.spout.vanilla.controller.source.DamageCause;
import org.spout.vanilla.controller.source.HealthChangeReason;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.entity.EntityAnimationEvent;
import org.spout.vanilla.event.entity.EntityStatusEvent;
import org.spout.vanilla.protocol.msg.entity.EntityAnimationMessage;
import org.spout.vanilla.protocol.msg.entity.EntityStatusMessage;

/**
 * A component handling health and death
 */
public class HealthComponent extends LogicRunnable<VanillaEntityController> {
	private static final int DEATH_TIME_TICKS = 30;
	private int deathTicks = -1;
	private int health = 1;
	private int maxHealth = 1;
	private boolean hasDeathAnimation = true;
	// Damage
	private DamageCause lastDamageCause = DamageCause.UNKNOWN;
	private VanillaEntityController lastDamager;

	public HealthComponent(VanillaEntityController parent, LogicPriority priority) {
		super(parent, priority);
	}

	@Override
	public void onRegistration() {
		health = getParent().data().get(VanillaData.HEALTH);
		maxHealth = getParent().data().get(VanillaData.MAX_HEALTH);
	}

	@Override
	public void onUnregistration() {
		//TODO: Verify that this saves properly?
		getParent().data().put(VanillaData.HEALTH, health);
		getParent().data().put(VanillaData.MAX_HEALTH, maxHealth);
	}

	/**
	 * Gets the last cause of the damage
	 * 
	 * @return the last damager
	 */
	public DamageCause getLastDamageCause() {
		return lastDamageCause;
	}

	/**
	 * Gets the last entity that damages this controller
	 * 
	 * @return last damager
	 */
	public VanillaEntityController getLastDamager() {
		return lastDamager;
	}

	/**
	 * Gets the maximum health this controller can have
	 * 
	 * @return the maximum health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Sets the maximum health this controller can have
	 * 
	 * @param maxHealth to set to
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * Sets the initial maximum health and sets the health to this value
	 * 
	 * @param maxHealth of this health component
	 */
	public void setSpawnHealth(int maxHealth) {
		this.maxHealth = maxHealth;
		this.setHealth(maxHealth, HealthChangeReason.SPAWN);
	}

	/**
	 * Gets the health of this controller (hitpoints)
	 * 
	 * @return the health value
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets the current health value for this controller
	 * 
	 * @param health hitpoints value to set to
	 * @param source of the change
	 */
	public void setHealth(int health, Source source) {
		EntityHealthChangeEvent event = new EntityHealthChangeEvent(getParent().getParent(), source, health - this.health);
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (this.health + event.getChange() > maxHealth) {
				this.health = maxHealth;
			} else {
				this.health = this.health + event.getChange();
			}
		}
	}

	/**
	 * Sets the health value to 0
	 * 
	 * @param source of the change
	 */
	public void die(Source source) {
		setHealth(0, source);
	}

	/**
	 * Returns true if the entity is equal to or less than zero health remaining
	 * 
	 * @return dead
	 */
	public boolean isDead() {
		return health <= 0;
	}

	/**
	 * Returns true if the entity is dying
	 * 
	 * @return dying
	 */
	public boolean isDying() {
		return deathTicks > 0;
	}

	/**
	 * Damages this controller with the given {@link DamageCause}.
	 * @param amount amount the controller will be damaged by, can be modified based on armor and enchantments
	 */
	public void damage(int amount) {
		damage(amount, DamageCause.UNKNOWN);
	}

	/**
	 * Damages this controller with the given {@link DamageCause}.
	 * @param amount amount the controller will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this controller being damaged
	 */
	public void damage(int amount, DamageCause cause) {
		damage(amount, cause, true);
	}

	/**
	 * Damages this controller with the given {@link DamageCause}.
	 * @param amount amount the controller will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this controller being damaged
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public void damage(int amount, DamageCause cause, boolean sendHurtMessage) {
		damage(amount, cause, null, sendHurtMessage);
	}

	/**
	 * Damages this controller with the given {@link DamageCause} and damager.
	 * @param amount amount the controller will be damaged by, can be modified based on armor and enchantments
	 * @param cause cause of this controller being damaged
	 * @param damager controller that damaged this controller
	 * @param sendHurtMessage whether to send the hurt packet to all players online
	 */
	public void damage(int amount, DamageCause cause, VanillaEntityController damager, boolean sendHurtMessage) {
		// TODO take potion effects into account
		setHealth(getHealth() - amount, HealthChangeReason.DAMAGE);
		lastDamager = damager;
		lastDamageCause = cause;
		if (sendHurtMessage) {
			Point position = getParent().getParent().getPosition();
			for (Player player : position.getWorld().getNearbyPlayers(position, 128)) {
				player.getNetworkSynchronizer().callProtocolEvent(new EntityAnimationEvent(getParent().getParent(), EntityAnimationMessage.ANIMATION_HURT));
				player.getNetworkSynchronizer().callProtocolEvent(new EntityStatusEvent(getParent().getParent(), EntityStatusMessage.ENTITY_HURT));
			}
		}
	}

	public boolean hasDeathAnimation() {
		return hasDeathAnimation;
	}

	public void setDeathAnimation(boolean hasDeathAnimation) {
		this.hasDeathAnimation = hasDeathAnimation;
	}

	@Override
	public boolean shouldRun(float dt) {
		return true;
	}

	@Override
	public void run() {
		if (this.isDying()) {
			deathTicks--;
			if (deathTicks-- == 0) {
				getParent().kill();
			}
		} else if (this.getHealth() <= 0) {
			if (this.hasDeathAnimation()) {
				deathTicks = DEATH_TIME_TICKS;
				Point position = getParent().getParent().getPosition();
				for (Player player : position.getWorld().getNearbyPlayers(position, 128)) {
					player.getNetworkSynchronizer().callProtocolEvent(new EntityStatusEvent(getParent().getParent(), EntityStatusMessage.ENTITY_DEAD));
				}
			} else {
				getParent().kill();
			}
			getParent().onDeath();
		}
	}
}
