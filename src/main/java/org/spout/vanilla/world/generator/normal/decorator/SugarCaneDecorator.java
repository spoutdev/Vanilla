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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;
import org.spout.vanilla.world.generator.normal.NormalGenerator;
import org.spout.vanilla.world.generator.normal.object.variableheight.SugarCaneStackObject;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Chunk;

import org.spout.vanilla.material.VanillaMaterials;

/**
 * Decorator that decorates a biome with sugar canes.
 */
public class SugarCaneDecorator extends Decorator {
	private int clusterPlaceAttempts = 10;
	private int numberOfClusters = 1;
	private int maxClusterSize = 4;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final SugarCaneStackObject canes = new SugarCaneStackObject();
		canes.setRandom(random);
		byte successfulClusterCount = 0;
		for (byte count = 0; count < clusterPlaceAttempts; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			final int y = getHighestWorkableBlock(world, x, z);
			if (y == -1 || !canes.canPlaceObject(world, x, y, z)) {
				continue;
			}
			successfulClusterCount++;
			canes.randomize();
			canes.placeObject(world, x, y, z);
			for (byte placed = 1; placed < maxClusterSize; placed++) {
				final int xx = x - 3 + random.nextInt(7);
				final int zz = z - 3 + random.nextInt(7);
				canes.randomize();
				if (canes.canPlaceObject(world, xx, y, zz)) {
					canes.placeObject(world, xx, y, zz);
				}
			}
			if (successfulClusterCount >= numberOfClusters) {
				return;
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int z) {
		int y = NormalGenerator.HEIGHT;
		while (!world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.DIRT, VanillaMaterials.GRASS, VanillaMaterials.SAND)) {
			if (--y <= 0) {
				return -1;
			}
		}
		return ++y;
	}

	public void setClusterPlaceAttempts(int clusterPlaceAttempts) {
		this.clusterPlaceAttempts = clusterPlaceAttempts;
	}

	public void setNumberOfClusters(int numberOfClusters) {
		this.numberOfClusters = numberOfClusters;
	}

	public void setMaxClusterSize(int maxClusterSize) {
		this.maxClusterSize = maxClusterSize;
	}
}
