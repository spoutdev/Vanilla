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

import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.set.TInt10Procedure;
import org.spout.api.util.set.TInt10TripleSet;

public class ResolveHigherProcedure extends TInt10Procedure {
	
	private final static BlockFace[] allFaces = BlockFaces.NESWBT.toArray();
	private final TInt10TripleSet[] dirtySets;
	private int currentLevel;
	private final ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light;
	private final ImmutableCuboidBlockMaterialBuffer material;
	private final VanillaLightingManager manager;
	private final boolean checkAll;
	
	public ResolveHigherProcedure(VanillaLightingManager manager, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, TInt10TripleSet[] dirtySets, boolean checkAll) {
		this.dirtySets = dirtySets;
		this.light = light;
		this.material = material;
		this.manager = manager;
		this.currentLevel = 16;
		this.checkAll = checkAll;
	}

	public void setCurrentLevel(int level) {
		this.currentLevel = level;
	}

	@Override
	public boolean execute(int x, int y, int z) {
		return execute(x, y, z, false, false);
	}
	
	public boolean execute(int x, int y, int z, boolean root, boolean root2) {
		int lightLevel = manager.getLightLevel(light, x, y, z);
		int computedLevel = manager.computeLightLevel(light, material, x, y, z);
		//Spout.getLogger().info("Checking higher " + x + ", " + y + ", " + z + " actual " + lightLevel + " computed" + computedLevel + " root " + root);
		if (root2 ? (computedLevel >= lightLevel) : (checkAll || computedLevel > lightLevel)) {
			if (!root && !root2 && computedLevel != currentLevel) {
				throw new IllegalStateException("Light dirty block added to wrong set, computed level " + computedLevel + ", actual level " + currentLevel);
			}
			manager.setLightLevel(light, x, y, z, computedLevel);
			if (!root) {
				for (BlockFace face : allFaces) {
					Vector3 offset = face.getOffset();
					int nx = x + offset.getFloorX();
					int ny = y + offset.getFloorY();
					int nz = z + offset.getFloorZ();
					manager.checkAndAddDirtyRising(dirtySets, light, material, nx, ny, nz);
				}
			}
		}
		return true;
	}

}
