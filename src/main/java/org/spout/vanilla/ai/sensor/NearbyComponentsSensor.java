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
package org.spout.vanilla.ai.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.spout.api.ai.Sensor;
import org.spout.api.ai.goap.PlannerAgent;
import org.spout.api.ai.goap.WorldState;
import org.spout.api.entity.Entity;

import org.spout.vanilla.component.entity.VanillaEntityComponent;

/**
 * Simple Sensor that detects Entities with a specified Component nearby. <p> TODO Probably should have some sort of sight limitation on this sensor. Would be a great optimization.
 */
public class NearbyComponentsSensor implements Sensor {
	private final PlannerAgent agent;
	private final WorldState state;
	private final List<Class<? extends VanillaEntityComponent>> classes;
	private int radius;

	public NearbyComponentsSensor(PlannerAgent agent, Class<? extends VanillaEntityComponent>... classes) {
		this.agent = agent;
		this.state = WorldState.createEmptyState();
		this.classes = new ArrayList<Class<? extends VanillaEntityComponent>>();
		for (Class<? extends VanillaEntityComponent> clazz : classes) {
			this.classes.add(clazz);
		}
	}

	@Override
	public WorldState generateState() {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (Entity entity : agent.getEntity().getWorld().getNearbyEntities(agent.getEntity(), radius)) {
			boolean hasClass = false;
			for (Class clazz : classes) {
				if (entity.get(clazz) != null) {
					hasClass = true;
					break;
				}
			}
			if (!hasClass) {
				continue;
			}
			entities.add(entity);
		}
		boolean found = entities.size() > 0;
		state.put("hasNearbyEntities", found);
		state.put("entities", found ? entities : Collections.emptyList());
		return state;
	}

	/**
	 * Returns all the entities this sensor found.
	 *
	 * @return Entities found
	 */
	public List<Entity> getEntities() {
		final List<Entity> targets = state.get("entities");
		return targets == null ? new ArrayList<Entity>() : targets;
	}

	/**
	 * Returns if the sensor sensed a player nearby.
	 *
	 * @return True if player detected, false if not.
	 */
	public boolean hasFoundEntity() {
		return (Boolean) state.get("hasNearbyEntities");
	}

	/**
	 * Sets the radius the sensor will scan for entities.
	 *
	 * @param radius radius to scan for players
	 */
	public void setSensorRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * Gets the radius this sensor will scan for.
	 *
	 * @return radius the sensor scans for.
	 */
	public int getSensorRadius() {
		return radius;
	}

	/**
	 * Returns the classes this Entity is sensing.
	 *
	 * @return classes the entity is sensing
	 */
	public List<Class<? extends VanillaEntityComponent>> sensing() {
		return Collections.unmodifiableList(classes);
	}

	/**
	 * Adds a new class for the Entity to sense.
	 */
	public void shouldSense(Class<? extends VanillaEntityComponent> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("Must be a non-null class to search for!");
		}
		if (classes.contains(clazz)) {
			return;
		}
		classes.add(clazz);
	}

	/**
	 * Removes a class from being sensed anymore. Ignores classes that are null or aren't contained in the list.
	 *
	 * @param clazz The class to remove
	 */
	public void removeSense(Class<? extends VanillaEntityComponent> clazz) {
		if (clazz == null) {
			return;
		}
		if (!classes.contains(clazz)) {
			return;
		}
		classes.remove(clazz);
	}
}
