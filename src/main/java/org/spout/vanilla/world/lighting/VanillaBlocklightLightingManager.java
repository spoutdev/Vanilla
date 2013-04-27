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
import org.spout.api.util.IntVector3Array;
import org.spout.api.util.IntVector3CompositeIterator;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;

public class VanillaBlocklightLightingManager extends VanillaLightingManager {
	public VanillaBlocklightLightingManager(String name) {
		super(name);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] x, int[] y, int[] z, int changedBlocks) {
		Iterable<IntVector3> coords = new IntVector3Array(x, y, z, changedBlocks);
		super.resolve(light, material, height, coords);
	}

	@Override
	public void resolveChunks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] bx, int[] by, int[] bz, int changedCuboids) {
	//	Iterable<IntVector3> coords = new IntVector3CuboidArray(bx, by, bz, tx, ty, tz, changedCuboids);
	//	super.resolve(light, material, height, coords);
	}
	
	@Override
	protected void initChunks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] bx, int[] by, int[] bz, int initializedChunks) {
		// Scan for new chunks needs to check
		// - light emitting blocks
		// - boundary of entire volume
		
		@SuppressWarnings("unchecked")
		Iterable<IntVector3>[] emitters = new Iterable[initializedChunks + 1];
		
		for (int i = 0; i < initializedChunks; i++) {
			emitters[i] = this.scanChunk(light, material, height, bx[i], by[i], bz[i]);
		}
		
		Iterable<IntVector3> boundary = this.getBoundary(material, bx, by, bz, initializedChunks);
		
		emitters[initializedChunks] = boundary;
		
		Iterable<IntVector3> combined = new IntVector3CompositeIterator(emitters);
		
		super.resolve(light, material, height, combined);
	}
	
	@Override
	protected int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		BlockMaterial m = material.get(x, y, z);
		short data = material.getData(x, y, z);
		return m.getLightLevel(data);
	}
	
	@Override
	public int updateEmittingBlocks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		int maxLight = 0;
		int minLight = 15;
		
		int emittingBlocks = 0;
		
		for (int xx = x; xx < x + Chunk.BLOCKS.SIZE; xx++) {
			for (int yy = y; yy < y + Chunk.BLOCKS.SIZE; yy++) {
				for (int zz = z; zz < z + Chunk.BLOCKS.SIZE; zz++) {
					int emitted = this.getEmittedLight(material, height, xx, yy, zz);
					if (emitted > 0) {
						this.setLightLevel(light, xx, yy, zz, emitted);
						emittingBlocks++;
					}
					if (emitted > maxLight) {
						maxLight = emitted;
					}
					if (emitted < minLight) {
						minLight = emitted;
					}
				}
			}
		}
		
		return maxLight != minLight ? emittingBlocks : -1;
	}
	
}
