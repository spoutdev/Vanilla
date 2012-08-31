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
package org.spout.vanilla.world.generator.normal.biome.sandy;

import org.spout.api.generator.biome.Decorator;
import org.spout.api.geo.World;
import org.spout.api.math.MathHelper;

import org.spout.vanilla.data.Climate;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.normal.biome.NormalBiome;

public abstract class SandyBiome extends NormalBiome {
	public SandyBiome(int biomeId, Decorator... decorators) {
		super(biomeId, decorators);
		this.setClimate(Climate.WARM);
	}

	@Override
	public int placeGroundCover(World world, int x, int y, int z) {
		super.placeGroundCover(world, x, y, z);
		final byte sandDepth = (byte) MathHelper.clamp(Math.round(BLOCK_REPLACER.GetValue(x, -5, z) * 0.5 + 3.5), 3, 4);
		final byte sandstoneDepth = (byte) MathHelper.clamp(Math.round(BLOCK_REPLACER.GetValue(x, -6, z) + 2), 1, 3);
		final byte maxGroudCoverDepth = (byte) (sandDepth + sandstoneDepth);
		for (byte depth = 0; depth < maxGroudCoverDepth; depth++) {
			if (world.getBlockMaterial(x, y - depth, z).isMaterial(VanillaMaterials.AIR)) {
				return maxGroudCoverDepth;
			}
			if (depth < sandDepth) {
				world.setBlockMaterial(x, y - depth, z, VanillaMaterials.SAND, (short) 0, world);
			} else {
				world.setBlockMaterial(x, y - depth, z, VanillaMaterials.SANDSTONE, (short) 0, world);
			}
		}
		return maxGroudCoverDepth;
	}
}
