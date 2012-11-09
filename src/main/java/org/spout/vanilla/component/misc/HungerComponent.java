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
import org.spout.api.entity.Player;
import org.spout.vanilla.component.living.Human;
import org.spout.vanilla.data.ExhaustionLevel;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.event.entity.EntityFoodSaturationChangeEvent;
import org.spout.vanilla.event.entity.EntityHungerChangeEvent;
import org.spout.vanilla.event.player.network.PlayerHealthEvent;
import org.spout.vanilla.source.FoodSaturationChangeCause;
import org.spout.vanilla.source.HealthChangeCause;
import org.spout.vanilla.source.HungerChangeCause;

public class HungerComponent extends EntityComponent {

	public static final int MAXIMUM_WAITING_TIME = 80;
	private int counter = 0;
	private boolean infiniteHunger = false;
	
	@Override
	public boolean isDetachable() {
		return false;
	}

	@Override
	public boolean canTick() {
		boolean result = false;
		if (!hasInfiniteHunger()) {
			Human human = getOwner().get(Human.class);
			if (human != null) {
				result = human.isSurvival();
			} else {
				result = true;
			}
		}
		return result;
		
	}

	@Override
	public void onTick(float dt) {

		float distanceMoved = 0f;
		if (getOwner().getTransform() != null) {
			distanceMoved += getOwner().getTransform().getTransformLive().getPosition().distanceSquared(getOwner().getTransform().getTransform().getPosition());
			if (distanceMoved >= 1) {
				addExhaustion(ExhaustionLevel.WALKING.getAmount());
			}
		}
		Human human = getOwner().get(Human.class);
		if (human != null) {
			if (human.isSprinting()) {
				addExhaustion(ExhaustionLevel.SPRINTING.getAmount());
			}
		}
		

		if (counter >= MAXIMUM_WAITING_TIME) {
			counter = 0;
			if (getExhaustion() > 4.0) {
				setExhaustion(getExhaustion() - 4.0f);
				if (getFoodSaturation() > 0.0f) {
					setFoodSaturation(Math.max(getFoodSaturation() - 1f, 0), FoodSaturationChangeCause.EXHAUSTION);
				} else {
					setHunger((short) Math.max(getHunger() - 1, 0), HungerChangeCause.LOW_FOOD_SATURATION);
				}
			}

			if (getHunger() <= 0 && !getOwner().get(HealthComponent.class).isDead()) {
				int maxDrop;
				switch (getOwner().getWorld().getComponentHolder().getData().get(VanillaData.DIFFICULTY)) {
				case EASY:
					maxDrop = 10;
					break;
				case NORMAL:
					maxDrop = 1;
					break;
				default:
					maxDrop = 0;
				}
				if (maxDrop < getOwner().get(HealthComponent.class).getHealth()) {
					HealthComponent health = getOwner().get(HealthComponent.class);
					health.setHealth(health.getHealth() - 1, HealthChangeCause.NO_FOOD);
				}

			} else if (getHunger() >= 18 && getOwner().get(HealthComponent.class).getHealth() < 20) {
				HealthComponent health = getOwner().get(HealthComponent.class);
				health.setHealth(health.getHealth() + 1, HealthChangeCause.REGENERATION);
			}
			if (Spout.debugMode()) {
				Spout.getLogger().info("=== Health update for " + ((Player) getOwner()).getName() + " === ");
				Spout.getLogger().info("Health: " + getOwner().get(HealthComponent.class).getHealth());
				Spout.getLogger().info("Hunger: " + getHunger());
				Spout.getLogger().info("Food Saturation: " + getFoodSaturation());
				Spout.getLogger().info("Exhaustion: " + getExhaustion());
			}
		} else {
			counter++;
		}
	}

	/**
	 * Update the player stats client-side.
	 */
	public void updatePlayer() {
		if (getOwner() instanceof Player) {
			getOwner().getNetwork().callProtocolEvent(new PlayerHealthEvent((Player) getOwner()));
		}
	}

	/**
	 * Retrieve the entity exhaustion.
	 * 
	 * @return the entity exhaustion.
	 */
	public float getExhaustion() {
		return getData().get(VanillaData.EXHAUSTION);
	}

	/**
	 * Sets the entity exhaustion.
	 * 
	 * @param exhaustion The player exhaustion.
	 */
	public void setExhaustion(float exhaustion) {
		getData().put(VanillaData.EXHAUSTION, exhaustion);
	}

	/**
	 * Add exhaustion to a entity.
	 * 
	 * @param exhaustion the exhaustion to add.
	 */
	public void addExhaustion(float exhaustion) {
		setExhaustion(getExhaustion() + exhaustion);
	}

	/**
	 * Get the food saturation of the entity.
	 * 
	 * @return the food saturation of the entity.
	 */
	public float getFoodSaturation() {
		return getData().get(VanillaData.FOOD_SATURATION);
	}

	/**
	 * Set the food saturation of the entity
	 * 
	 * @param foodSaturation The food saturation of the entity.
	 * @param source The source of the change.
	 */
	public void setFoodSaturation(float foodSaturation, final Source source) {
		EntityFoodSaturationChangeEvent event = new EntityFoodSaturationChangeEvent((Player) getOwner(), source, (short) (foodSaturation - getFoodSaturation()));
		Spout.getEngine().getEventManager().callEvent(event);
		if (!event.isCancelled()) {
			if (getFoodSaturation() + event.getChange() > getMaxFoodSaturation()) {
				getData().put(VanillaData.FOOD_SATURATION, getMaxFoodSaturation());
			} else {
				getData().put(VanillaData.FOOD_SATURATION, getFoodSaturation() + event.getChange());
			}
			if (getOwner().has(Human.class)) {
				updatePlayer();
			}
			
		}
	}

	/**
	 * Retrieve the maximum value the food saturation can have.
	 * 
	 * The food saturation can never go higher than the current level of hunger.
	 * 
	 * @return The maximum food saturation.
	 */
	public float getMaxFoodSaturation() {
		return (float) getHunger();
	}

	/**
	 * Retrieve the maximum amount of hunger a eneity can have.
	 * 
	 * @return the maximum amount of hunger.
	 */
	public short getMaxHunger() {
		return getData().get(VanillaData.MAX_HUNGER);
	}

	/**
	 * Sets the maximum amount of hunger a entity can have.
	 * 
	 * @param maxHunger the maximum amount of hunger.
	 */
	public void setMaxHunger(short maxHunger) {
		getData().put(VanillaData.MAX_HUNGER, maxHunger);
	}

	/**
	 * Retrieve the hunger of the entity.
	 * 
	 * @return The hunger level.
	 */
	public short getHunger() {
		return getOwner().getData().get(VanillaData.HUNGER);
	}

	/**
	 * Sets the hunger of the Entity.
	 * 
	 * @param hunger The level of hunger to set to.
	 * @param source The source of the change.
	 */
	public void setHunger(short hunger, Source source) {
		if (EntityHungerChangeEvent.getHandlerList().getRegisteredListeners().length > 0) {
			EntityHungerChangeEvent event = Spout.getEngine().getEventManager().callEvent(new EntityHungerChangeEvent((Player) getOwner(), source, (short) (hunger - getHunger())));
			
			if (!event.isCancelled()) {
				if (getHunger() + event.getChange() > getMaxHunger()) {
					getData().put(VanillaData.HUNGER, getMaxHunger());
				} else {
					getData().put(VanillaData.HUNGER, (short) (getHunger() + event.getChange()));
				}
				if (getOwner().has(Human.class)) {
					updatePlayer();
				}
			}
		}
	}

	/**
	 * Check if the entity have infinite hunger.
	 * @return True if the entity have infinite hunger else false.
	 */
	public boolean hasInfiniteHunger() {
		return infiniteHunger;
	}

	/**
	 * Sets if the entity have infinite hunger.
	 * @param infiniteHunger True for infinite hunger else False.
	 */
	public void setInfiniteHunger(boolean infiniteHunger) {
		this.infiniteHunger = infiniteHunger;
	}
}
