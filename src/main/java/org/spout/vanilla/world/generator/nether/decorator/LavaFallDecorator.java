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
package org.spout.vanilla.world.generator.nether.decorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;
import org.spout.vanilla.world.generator.nether.NetherGenerator;

public class LavaFallDecorator extends Decorator {
	private int lavaAttempts = 8;

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final List<Block> liquids = new ArrayList<Block>();
		for (byte count = 0; count < lavaAttempts; count++) {
			final int x = chunk.getBlockX(random);
			final int y = random.nextInt(NetherGenerator.HEIGHT);
			final int z = chunk.getBlockZ(random);
			final Block block = world.getBlock(x, y, z);
			if (isValidSourcePoint(block)) {
				block.setMaterial(VanillaMaterials.LAVA);
				liquids.add(block);
			}
		}
		Liquid.performInstantFlow(liquids);
	}

	private boolean isValidSourcePoint(Block block) {
		if (!block.isMaterial(VanillaMaterials.NETHERRACK, VanillaMaterials.AIR)
				|| !block.translate(BlockFace.TOP).isMaterial(VanillaMaterials.NETHERRACK)) {
			return false;
		}
		byte adjacentNetherrackCount = 0;
		byte adjacentAirCount = 0;
		for (final BlockFace face : BlockFaces.NSEWB) {
			final BlockMaterial material = block.translate(face).getMaterial();
			if (material == VanillaMaterials.AIR) {
				adjacentAirCount++;
			} else if (material == VanillaMaterials.NETHERRACK) {
				adjacentNetherrackCount++;
			}
		}
		return adjacentNetherrackCount == 4 && adjacentAirCount == 1;
	}

	public void setLavaAttempts(int lavaAttempts) {
		this.lavaAttempts = lavaAttempts;
	}
}
