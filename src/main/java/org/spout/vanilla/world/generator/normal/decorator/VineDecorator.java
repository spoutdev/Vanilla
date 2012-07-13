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
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;

public class VineDecorator extends Decorator {
	private final byte amount;

	public VineDecorator() {
		this((byte) 15);
	}

	public VineDecorator(byte amount) {
		this.amount = amount;
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		final World world = chunk.getWorld();
		for (byte count = 0; count < amount; count++) {
			final int x = chunk.getBlockX(random);
			final int z = chunk.getBlockZ(random);
			for (short y = 64; y < 128; y++) {
				final int xx = x - 3 + random.nextInt(7);
				final int zz = z - 3 + random.nextInt(7);
				final Block block = world.getBlock(xx, y, zz);
				if (block.isMaterial(VanillaMaterials.AIR)) {
					if (block.translate(BlockFace.TOP).isMaterial(VanillaMaterials.VINES)) {
						block.setMaterial(VanillaMaterials.VINES);
						block.setData(block.translate(BlockFace.TOP));
					} else {
						faceCheck:
						for (final BlockFace face : BlockFaces.NESW) {
							if (VanillaMaterials.VINES.canPlace(block, (short) 0, face, false)) {
								block.setMaterial(VanillaMaterials.VINES);
								VanillaMaterials.VINES.setFaceAttached(block, face, true);
								break faceCheck;
							}
						}
					}
				}
			}
		}
	}
}
