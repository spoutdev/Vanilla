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
import org.spout.api.lighting.LightingManager;
import org.spout.api.lighting.Modifiable;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.bytebit.ByteBitSet;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.set.TInt10TripleSet;

public abstract class VanillaLightingManager extends LightingManager<VanillaCuboidLightBuffer> {
	private final static BlockFace[] allFaces = BlockFaces.NESWBT.toArray();

	public VanillaLightingManager(String name) {
		super(name);
	}

	@Override
	public VanillaCuboidLightBuffer deserialize(Modifiable holder, int baseX, int baseY, int baseZ, int sizeX, int sizeY, int sizeZ, byte[] data) {
		return new VanillaCuboidLightBuffer(holder, getId(), baseX, baseY, baseZ, sizeX, sizeY, sizeZ, data);
	}

	@Override
	public void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int[] hx, int[] hz, int[] oldHy, int[] newHy, int changedColumns) {
	}

	// TODO - needs surface height data
	protected abstract int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, int x, int y, int z);

	protected boolean processDirty(TInt10TripleSet[] dirtySets, int currentLevel, boolean falling) {

		return false;
	}

	protected int computeLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int x, int y, int z) {
		BlockMaterial m = material.get(x, y, z);
		short data = material.getData(x, y, z);

		ByteBitSet occlusionSet = m.getOcclusion(data);

		if (occlusionSet.get(BlockFaces.NESWBT)) {
			return 0;
		}
		// TODO - needs to be made generic
		int opacity = m.getOpacity() + 1;

		// TODO - add this method -> probably need sub-classes of VanillaLightBuffer
		int neighborLight = getEmittedLight(material, x, y, z);
		for (int i = 0; i < 6; i++) {
			BlockFace face = allFaces[i];
			int faceLight = getIncomingLight(light, material, x, y, z, occlusionSet, face) - opacity;
			neighborLight = Math.max(neighborLight, faceLight);
		}

		return neighborLight;
	}

	public int getIncomingLight(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int x, int y, int z, ByteBitSet occlusionSet, BlockFace face) {
		if (occlusionSet.get(face)) {
			return 0;
		}
		Vector3 nOffset = face.getOffset();
		int nx = x + nOffset.getFloorX();
		int ny = y + nOffset.getFloorY();
		int nz = z + nOffset.getFloorZ();
		BlockMaterial m = material.get(nx, ny, nz);
		if (m == BlockMaterial.UNGENENERATED) {
			return 0;
		}
		short data = material.getData(nx, ny, nz);

		ByteBitSet neighborOcclusionSet = m.getOcclusion(data);
		if (neighborOcclusionSet.get(face.getOpposite())) {
			return 0;
		}

		return getLightLevel(light, nx, ny, nz);
	}

	public int getLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, int x, int y, int z) {
		VanillaCuboidLightBuffer buffer = light.getLightBuffer(x, y, z);
		int level = 0;
		if (buffer != null) {
			level = buffer.get(x, y, z);
		}
		return level;
	}

	public void setLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, int x, int y, int z, int level) {
		VanillaCuboidLightBuffer buffer = light.getLightBuffer(x, y, z);
		if (buffer != null) {
			buffer.set(x, y, z, (byte) level);
		} else {
			Spout.getLogger().info("No light buffer to write to");
		}
	}

	protected void checkAndAddDirty(TInt10TripleSet[] dirtySets, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, int x, int y, int z, boolean falling) {
		int actualLevel = getLightLevel(light, x, y, z);
		int calculatedLevel = computeLightLevel(light, material, x, y, z);
		if (falling) {
			if (calculatedLevel < actualLevel) {
				dirtySets[calculatedLevel].add(x, y, z);
			}
		} else {
			if (calculatedLevel > actualLevel) {
				dirtySets[calculatedLevel].add(x, y, z);
			}
		}
	}

	protected void resolveHigher(TInt10TripleSet[] dirtySets, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material) {
		ResolveProcedure proc = new ResolveProcedure(this, light, material, dirtySets);
		for (int i = dirtySets.length - 1; i >= 0; i--) {
			//Spout.getLogger().info("Start Processing level " + i + " (" + dirtySets[i].size()+ ")");
			proc.setCurrentLevel(i);
			dirtySets[i].forEach(proc);
			//Spout.getLogger().info("Done Processing level " + i);
		}
	}
}
