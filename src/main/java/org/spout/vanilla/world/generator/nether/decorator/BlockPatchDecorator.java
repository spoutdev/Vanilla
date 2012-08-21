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
package org.spout.vanilla.world.generator.nether.decorator;

import java.util.Random;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.nether.NetherGenerator;

public class BlockPatchDecorator extends Decorator {
	private final Perlin elevation = new Perlin();
	private final Perlin shapeBase = new Perlin();
	private final Turbulence shape = new Turbulence();
	private final BlockMaterial material;

	public BlockPatchDecorator(BlockMaterial material) {
		this.material = material;

		elevation.setFrequency(0.01);
		elevation.setLacunarity(1);
		elevation.setNoiseQuality(NoiseQuality.STANDARD);
		elevation.setPersistence(0.7);
		elevation.setOctaveCount(1);

		shapeBase.setFrequency(0.05);
		shapeBase.setNoiseQuality(NoiseQuality.STANDARD);
		shapeBase.setOctaveCount(1);

		shape.SetSourceModule(0, shapeBase);
		shape.setFrequency(0.03);
		shape.setPower(8);
		shape.setRoughness(2);
	}

	@Override
	public void populate(Chunk chunk, Random random) {
		if (chunk.getY() != 4) {
			return;
		}
		final long seed = chunk.getWorld().getSeed() * material.getId();
		elevation.setSeed((int) (seed * 101));
		shapeBase.setSeed((int) (seed * 313));
		shape.setSeed((int) (seed * 661));
		final World world = chunk.getWorld();
		final int x = chunk.getBlockX();
		final int z = chunk.getBlockZ();
		final int size = Chunk.BLOCKS.SIZE;
		final int scale = (NetherGenerator.HEIGHT - size * 2) / 2;
		final double[][] displacement = WorldGeneratorUtils.fastNoise(elevation, size, size, 4, x, 0, z);
		final double[][] values = WorldGeneratorUtils.fastNoise(shape, size, size, 4, x, 0, z);
		for (byte xx = 0; xx < size; xx++) {
			for (byte zz = 0; zz < size; zz++) {
				if (values[xx][zz] > 0.6) {
					final int y = getHighestWorkableBlock(world, x + xx, (int) (displacement[xx][zz] * scale + scale + size), z + zz);
					if (y == -1 || !world.getBlockMaterial(x + xx, y + 1, z + zz).isMaterial(VanillaMaterials.AIR)) {
						continue;
					}
					final int depth = random.nextInt(3) + 3;
					for (int yy = 0; yy < depth; yy++) {
						final Block block = world.getBlock(x + xx, y - yy, z + zz, world);
						if (block.getMaterial().isMaterial(VanillaMaterials.NETHERRACK)) {
							block.setMaterial(material);
						} else {
							break;
						}
					}
				}
			}
		}
	}

	private int getHighestWorkableBlock(World world, int x, int y, int z) {
		while (!world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.NETHERRACK)) {
			y--;
			if (y <= 0) {
				return -1;
			}
		}
		return y;
	}
}
