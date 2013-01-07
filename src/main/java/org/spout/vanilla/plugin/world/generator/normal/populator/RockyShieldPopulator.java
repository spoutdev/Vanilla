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
package org.spout.vanilla.plugin.world.generator.normal.populator;

import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.module.modifier.Turbulence;
import net.royawesome.jlibnoise.module.source.Perlin;

import org.spout.api.generator.GeneratorPopulator;
import org.spout.api.generator.WorldGeneratorUtils;
import org.spout.api.generator.biome.BiomeManager;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.CuboidBlockMaterialBuffer;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.normal.NormalGenerator;

public class RockyShieldPopulator implements GeneratorPopulator {
	private static final Perlin SHIELD_BASE = new Perlin();
	private static final Turbulence SHIELD = new Turbulence();

	static {
		SHIELD_BASE.setFrequency(0.01);
		SHIELD_BASE.setNoiseQuality(NoiseQuality.STANDARD);
		SHIELD_BASE.setOctaveCount(1);

		SHIELD.SetSourceModule(0, SHIELD_BASE);
		SHIELD.setFrequency(0.05);
		SHIELD.setPower(10);
		SHIELD.setRoughness(3);
	}

	@Override
	public void populate(CuboidBlockMaterialBuffer blockData, int x, int y, int z, BiomeManager biomes, long seed) {
		final Vector3 size = blockData.getSize();
		final int sizeX = size.getFloorX();
		final int sizeY = size.getFloorY();
		final int sizeZ = size.getFloorZ();
		SHIELD_BASE.setSeed((int) seed * 73);
		SHIELD.setSeed((int) seed * 79);
		final double[][] noise = WorldGeneratorUtils.fastNoise(SHIELD, sizeX, sizeZ, 4, x, 63, z);
		for (int xx = 0; xx < sizeX; xx++) {
			for (int zz = 0; zz < sizeZ; zz++) {
				if (noise[xx][zz] > 0.92) {
					int yy = sizeY - 1;
					for (; yy >= 0 && !canReplace(blockData.get(x + xx, y + yy, z + zz)); yy--) {
					}
					if (yy < 0) {
						continue;
					}
					final int depthY = yy - 7;
					final int surfaceY = yy;
					for (; yy > depthY; yy--) {
						if (yy < 0) {
							break;
						}
						if (!canReplace(blockData.get(x + xx, y + yy, z + zz))) {
							continue;
						}
						if (yy == surfaceY) {
							blockData.set(x + xx, y + yy, z + zz, y + yy <= NormalGenerator.SEA_LEVEL
									? VanillaMaterials.STATIONARY_WATER : VanillaMaterials.AIR);
						} else {
							blockData.set(x + xx, y + yy, z + zz, VanillaMaterials.STONE);
						}
					}
				}
			}
		}
	}

	private boolean canReplace(BlockMaterial material) {
		return (material.equals(VanillaMaterials.GRASS) || material.equals(VanillaMaterials.MYCELIUM)
				|| material.equals(VanillaMaterials.DIRT) || material.equals(VanillaMaterials.SAND)
				|| material.equals(VanillaMaterials.SANDSTONE));
	}
}
