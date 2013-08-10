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
	 * Spawns a new Entity with the Entity Component as specified by {@link #setSpawnedComponent(Class)}.
	 * The Entity is spawned an offset away from the Block specified.
	 * 
	 * @param at the Block to spawn
	 * @param offset to spawn away from the Block
	 * @return an instance of the spawned component, stored by the spawned entity
	 */
	public T spawnEntity(Block at, Vector3 offset) {
		return spawnEntity(at.getPosition().add(offset));
	}

	/**
	 * Spawns a new Entity with the Entity Component as specified by {@link #setSpawnedComponent(Class)}.
	 * The Entity is spawned an offset away from the Entity specified.
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
	@SuppressWarnings("unchecked")
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
