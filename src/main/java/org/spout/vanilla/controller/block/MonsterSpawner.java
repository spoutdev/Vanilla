/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.controller.block;

import java.util.Random;
import java.util.Set;

import org.spout.api.entity.Entity;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.math.Vector3;
import org.spout.api.player.Player;

import org.spout.vanilla.controller.VanillaBlockController;
import org.spout.vanilla.controller.VanillaControllerType;
import org.spout.vanilla.controller.VanillaControllerTypes;
import org.spout.vanilla.controller.living.Creature;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.TileEntityDataMessage;
import org.spout.vanilla.world.generator.nether.NetherGenerator;

public class MonsterSpawner extends VanillaBlockController {
	private int range = 17, maxEntities = 6, spawnAmount = 4;
	private final Random random = new Random();
	private float counter;
	private VanillaControllerType nextSpawn;

	public MonsterSpawner() {
		super(VanillaControllerTypes.MONSTER_SPAWNER, VanillaMaterials.MONSTER_SPAWNER);
	}

	@Override
	public void onAttached() {
		resetCounter();
		setNextSpawn(getSpawnType());
		System.out.println("Monster Spawner of type " + nextSpawn.getName() + " spawned.");
	}

	@Override
	public void onTick(float dt) {
		Block block = getBlock();
		Set<Player> nearbyPlayers = block.getWorld().getNearbyPlayers(block.getPosition(), range);
		if (nearbyPlayers == null || nearbyPlayers.isEmpty()) {
			return;
		}

		int nearbyEntities = 0;
		for (Entity entity : block.getWorld().getAll()) {
			if (entity.getController() instanceof Creature && entity.getPosition().getDistance(block.getPosition()) < range) {
				nearbyEntities++;
			}
		}

		if (nearbyEntities >= maxEntities) {
			return;
		}

		spawnAmount = Math.min(4, maxEntities - nearbyEntities);
		counter -= dt;
		if (counter <= 0) {
			if (nextSpawn != null) {
				World world = block.getWorld();
				for (int i = 0; i < spawnAmount; i++) {
					world.createAndSpawnEntity(new Point(world, offset(block.getX()), block.getY(), offset(block.getZ())), nextSpawn.createController());
				}
			}
			setNextSpawn(getSpawnType());
			resetCounter();
		}
	}

	private float offset(int i) {
		return i + (random.nextFloat() * 4);
	}

	@SuppressWarnings("fallthrough")
	private VanillaControllerType getSpawnType() {
		Block block = getBlock();
		World world = block.getWorld();
		Block below = world.getBlock(block.getPosition().subtract(Vector3.UNIT_Y));
		if (below.getMaterial() == VanillaMaterials.GRASS && block.getLight() > 8) {
			int i = random.nextInt(6);
			switch (i) {
				case 0:
					return VanillaControllerTypes.CHICKEN;
				case 1:
					return VanillaControllerTypes.COW;
				case 2:
					return VanillaControllerTypes.MOOSHROOM;
				case 3:
					return VanillaControllerTypes.PIG;
				case 4:
					return VanillaControllerTypes.SHEEP;
				case 5:
					return VanillaControllerTypes.WOLF;
				default:
					return VanillaControllerTypes.SHEEP;
			}
		} else if (random.nextInt(100) > 25 && (below.getMaterial() == VanillaMaterials.GRASS || below.getMaterial() == VanillaMaterials.LEAVES) && block.getY() > 62) {
			return VanillaControllerTypes.OCELOT;
		} else if (block.getLight() < 9) {
			int i = random.nextInt(7);
			switch (i) {
				case 0:
					return VanillaControllerTypes.CREEPER;
				case 1:
					return VanillaControllerTypes.ENDERMAN;
				case 2:
					return VanillaControllerTypes.SKELETON;
				case 3:
					return VanillaControllerTypes.SPIDER;
				case 4:
					return VanillaControllerTypes.CAVE_SPIDER;
				case 5:
					return VanillaControllerTypes.ZOMBIE;
				case 6:
					Set<Player> nearbyPlayers = block.getWorld().getNearbyPlayers(block.getPosition(), 5);
					if (nearbyPlayers == null || nearbyPlayers.isEmpty()) {
						return VanillaControllerTypes.SILVERFISH;
					}
				default:
					return VanillaControllerTypes.CREEPER;
			}
		} else if (block.getY() < 40 && random.nextInt(10) == 0) {
			return VanillaControllerTypes.SLIME;
		} else if (world.getGenerator() instanceof NetherGenerator && random.nextInt(20) == 0) {
			return VanillaControllerTypes.GHAST;
		} else if (block.getY() > 45 && block.getY() < 63) {
			return VanillaControllerTypes.SQUID;
		}
		return null;
	}

	private void resetCounter() {
		counter = random.nextInt(30) + 10;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getMaxEntities() {
		return maxEntities;
	}

	public void setMaxEntities(int maxEntities) {
		this.maxEntities = maxEntities;
	}

	public VanillaControllerType getNextSpawn() {
		return nextSpawn;
	}

	public void setNextSpawn(VanillaControllerType nextSpawn) {
		Block block = getBlock();
		VanillaNetworkSynchronizer.broadcastPacket(new TileEntityDataMessage(block.getX(), block.getY(), block.getZ(), 1, nextSpawn.getID(), -1, -1));
		this.nextSpawn = nextSpawn;
	}
}
