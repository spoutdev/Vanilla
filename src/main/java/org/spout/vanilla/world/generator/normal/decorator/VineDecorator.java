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
package org.spout.vanilla.world.generator.normal.decorator;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.decorator.VariableAmountDecorator;
import org.spout.vanilla.world.generator.normal.NormalGenerator;

public class VineDecorator extends VariableAmountDecorator {
	public VineDecorator() {
		super(50, 0);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final World world = chunk.getWorld();
		final int amount = getAmount(random);
		for (byte count = 0; count < amount; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			for (int y = NormalGenerator.SEA_LEVEL; y < NormalGenerator.HEIGHT; y++) {
				final int xx = x - 3 + random.nextInt(7);
				final int zz = z - 3 + random.nextInt(7);
				final Block block = world.getBlock(xx, y, zz);
				if (block.isMaterial(VanillaMaterials.AIR)) {
					final Block top = block.translate(BlockFace.TOP);
					if (top.isMaterial(VanillaMaterials.VINES)) {
						block.setMaterial(VanillaMaterials.VINES);
						block.setData(top.getBlockData());
					} else {
						for (final BlockFace face : BlockFaces.NESW) {
							if (VanillaMaterials.VINES.canPlace(block, (short) 0, face, face.getOffset(), false, null)) {
								VanillaMaterials.VINES.onPlacement(block, (short) 0, face, face.getOffset(), false, null);
								break;
							}
						}
					}
				}
			}
		}
	}
}
