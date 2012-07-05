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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.plant.Flower;

public class FlowerDecorator extends Decorator {
	private static final List<Flower> flowers = new ArrayList<Flower>();

	static {
		flowers.add(VanillaMaterials.DANDELION);
		flowers.add(VanillaMaterials.ROSE);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() < 4) {
			return;
		}
		int howMany = random.nextInt(15);
		Flower flower = getRandomFlower(random);
		for (int i = 0; i < howMany; i++) {
			int dx = random.nextInt(Chunk.BLOCKS.SIZE);
			int dz = random.nextInt(Chunk.BLOCKS.SIZE);
			int dy = getHighestWorkableBlock(chunk, dx, dz);
			if (dy == -1) {
				continue;
			}
			Block block = chunk.getBlock(dx, dy, dz).translate(BlockFace.TOP);
			if (flower.canPlace(block, (short) 0, BlockFace.TOP, false)) {
				flower.onPlacement(block, (short) 0, BlockFace.TOP, false);
			}
			if (flower.canAttachTo(block, BlockFace.TOP)) {
				block.translate(BlockFace.TOP).setMaterial(flower);
			}
		}
	}

	private Flower getRandomFlower(Random random) {
		int which = random.nextInt(flowers.size());
		return flowers.get(which);
	}

	private int getHighestWorkableBlock(Chunk c, int px, int pz) {
		int y = 15;
		while (c.getBlockMaterial(px, y, pz) != VanillaMaterials.GRASS) {
			y--;
			if (y == 0 || c.getBlockMaterial(px, y, pz) == VanillaMaterials.WATER) {
				return -1;
			}
		}
		y++;
		return y;
	}
}
