/*
 * This file is part of vanilla (http://www.spout.org/).
 *
 * vanilla is licensed under the SpoutDev License Version 1.
 *
 * vanilla is free software: you can redistribute it and/or modify
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
 * the MIT license and the SpoutDev license version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.controller.world;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.spout.api.entity.Controller;
import org.spout.api.entity.Entity;
import org.spout.api.entity.type.ControllerType;
import org.spout.api.entity.type.EmptyConstructorControllerType;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.player.Player;

import org.spout.vanilla.controller.VanillaController;

/**
 * Controller that spawns entities in regions.
 */
public class RegionSpawner extends Controller implements VanillaController {
	public static final ControllerType TYPE = new EmptyConstructorControllerType(RegionSpawner.class, "Region Spawner");
	private static final int SPAWN_TRIES = 6;
	private final Region region;
	private final Random rand = new Random();
	private final Map<ControllerType, SpawnInformation> spawnableTypes = new ConcurrentHashMap<ControllerType, SpawnInformation>();

	public RegionSpawner(Region region) {
		super(TYPE);
		this.region = region;
	}

	@Override
	public void onAttached() {
		getParent().setCollision(null);
	}

	@Override
	public void onTick(float dt) {
		World world = region.getWorld();
		Set<Player> players = world.getPlayers();
		if (players.isEmpty()) {
			return;
		}
	}

	/**
	 * Adds a controller type to the list of controller types this region spawner will try to spawn.
	 * @param type       to spawn
	 * @param canSpawnOn a set of block materials that this controller can spawn on
	 * @param amount     of this type of controller that should be spawned per chunk, on average
	 * @throws IllegalStateException if the controller cannot be spawned
	 */
	public void addSpawnableType(ControllerType type, Set<BlockMaterial> canSpawnOn, int amount) {
		if (!type.canCreateController()) {
			throw new IllegalStateException("Class " + type + " does not have a default constructor!");
		}
		spawnableTypes.put(type, new SpawnInformation(amount, canSpawnOn));
	}

	/**
	 * Region this spawner manages.
	 * @return region
	 */
	public Region getRegion() {
		return region;
	}

	public void spawn(Chunk chunk) {
		for (Entry<ControllerType, SpawnInformation> entry : spawnableTypes.entrySet()) {
			SpawnInformation info = entry.getValue();
			Set<Entity> existing = region.getAll(entry.getKey().getControllerClass());
			if (existing.size() < info.amount) {
				int randX = this.rand.nextInt(Chunk.CHUNK_SIZE);
				int randY = this.rand.nextInt(Chunk.CHUNK_SIZE);
				int randZ = this.rand.nextInt(Chunk.CHUNK_SIZE);

				//TODO: check for nearby player?

				BlockMaterial material = chunk.getBlockMaterial(randX, randY, randZ);
				if (info.canSpawnOn.contains(material) && canSpawnAt(chunk, randX, randY + 1, randZ)) {
					for (int tries = 0; tries < SPAWN_TRIES; tries++) {
						int x = randX + (this.rand.nextInt(SPAWN_TRIES) - this.rand.nextInt(SPAWN_TRIES));
						int y = randY + 1;
						int z = randZ + (this.rand.nextInt(SPAWN_TRIES) - this.rand.nextInt(SPAWN_TRIES));
						if (canSpawnAt(chunk, x, y, z)) {
							try {
								Controller controller = entry.getKey().createController();
								x += chunk.getX() * 16 + 0.5F;
								y += chunk.getY() * 16 + 1F;
								z += chunk.getZ() * 16 + 0.5F;
								region.getWorld().createAndSpawnEntity(new Point(region.getWorld(), x, y, z), controller);
							} catch (Exception e) {
								throw new RuntimeException("Unable to spawn " + entry.getKey().getName(), e);
							}
						}
					}
				}
			}
		}
	}

	private boolean canSpawnAt(Chunk chunk, int randX, int randY, int randZ) {
		int x = chunk.getX() * 16 + randX;
		int y = chunk.getY() * 16 + randY;
		int z = chunk.getZ() * 16 + randZ;
		World world = chunk.getWorld();
		if (world.getBlock(x, y + 1, z) != BlockMaterial.AIR) {
			return false;
		}
		if (world.getBlock(x + 1, y, z) != BlockMaterial.AIR) {
			return false;
		}
		if (world.getBlock(x - 1, y, z) != BlockMaterial.AIR) {
			return false;
		}
		if (world.getBlock(x, y, z + 1) != BlockMaterial.AIR) {
			return false;
		}
		if (world.getBlock(x, y, z - 1) != BlockMaterial.AIR) {
			return false;
		}

		return true;
	}
}

class SpawnInformation {
	protected final int amount;
	protected final Set<BlockMaterial> canSpawnOn;

	public SpawnInformation(int amount, Set<BlockMaterial> canSpawnOn) {
		this.amount = amount;
		this.canSpawnOn = canSpawnOn;
	}
}
