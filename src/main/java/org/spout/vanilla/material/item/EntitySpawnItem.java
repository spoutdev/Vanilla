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
package org.spout.vanilla.material.item;

import org.spout.api.entity.Entity;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.Material;
import org.spout.api.math.Vector2;
import org.spout.api.math.Vector3;

import org.spout.vanilla.component.entity.VanillaEntityComponent;

/**
 * An Item Material that spawns an Entity, usually during interaction.
 */
public class EntitySpawnItem<T extends VanillaEntityComponent> extends VanillaItemMaterial {
	private Class<? extends T> spawnedComponent;

	public EntitySpawnItem(short dataMask, String name, int id, Vector2 pos) {
		super(dataMask, name, id, pos);
	}

	public EntitySpawnItem(String name, int id, int data, Material parent, Vector2 pos) {
		super(name, id, data, parent, pos);
	}

	public EntitySpawnItem(String name, int id, Vector2 pos) {
		super(name, id, pos);
	}

	/**
	 * Spawns a new Entity with the Entity Component as specified by {@link #setSpawnedComponent(Class)}. The Entity is spawned an offset away from the Block specified.
	 *
	 * @param at the Block to spawn
	 * @param offset to spawn away from the Block
	 * @return an instance of the spawned component, stored by the spawned entity
	 */
	public T spawnEntity(Block at, Vector3 offset) {
		return spawnEntity(at.getPosition().add(offset));
	}

	/**
	 * Spawns a new Entity with the Entity Component as specified by {@link #setSpawnedComponent(Class)}. The Entity is spawned an offset away from the Entity specified.
	 *
	 * @param by the Entity to spawn
	 * @param offset to spawn away from the Entity
	 * @return an instance of the spawned component, stored by the spawned entity
	 */
	public T spawnEntity(Entity by, Vector3 offset) {
		return spawnEntity(by.getPhysics().getPosition().add(offset));
	}

	/**
	 * Spawns a new Entity with the Entity Component as specified by {@link #setSpawnedComponent(Class)}.
	 *
	 * @param position to spawn the Entity at
	 * @return an instance of the spawned component, stored by the spawned entity
	 */
	@SuppressWarnings ("unchecked")
	public T spawnEntity(Point position) {
		Class<? extends T> component = this.getSpawnedComponent();
		Entity spawned = position.getWorld().createAndSpawnEntity(position, LoadOption.NO_LOAD, component);
		return spawned.add(component);
	}

	/**
	 * Sets the main Entity component that is spawned by this item
	 *
	 * @param spawnedComponent to set to
	 */
	public void setSpawnedComponent(Class<? extends T> spawnedComponent) {
		this.spawnedComponent = spawnedComponent;
	}

	/**
	 * Gets the main Entity component that is spawned by this item
	 *
	 * @return Spawned Entity Component
	 */
	public Class<? extends T> getSpawnedComponent() {
		return this.spawnedComponent;
	}
}
