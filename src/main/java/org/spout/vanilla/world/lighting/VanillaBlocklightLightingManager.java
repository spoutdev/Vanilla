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
import org.spout.api.math.IntVector3;
import org.spout.api.util.IntVector3Array;
import org.spout.api.util.IntVector3CuboidArray;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;

public class VanillaBlocklightLightingManager extends VanillaLightingManager {
	public VanillaBlocklightLightingManager(String name) {
		super(name);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int[] x, int[] y, int[] z, int changedBlocks) {
		Iterable<IntVector3> coords = new IntVector3Array(x, y, z, changedBlocks);
		super.resolve(light, material, coords);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int[] bx, int[] by, int[] bz, int[] tx, int[] ty, int[] tz, int changedCuboids) {
		Iterable<IntVector3> coords = new IntVector3CuboidArray(bx, by, bz, tx, ty, tz, changedCuboids);
		super.resolve(light, material, coords);
	}

	@Override
	protected int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, int x, int y, int z) {
		BlockMaterial m = material.get(x, y, z);
		short data = material.getData(x, y, z);
		return m.getLightLevel(data);
	}
}
