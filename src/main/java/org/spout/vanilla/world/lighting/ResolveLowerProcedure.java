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

import org.spout.api.Spout;
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

public class ResolveLowerProcedure extends TInt10Procedure {
	private final static BlockFace[] allFaces = BlockFaces.NESWBT.toArray();
	private final TInt10TripleSet[] dirtySets;
	private final TInt10TripleSet[] regenSets;
	private final ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light;
	private final ImmutableCuboidBlockMaterialBuffer material;
	private final VanillaLightingManager manager;
	private final ImmutableHeightMapBuffer height;
	private int previousLevel = 15;

	public ResolveLowerProcedure(VanillaLightingManager manager, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, TInt10TripleSet[] dirtySets, TInt10TripleSet[] regenSets) {
		this.dirtySets = dirtySets;
		this.regenSets = regenSets;
		this.light = light;
		this.material = material;
		this.manager = manager;
		this.height = height;
	}
	
	public void setPreviousLevel(int previousLevel) {
		this.previousLevel = previousLevel;
	}

	@Override
	public boolean execute(int x, int y, int z) {
		return execute(x, y, z, true);
	}

	public boolean execute(int x, int y, int z, boolean neighbours) {	
		// Spout.getLogger().info("Resolving lower for " + x + ", " + y + ", " + z);
		for (int f = 0; f < allFaces.length; f++) {
			BlockFace face = allFaces[f];
			IntVector3 offset = face.getIntOffset();
			int nx = x + offset.getX();
			int ny = y + offset.getY();
			int nz = z + offset.getZ();
			
			short nId = material.getId(nx, ny, nz);
			if (nId == BlockMaterial.UNGENERATED.getId()) {
				continue;
			}
			int neighborLight = manager.getLightLevel(light, nx, ny, nz, true);
			
			short nData = material.getData(nx, ny, nz);
			BlockMaterial nMaterial = BlockMaterial.get(nId, nData);
			
			ByteBitSet occlusionSet = nMaterial.getOcclusion(nData);
			if (occlusionSet.get(face.getOpposite())) {
				continue;
			}
			
			int newLight = previousLevel - nMaterial.getOpacity() - 1;

			// Spout.getLogger().info("new light " + newLight + " neighbor light " + neighborLight + " for neighbor " + nx + ", " + ny + ", " + nz);

			if (newLight == neighborLight) {
				if (newLight >= 0) {
					// Spout.getLogger().info("Adding to dirty " + newLight + " for neighbor " + nx + ", " + ny + ", " + nz);
					dirtySets[newLight].add(nx, ny, nz);
				}
			} else {
				if (neighborLight > 0) {
					// Spout.getLogger().info("Adding to regen " + neighborLight + " for neighbor " + nx + ", " + ny + ", " + nz);
					regenSets[neighborLight].add(nx, ny, nz);
				}
			}
		}
		return true;
	}
}
