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
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Vector3;
import org.spout.api.util.IntVector3CuboidArray;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;
import org.spout.api.util.cuboid.procedure.CuboidBlockMaterialProcedure;

public class VanillaSkylightLightingManager extends VanillaBlocklightLightingManager {
	public VanillaSkylightLightingManager(String name) {
		super(name);
	}

	@Override
	public void resolveColumns(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] hx, int[] hz, int[] oldHy, int[] newHy, int changedColumns) {
		Iterable<IntVector3> coords = new IntVector3CuboidArray(hx, oldHy, hz, newHy, changedColumns, true);
		super.resolve(light, material, height, coords, false);
	}

	@Override
	protected int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		return y > height.get(x, z) ? 15 : 0;
	}
	
	@Override
	public void updateEmittingBlocks(int[][][] emittedLight, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		int size = Chunk.BLOCKS.SIZE;
		for (int xx = x; xx < x + size; xx++) {
			for (int zz = z; zz < z + size; zz++) {
				int xIndex = xx - x + 1;
				int zIndex = zz - z + 1;
				int yIndex = 1;
				int h = Math.max(height.get(xx, zz) + 1, y);
				h = Math.min(h, y + size);
				for (int yy = y; yy < h; yy++) {
					emittedLight[xIndex][yIndex++][zIndex] = 0;
				}
				for (int yy = h; yy < y + size; yy++) {
					emittedLight[xIndex][yIndex++][zIndex] = 15;
				}
			}
		}
	}
	
	@Override
	public void bulkEmittingInitialize(ImmutableCuboidBlockMaterialBuffer buffer, int[][][] light, int[][] genHeight) {
		
		Vector3 size = buffer.getSize();
		
		final int sizeX = size.getFloorX();
		final int sizeY = size.getFloorY();
		final int sizeZ = size.getFloorZ();
		
		Vector3 base = buffer.getBase();
		
		final int baseX = base.getFloorX();
		final int baseY = base.getFloorY();
		final int baseZ = base.getFloorZ();
		
		final int topY = buffer.getTop().getFloorY();
		
		final int[][] height = new int[sizeX][sizeZ];
		
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				height[x][z] = genHeight[x][z];
			}
		}
		
		buffer.forEach(new CuboidBlockMaterialProcedure() {
			@Override
			public boolean execute(int x, int y, int z, short id, short data) {
				x -= baseX;
				z -= baseZ;

				BlockMaterial m = BlockMaterial.get(id, data);
				if (height[x][z] < y && m.isSurface()) {
					height[x][z] = y;
				}
				return true;
			}
		});
		
		for (int x = 1; x <= sizeX; x++) {
			for (int z = 1; z <= sizeZ; z++) {
				int h = height[x - 1][z - 1];
				h -= baseY;
				for (int y = Math.max(1, 2 + h); y <= sizeY ; y++) {
					light[x][y][z] = 15;
				}
			}
		}
	}
}
