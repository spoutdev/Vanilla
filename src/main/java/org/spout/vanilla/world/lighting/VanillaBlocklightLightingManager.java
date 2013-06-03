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
import org.spout.api.util.IntVector3Array;
import org.spout.api.util.IntVector3CuboidArray;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;
import org.spout.api.util.cuboid.procedure.CuboidBlockMaterialProcedure;

public class VanillaBlocklightLightingManager extends VanillaLightingManager {
	public VanillaBlocklightLightingManager(String name) {
		super(name);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] x, int[] y, int[] z, int changedBlocks) {
		Iterable<IntVector3> coords = new IntVector3Array(x, y, z, changedBlocks);
		super.resolve(light, material, height, coords, false);
	}

	@Override
	public void resolveChunks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] bx, int[] by, int[] bz, int[] tx, int[] ty, int[] tz, int changedCuboids) {
		Iterable<IntVector3> coords = new IntVector3CuboidArray(bx, by, bz, tx, ty, tz, changedCuboids);
		super.resolve(light, material, height, coords, false);
	}

	@Override
	protected int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		BlockMaterial m = material.get(x, y, z);
		short data = material.getData(x, y, z);
		return m.getLightLevel(data);
	}

	@Override
	public void updateEmittingBlocks(int[][][] emittedLight, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		int size = Chunk.BLOCKS.SIZE;
		for (int xx = x; xx < x + size; xx++) {
			for (int yy = y; yy < y + size; yy++) {
				int xIndex = xx - x + 1;
				int zIndex = 1;
				int yIndex = yy - y + 1;
				int[] zArray = emittedLight[xIndex][yIndex];
				for (int zz = z; zz < z + size; zz++) {
					BlockMaterial m = material.get(xx, yy, zz);
					zArray[zIndex++] = m.getLightLevel(m.getData());
				}
			}
		}
	}

	@Override
	public void bulkEmittingInitialize(ImmutableCuboidBlockMaterialBuffer buffer, final int[][][] light, int[][] height) {
		Vector3 base = buffer.getBase();

		final int baseX = base.getFloorX();
		final int baseY = base.getFloorY();
		final int baseZ = base.getFloorZ();

		buffer.forEach(new CuboidBlockMaterialProcedure() {
			@Override
			public boolean execute(int x, int y, int z, short id, short data) {
				x -= baseX;
				z -= baseZ;

				BlockMaterial m = BlockMaterial.get(id, data);

				int lightLevel = m.getLightLevel(m.getData());
				if (lightLevel > 0) {
					light[x + 1][y + 1][z + 1] = lightLevel;
				}
				return true;
			}
		});
	}
}
