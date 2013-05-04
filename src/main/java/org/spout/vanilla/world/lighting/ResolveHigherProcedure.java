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

import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.IntVector3;
import org.spout.api.util.bytebit.ByteBitSet;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;
import org.spout.api.util.set.TInt10Procedure;
import org.spout.api.util.set.TInt10TripleSet;

public class ResolveHigherProcedure extends TInt10Procedure {
	private final static BlockFace[] allFaces = BlockFaces.NESWBT.toArray();
	private final TInt10TripleSet[] dirtySets;
	private final ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light;
	private final ImmutableCuboidBlockMaterialBuffer material;
	private final ImmutableHeightMapBuffer height;
	private final VanillaLightingManager manager;
	private int targetLevel;

	public ResolveHigherProcedure(VanillaLightingManager manager, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, TInt10TripleSet[] dirtySets) {
		this.dirtySets = dirtySets;
		this.light = light;
		this.material = material;
		this.manager = manager;
		this.height = height;
		this.targetLevel = 15;
	}
	
	public void setTargetLevel(int level) {
		this.targetLevel = level;
	}

	@Override
	public boolean execute(int x, int y, int z) {
		
		short id = material.getId(x, y, z);
		if (id == BlockMaterial.UNGENERATED.getId()) {
			return true;
		}
		
		short data = material.getData(x, y, z);
		BlockMaterial m = BlockMaterial.get(id, data);
		
		int lightLevel = manager.getLightLevel(light, x, y, z);

		if (lightLevel < targetLevel) {
			throw new IllegalStateException("Light level was not raised to " + targetLevel + " (" + lightLevel + ") at " + x + ", " + y + ", " + z);
		} else if (lightLevel > targetLevel) {
			return true;
		}
		
		ByteBitSet occlusionSet = m.getOcclusion(data);
			
		for (int f = 0; f < allFaces.length; f++) {
			BlockFace face = allFaces[f];
			IntVector3 offset = face.getIntOffset();
			int nx = x + offset.getX();
			int ny = y + offset.getY();
			int nz = z + offset.getZ();
			int neighborLight = manager.getLightLevel(light, nx, ny, nz, true);
			if (neighborLight >= lightLevel - 1) {
				continue;
			}
			if (occlusionSet.get(face)) {
				continue;
			}
			short nId = material.getId(nx, ny, nz);
			if (nId == BlockMaterial.UNGENERATED.getId()) {
				continue;
			}
			short nData = material.getData(nx, ny, nz);
			BlockMaterial nMaterial = BlockMaterial.get(nId, nData);
			
			int newLight = targetLevel - nMaterial.getOpacity() - 1;
			if (newLight > neighborLight) {
				dirtySets[newLight].add(nx, ny, nz);
			}			
		}

		return true;
	}
}
