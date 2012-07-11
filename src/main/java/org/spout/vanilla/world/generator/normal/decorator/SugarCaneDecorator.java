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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.object.largeplant.SugarCaneStackObject;

/**
 * Decorator that decorates a biome with sugar canes.
 */
public class SugarCaneDecorator extends Decorator {
	private static final SugarCaneStackObject CANES = new SugarCaneStackObject();
	private final byte maxClusterSize;
	private final byte clusterPlaceAttempts;
	private final byte numberOfClusters;

	public SugarCaneDecorator() {
		this((byte) 6, (byte) 20, (byte) 1);
	}

	public SugarCaneDecorator(byte maxClusterSize, byte clusterPlaceAttempts, byte numberOfClusters) {
		this.maxClusterSize = maxClusterSize;
		this.clusterPlaceAttempts = clusterPlaceAttempts;
		this.numberOfClusters = numberOfClusters;
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		CANES.setRandom(random);
		byte successfulClusterCount = 0;
		for (byte count = 0; count < clusterPlaceAttempts; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			if (y == -1 || !CANES.canPlaceObject(world, x, y, z)) {
				continue;
			}
			successfulClusterCount++;
			CANES.randomize();
			CANES.placeObject(world, x, y, z);
			for (byte placed = 1; placed < maxClusterSize; placed++) {
				final int xx = x - 3 + random.nextInt(7);
				final int zz = z - 3 + random.nextInt(7);
				CANES.randomize();
				if (CANES.canPlaceObject(world, xx, y, zz)) {
					CANES.placeObject(world, xx, y, zz);
				}
			}
			if (successfulClusterCount >= numberOfClusters) {
				return;
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = world.getHeight();
		while (!world.getBlockMaterial(x, y, z).equals(VanillaMaterials.DIRT, VanillaMaterials.GRASS, VanillaMaterials.SAND)) {
			y--;
			if (y == 0) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
