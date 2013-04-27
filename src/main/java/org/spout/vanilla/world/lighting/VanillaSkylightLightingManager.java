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
package org.spout.vanilla.world.lighting;

import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;

public class VanillaSkylightLightingManager extends VanillaBlocklightLightingManager {
	public VanillaSkylightLightingManager(String name) {
		super(name);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] hx, int[] hz, int[] oldHy, int[] newHy, int changedColumns) {
		//Spout.getLogger().info(getClass().getSimpleName() + ":" + changedColumns + " columns changed");
	}

	@Override
	protected int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		return y > height.get(x, z) ? 15 : 0;
	}
	
	@Override
	public int updateEmittingBlocks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		int maxLight = 0;
		int minLight = 15;
		
		int emittingBlocks = 0;
		
		int bh = light.getBase().getFloorY();
		int th = bh + Chunk.BLOCKS.SIZE;
		
		for (int xx = x; xx < x + Chunk.BLOCKS.SIZE; xx++) {
			for (int zz = z; zz < z + Chunk.BLOCKS.SIZE; zz++) {
				int h = height.get(xx, zz);
				if (h < th - 1) {
					if (h >= bh) {
						minLight = 0;
					}
					for (int yy = Math.max(bh, h + 1); yy < th; yy++) {
						emittingBlocks++;
						this.setLightLevel(light, xx, yy, zz, 15);
						maxLight = 15;
					}
				} else {
					minLight = 0;
				}
			}
		}
		
		return maxLight != minLight ? emittingBlocks : -1;
	}
}
