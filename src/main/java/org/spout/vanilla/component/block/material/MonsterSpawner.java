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
package org.spout.vanilla.component.block.material;

import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.entity.Player;
import org.spout.api.geo.LoadOption;
import org.spout.api.geo.discrete.Point;

import org.spout.vanilla.component.block.VanillaBlockComponent;
import org.spout.vanilla.data.VanillaData;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.entity.creature.CreatureType;

/**
 * Component that represents a Monster Spawner in the world.
 */
public class MonsterSpawner extends VanillaBlockComponent {
	private final Random random = new Random();

	@Override
	public void onAttached() {
		resetSpawnDelay();
		sendData();
	}

	@Override
	public void onTick(float dt) {
		if (isActive()) {
			float spawnDelay = pulseSpawnDelay(dt);
			if (spawnDelay <= 0) {
				doSpawn();
			}
		} else {
			resetSpawnDelay();
		}
	}

	@Override
	public boolean canTick() {
		return true;
	}

	/**
	 * Sets the minimum amount of seconds that the spawner can spawn within.
	 * @param delay in seconds
	 */
	public void setMinSpawnDelay(int delay) {
		getData().put(VanillaData.MIN_SPAWN_DELAY, delay);
	}

	/**
	 * Returns the minimum amount of seconds that the spawner can spawn within.
	 * @return delay in seconds
	 */
	public int getMinSpawnDelay() {
		return getData().get(VanillaData.MIN_SPAWN_DELAY);
	}

	/**
	 * Sets the maximum amount of seconds that a spawner can spawn within.
	 * @param delay in seconds
	 */
	public void setMaxSpawnDelay(int delay) {
		getData().put(VanillaData.MAX_SPAWN_DELAY, delay);
	}

	/**
	 * Returns the maximum amount of seconds that a spawner can spawn within.
	 * @return delay in seconds
	 */
	public int getMaxSpawnDelay() {
		return getData().get(VanillaData.MAX_SPAWN_DELAY);
	}

	/**
	 * Sets the amount of creatures that can be within the bounds specified by
	 * {@link #getCreatureSearchRange()}.
	 * @param maxCreatures maximum amount of creatures that can spawn
	 */
	public void setMaxCreatures(int maxCreatures) {
		getData().put(VanillaData.MAX_CREATURES, maxCreatures);
	}

	/**
	 * Returns the amount of creatures that can be within the bounds specified
	 * by {@link #getCreatureSearchRange()}.
	 * @return maximum amount of creatures that can spawn
	 */
	public int getMaxCreatures() {
		return getData().get(VanillaData.MAX_CREATURES);
	}

	/**
	 * Sets the range in which to search for creatures in.
	 * @param range to search for creatures in
	 */
	public void setCreatureSearchRange(int range) {
		getData().put(VanillaData.CREATURE_SEARCH_RANGE, range);
	}

	/**
	 * Returns the range in which to search for creatures in.
	 * @return range to search for creatures in
	 */
	public int getCreatureSearchRange() {
		return getData().get(VanillaData.CREATURE_SEARCH_RANGE);
	}

	/**
	 * Sets the range in which entities can spawn from the spawner.
	 * @param spawnRange of spawner
	 */
	public void setSpawnRange(float spawnRange) {
		getData().put(VanillaData.SPAWN_RANGE, spawnRange);
	}

	/**
	 * Returns the range in which entities can spawn from the spawner.
	 * @return range from the spawner
	 */
	public float getSpawnRange() {
		return getData().get(VanillaData.SPAWN_RANGE);
	}

	/**
	 * Sets the amount of entities the spawner should attempt to spawn on every
	 * {@link #doSpawn()}.
	 * @param spawnCount how many entities to try and spawn
	 */
	public void setSpawnCount(int spawnCount) {
		getData().put(VanillaData.SPAWN_COUNT, spawnCount);
	}

	/**
	 * Returns the amount of entities the spawner should attempt to spawn on
	 * every {@link #doSpawn()}.
	 * @return how many entities to spawn
	 */
	public int getSpawnCount() {
		return getData().get(VanillaData.SPAWN_COUNT);
	}

	/**
	 * Attempts to spawn the amount of entities specified by
	 * {@link #getSpawnCount()} within the range specified by
	 * {@link #getSpawnRange()} and resets the spawn delay.
	 */
	public void doSpawn() {
		Point pos = getPoint();
		resetSpawnDelay();
		for (int i = 0; i < getSpawnCount(); i++) {
			float range = getSpawnRange();
			float x = pos.getX() + (random.nextFloat() - random.nextFloat()) * range;
			float y = pos.getY() + random.nextInt(3) - 1;
			float z = pos.getZ() + (random.nextFloat() - random.nextFloat()) * range;
			Point p = new Point(pos.getWorld(), x, y, z);
			if (p.getBlock().getMaterial().isMaterial(VanillaMaterials.AIR) && canSpawn()) {
				p.getWorld().createAndSpawnEntity(p, LoadOption.LOAD_ONLY, getCreatureType().getComponentType());
			}
		}
	}

	/**
	 * Whether the spawner should spawn more creatures.
	 * @return true if the spawner is able to spawn more
	 */
	public boolean canSpawn() {
		int count = 0;
		int range = getCreatureSearchRange();
		for (Entity e : getOwner().getWorld().getNearbyEntities(getPoint(), range)) {
			if (e instanceof Player) {
				continue;
			}
			count++;
		}
		return count < getMaxCreatures();
	}

	/**
	 * Sets the {@link CreatureType} spawned.
	 * @param type to spawn
	 */
	public void setCreatureType(CreatureType type) {
		getData().put(VanillaData.CREATURE_TYPE, type.getId());
		sendData();
	}

	/**
	 * Returns the {@link CreatureType} to spawn.
	 * @return creature to spawn
	 */
	public CreatureType getCreatureType() {
		return CreatureType.get(getData().get(VanillaData.CREATURE_TYPE));
	}

	/**
	 * Sets the radius in which the spawner requires at least one
	 * {@link Player} to be within.
	 * @param radius to search for players in
	 */
	public void setRadius(int radius) {
		getData().put(VanillaData.RADIUS, radius);
	}

	/**
	 * Returns the radius in which the spawner requires at least one
	 * {@link Player} to be within.
	 * @return radius to search for players in
	 */
	public int getRadius() {
		return getData().get(VanillaData.RADIUS);
	}

	/**
	 * Returns true if a {@link Player} is within the radius specified by
	 * {@link #getRadius()}.
	 * @return true if there is a player within the radius
	 */
	public boolean isActive() {
		return !getOwner().getWorld().getNearbyPlayers(getPoint(), getRadius()).isEmpty();
	}

	/**
	 * Returns the amount of time, in seconds, there is until the next
	 * {@link #doSpawn()} call.
	 * @return time, in seconds, until next spawn
	 */
	public float getSpawnDelay() {
		return getData().get(VanillaData.SPAWN_DELAY);
	}

	/**
	 * Sets the amount of time, in seconds, there is until the next
	 * {@link #doSpawn()} call.
	 * @param spawnDelay in seconds
	 */
	public void setSpawnDelay(float spawnDelay) {
		getData().put(VanillaData.SPAWN_DELAY, spawnDelay);
	}

	/**
	 * Resets the spawn delay to an amount between 10 and 40 seconds.
	 */
	public void resetSpawnDelay() {
		int max = getMaxSpawnDelay(), min = getMinSpawnDelay();
		setSpawnDelay(random.nextInt(max - min) + min);
	}

	private float pulseSpawnDelay(float dt) {
		float delay = getSpawnDelay() - dt;
		setSpawnDelay(delay);
		return delay;
	}

	private void sendData() {
		// TODO: Update the client
	}
}
