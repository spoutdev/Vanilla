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
import org.spout.api.math.Vector3;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.set.TInt10TripleSet;

public class VanillaBlocklightLightingManager extends VanillaLightingManager {
	public VanillaBlocklightLightingManager(String name) {
		super(name);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int[] x, int[] y, int[] z, int changedBlocks) {
		TInt10TripleSet[] dirtySets = new TInt10TripleSet[16];
		for (int i = 0; i < dirtySets.length; i++) {
			Vector3 base = light.getBase();
			dirtySets[i] = new TInt10TripleSet(base.getFloorX(), base.getFloorY(), base.getFloorZ());
		}
		//Spout.getLogger().info("Adding blocks from change list, " + changedBlocks + " " + getClass().getSimpleName());
		for (int i = 0; i < changedBlocks; i++) {
			checkAndAddDirty(dirtySets, light, material, x[i], y[i], z[i], false);
		}
		resolveHigher(dirtySets, light, material);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int[] bx, int[] by, int[] bz, int[] tx, int[] ty, int[] tz, int changedCuboids) {
		//Spout.getLogger().info(getClass().getSimpleName() + ":" + changedCuboids + " cuboids changed");
	}

	@Override
	protected int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, int x, int y, int z) {
		BlockMaterial m = material.get(x, y, z);
		short data = material.getData(x, y, z);
		return m.getLightLevel(data);
	}
}
