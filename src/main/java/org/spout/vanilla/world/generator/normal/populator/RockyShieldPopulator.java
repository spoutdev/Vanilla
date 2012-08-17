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
package org.spout.vanilla.world.generator.normal.populator;

import java.util.Random;

import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.Populator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Liquid;

public class RockyShieldPopulator extends Populator {
	private static final Perlin SHIELD_BASE = new Perlin();
	private static final Turbulence SHIELD = new Turbulence();

	static {
		SHIELD_BASE.setFrequency(0.01);
		SHIELD_BASE.setOctaveCount(1);
		SHIELD.SetSourceModule(0, SHIELD_BASE);
		SHIELD.setFrequency(0.05);
		SHIELD.setPower(10);
		SHIELD.setRoughness(3);
	}

	public RockyShieldPopulator() {
		super(true);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final int size = Chunk.BLOCKS.SIZE;
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		final World world = chunk.getWorld();
		final int seed = (int) (world.getSeed() * 73);
		SHIELD_BASE.setSeed(seed);
		SHIELD.setSeed(seed);
		final double[][] noise = WorldGeneratorUtils.fastNoise(SHIELD, size, size, 4, x, 63, z);
		for (int xx = 0; xx < size; xx++) {
			for (int zz = 0; zz < size; zz++) {
				if (noise[xx][zz] > 0.9) {
					final int y = world.getSurfaceHeight(x + xx, z + zz);
					for (int yy = 0; yy >= -7; yy--) {
						if (yy == 0) {
							final Block block = world.getBlock(x + xx, y + yy, z + zz, world);
							if (!canReplace(block.getMaterial())) {
								continue;
							}
							BlockMaterial material = VanillaMaterials.AIR;
							for (BlockFace face : BlockFaces.NSEW) {
								final Block adjacent = block.translate(face);
								if (adjacent.getMaterial() instanceof Liquid) {
									material = adjacent.getMaterial();
									break;
								}
							}
							block.setMaterial(material);
						} else {
							if (canReplace(world.getBlockMaterial(x + xx, y + yy, z + zz))) {
								world.setBlockMaterial(x + xx, y + yy, z + zz, VanillaMaterials.STONE, (short) 0, world);
							}
						}
					}
				}
			}
		}
	}
	
	private boolean canReplace(BlockMaterial material) {
		return material.isMaterial(VanillaMaterials.GRASS, VanillaMaterials.MYCELIUM,
				VanillaMaterials.DIRT, VanillaMaterials.SAND, VanillaMaterials.SANDSTONE);
	}
}
