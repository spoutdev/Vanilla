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

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.spout.api.Source;
import org.spout.api.entity.Entity;
import org.spout.api.event.HandlerList;
import org.spout.api.exception.InvalidControllerException;

import org.spout.vanilla.component.substance.Potion;

/**
 * Event which is called when a potion splashes
 */
public class PotionSplashEvent extends ProjectileHitEvent {
	private static HandlerList handlers = new HandlerList();
	private final Map<Entity, Double> entitiesAffected;
	private Potion potion;

	public PotionSplashEvent(Entity e, Source source, Map<Entity, Double> entitiesAffected) throws InvalidControllerException {
		super(e, source);
		if (!e.has(Potion.class)) {
			throw new InvalidControllerException();
		}
		this.entitiesAffected = entitiesAffected;
		potion = e.get(Potion.class);
	}

	/**
	 * Gets the potion that was thrown
	 * @return The potion thrown.
	 */
	public Potion getPotionThrown() {
		return potion;
	}

	/**
	 * Sets the potion that was thrown.
	 * @param potion The potion that is thrown.
	 */
	public void setPotionThrown(Potion potion) {
		this.potion = potion;
	}

	/**
	 * Gets a list of all of the effected entities.
	 * @return A list of the affected entities.
	 */
	public Collection<Entity> getAffectedEntities() {
		return new LinkedList<Entity>(entitiesAffected.keySet());
	}

	/**
	 * Gets the intensity of the potion effect for an entity.
	 * @param entity The entity to get the intensity for
	 * @return The intensity of the potion effect. 0.0 symbolizes no effect, 1.0 symbolizes full effect.
	 */
	public double getIntensityFor(Entity entity) {
		Double temp = entitiesAffected.get(entity);
		if (temp == null) {
			return 0.0;
		}
		return temp;
	}

	/**
	 * Sets the intensity of the potion effect for an entity.
	 * @param entity The entity that will have a new defined intensity.
	 * @param intensity The intensity of the potion effect.
	 */
	public void setIntensityFor(Entity entity, double intensity) {
		if (intensity <= 0.0) {
			entitiesAffected.remove(entity);
		} else {
			entitiesAffected.put(entity, Math.min(intensity, 1.0));
		}
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
