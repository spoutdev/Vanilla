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

import gnu.trove.iterator.hash.TObjectHashIterator;

import java.util.Iterator;

import org.spout.api.Spout;
import org.spout.api.geo.cuboid.Chunk;
import org.spout.api.lighting.LightingManager;
import org.spout.api.lighting.Modifiable;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.IntVector3;
import org.spout.api.math.Vector3;
import org.spout.api.util.IntVector3Array;
import org.spout.api.util.IntVector3CompositeIterator;
import org.spout.api.util.bytebit.ByteBitSet;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;
import org.spout.api.util.hashing.Int10TripleHashed;
import org.spout.api.util.set.TInt10TripleSet;
import org.spout.api.util.sort.TripleIntArraySort;

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
	public void resolveColumns(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] hx, int[] hz, int[] oldHy, int[] newHy, int changedColumns) {
	}

	protected void resolve(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, Iterable<IntVector3> coords) {
		TInt10TripleSet[] dirtySets = new TInt10TripleSet[16];
		TInt10TripleSet[] regenSets = new TInt10TripleSet[16];
		Vector3 base = light.getBase();
		for (int i = 0; i < dirtySets.length; i++) {
			dirtySets[i] = new TInt10TripleSet(base.getFloorX(), base.getFloorY(), base.getFloorZ());
			regenSets[i] = new TInt10TripleSet(base.getFloorX(), base.getFloorY(), base.getFloorZ());
		}
		// Spout.getLogger().info("Buffer type: " + getClass().getSimpleName());
		// Spout.getLogger().info("About to process higher emit");

		// Spout.getLogger().info("About to scan higher root");
		// Scan all root blocks and neighbours and see if any need to be made brighter
		// Adds blocks to dirty set corresponding corresponding to required level
		Iterator<IntVector3> itr = coords.iterator();
		while (itr.hasNext()) {
			IntVector3 v = itr.next();
			try {
				checkAndAddDirtyRising(dirtySets, light, material, height, v.getX(), v.getY(), v.getZ());
			} catch (IllegalArgumentException iae) {
				Spout.getLogger().info("Extra info");
				Spout.getLogger().info("Current coords: " + v);
				Spout.getLogger().info("Light: " + light.getBase());
				Spout.getLogger().info("Material: " + material.getBase());
				itr = coords.iterator();
				Spout.getLogger().info("All points: ");
				while (itr.hasNext()) {
					Spout.getLogger().info(itr.next().toString());
				}
				throw iae;
			}
		}
		// Spout.getLogger().info("About to process higher from sets");

		// Loop to resolve and propagate brightening of blocks
		// Updates are limited to blocks that could have been affected by the modified blocks
		resolveHigher(dirtySets, light, material, height);

		// Reset dirty sets for darkening pass
		for (int i = 0; i < dirtySets.length; i++) {
			dirtySets[i].clear();
		}

		// Spout.getLogger().info("About to process lower root");
		itr = coords.iterator();
		while (itr.hasNext()) {
			IntVector3 v = itr.next();
			checkAndAddDirtyFalling(dirtySets, regenSets, light, material, height, v.getX(), v.getY(), v.getZ());
		}
		// Spout.getLogger().info("About to process lower from sets");
		resolveLower(dirtySets, regenSets, light, material, height);
		
		purgeSet(light, regenSets);
		
		// Spout.getLogger().info("About to process higher (lower regen)");
		resolveHigher(regenSets, light, material, height);
		// Spout.getLogger().info("Done processing regen");
	}

	public static int sizeSets(TInt10TripleSet[] sets) {
		int count = 0;
		for (int i = 0; i < sets.length; i++) {
			count += sets[i].size();
		}
		return count;
	}

	// TODO - needs surface height data
	protected abstract int getEmittedLight(ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z);

	protected boolean processDirty(TInt10TripleSet[] dirtySets, int currentLevel, boolean falling) {
		return false;
	}

	protected int computeLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		BlockMaterial m = material.get(x, y, z);
		short data = material.getData(x, y, z);

		ByteBitSet occlusionSet = m.getOcclusion(data);

		if (occlusionSet.get(BlockFaces.NESWBT)) {
			return 0;
		}
		// TODO - needs to be made generic
		int opacity = m.getOpacity() + 1;

		// TODO - add this method -> probably need sub-classes of VanillaLightBuffer
		int neighborLight = getEmittedLight(material, height, x, y, z);

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
		IntVector3 nOffset = face.getIntOffset();
		int nx = x + nOffset.getX();
		int ny = y + nOffset.getY();
		int nz = z + nOffset.getZ();
		short id = material.getId(nx, ny, nz);
		if (id == BlockMaterial.UNGENERATED.getId()) {
			return 0;
		}
		short data = material.getData(nx, ny, nz);

		BlockMaterial m = BlockMaterial.get(id, data);
		
		ByteBitSet neighborOcclusionSet = m.getOcclusion(data);
		if (neighborOcclusionSet.get(face.getOpposite())) {
			return 0;
		}

		return getLightLevel(light, nx, ny, nz);
	}

	public int getLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, int x, int y, int z) {
		return getLightLevel(light, x, y, z, false);
	}

	public int getLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, int x, int y, int z, boolean allowNull) {
		VanillaCuboidLightBuffer buffer = light.getLightBuffer(x, y, z, allowNull);
		int level = 0;
		if (buffer != null) {
			level = buffer.get(x, y, z);
		}
		return level;
	}

	public void setLightLevel(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, int x, int y, int z, int level) {
		VanillaCuboidLightBuffer buffer = light.getLightBuffer(x, y, z);
		if (buffer != null) {
			//Spout.getLogger().info("Setting " + x + ", " + y + ", " + z + " " + buffer.get(x, y, z) + "->" + level);
			buffer.set(x, y, z, (byte) level);
		} else {
			Spout.getLogger().info("No light buffer to write to");
		}
	}
	
	public void purgeSet(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, TInt10TripleSet[] sets) {
		for (int i = 0; i < sets.length; i++) {
			TInt10TripleSet set = sets[i];
			TObjectHashIterator<Integer> itr = set.iterator();
			while (itr.hasNext()) {
				Int10TripleHashed hash = set.getHash();
				Integer key = itr.next();
				int x = hash.keyX(key);
				int y = hash.keyY(key);
				int z = hash.keyZ(key);
				int lightLevel = this.getLightLevel(light, x, y, z);
				if (lightLevel < i) {
					itr.remove();
				}
			}
		}
	}

	protected void checkAndAddDirtyFalling(TInt10TripleSet[] dirtySets, TInt10TripleSet[] regenSets, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		if (BlockMaterial.UNGENERATED.getId() == material.getId(x, y, z)) {
			return;
		}

		int actualLevel = getLightLevel(light, x, y, z);
		if (actualLevel <= 0) {
			return;
		}

		int calculatedLevel = computeLightLevel(light, material, height, x, y, z);

		if (calculatedLevel < actualLevel) {
			dirtySets[actualLevel].add(x, y, z);
		}
	}

	protected void checkAndAddDirtyRising(TInt10TripleSet[] dirtySets, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z) {
		if (BlockMaterial.UNGENERATED.getId() == material.getId(x, y, z)) {
			return;
		}
		int actualLevel = getLightLevel(light, x, y, z);
		int calculatedLevel = computeLightLevel(light, material, height, x, y, z);

		if (calculatedLevel >= actualLevel) {
			dirtySets[calculatedLevel].add(x, y, z);
		}
	}

	/**
	 * Resolves block changes that cause blocks to become brighter.  This method never causes the light level
	 * for blocks to be reduced.
	 * @param dirtySets
	 * @param light
	 * @param material
	 */
	protected void resolveHigher(TInt10TripleSet[] dirtySets, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height) {
		ResolveHigherProcedure resolveProc = new ResolveHigherProcedure(this, light, material, height, dirtySets);
		IncreaseLightProcedure increaseProc = new IncreaseLightProcedure(this, light);

		// Each dirty set contains block updates for blocks that need to be set to a particular level.
		// Since brighter blocks cannot be made brighter due to darker blocks, blocks are processed 
		// from brightest to darkest.  If the resolve step adds new blocks, it always adds to the
		// darker dirty sets.  The update procedure will never cause a block to be made darker.  This
		// handles the case where a block is set updated due to 2 different sources.  The source which
		// gives the brightest result will be the one that is used.
		for (int i = dirtySets.length - 1; i >= 0; i--) {
			// Updates light levels for blocks that need to be set to a light level of i
			increaseProc.setTargetLevel(i);
			dirtySets[i].forEach(increaseProc);

			// Scan all updated blocks and neighbours and add to dirty sets
			resolveProc.setTargetLevel(i);
			dirtySets[i].forEach(resolveProc);
		}
	}

	protected void resolveLower(TInt10TripleSet[] dirtySets, TInt10TripleSet[] regenSets, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height) {
		ClearLightProcedure clearProc = new ClearLightProcedure(this, light);
		ResolveLowerProcedure lowerProc = new ResolveLowerProcedure(this, light, material, height, dirtySets, regenSets);

		// All blocks in dirty sets need to be set to zero.  They are stored in the dirty set
		// index matching what level they had when determined to require setting to zero
		// Each block is set to zero and then the block and neighbours are scanned to see if any more
		// blocks need to be set to zero.
		//
		// When a light source is removed, darkness expands from that point until either light
		// level 0 blocks are reached, or blocks that are receiving light are reached.
		// 
		// Blocks which are not set to zero are added to the regen set.  These blocks are the source points
		// for propagating light back into the dark region
		//
		// Since darker blocks can't cause brighter blocks to go darker, blocks are processed based
		// on their level before they were marked as needing to be set to zero.  This also acts as a
		// limit on the distance processing can occur due to a block modification.
		//
		// This method does not handle the light regeneration step
		for (int i = dirtySets.length - 1; i >= 0; i--) {
			dirtySets[i].forEach(clearProc);
			lowerProc.setPreviousLevel(i);
			dirtySets[i].forEach(lowerProc);
		}
	}

	/**
	 * Copies the emitted light values of each block in the given chunk into the given array for coords (1, 1, 1) to (16, 16, 16)
	 * 
	 * @param light
	 * @param material
	 * @param height
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public abstract void updateEmittingBlocks(int[][][] emittedLight, ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int x, int y, int z);

	@Override
	protected void initChunks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] bx, int[] by, int[] bz, int initializedChunks) {
		// Scan for new chunks needs to check
		// - light emitting blocks
		// - boundary of entire volume
		
		//TripleIntArraySort.tripleIntArraySort(bx, bz, by, initializedChunks);
		
		int[][][] emitLight = new int[18][18][18];

		@SuppressWarnings("unchecked")
		Iterable<IntVector3>[] emitters = new Iterable[initializedChunks];
		
		for (int i = 0; i < initializedChunks;i++) {
			fillEdges(emitLight);
			updateEmittingBlocks(emitLight, light, material, height, bx[i], by[i], bz[i]);
			emitters[i] = scanForEmitEdges(bx[i], by[i], bz[i], emitLight);
			copyToLightBuffer(light, bx[i], by[i], bz[i], emitLight);
		}
		
		Iterable<IntVector3> combined = new IntVector3CompositeIterator(emitters);

		resolve(light, material, height, combined);
	}
	
	public void fillEdges(int[][][] emitLight) {
		int max = Chunk.BLOCKS.SIZE - 1;
		for (int x = 0; x < 18; x++) {
			for (int y = 0; y < 18; y++) {
				emitLight[0][y][x] = -1;
				emitLight[max][y][x] = -1;
				emitLight[x][y][0] = -1;
				emitLight[x][y][max] = -1;
				emitLight[x][0][y] = -1;
				emitLight[x][max][y] = -1;
			}
		}
	}
	
	public void copyToLightBuffer(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, int bx, int by, int bz, int[][][] emitLight) {
		VanillaCuboidLightBuffer buffer = light.getLightBuffer(bx, by, bz);
		for (int x = 1; x < Chunk.BLOCKS.SIZE + 1; x++) {
			for (int y = 1; y < Chunk.BLOCKS.SIZE + 1; y++) {
				int xPos = bx + x - 1;
				int yPos = by + y - 1;
				int zPos = bz;
				for (int z = 1; z < Chunk.BLOCKS.SIZE + 1; z++) {
					buffer.set(xPos, yPos, zPos++, (byte) emitLight[x][y][z]);
				}
			}
		}
	}
	
	public Iterable<IntVector3> scanForEmitEdges(int bx, int by, int bz, int[][][] emitLight) {
		int[] ex = new int[Chunk.BLOCKS.VOLUME];
		int[] ey = new int[Chunk.BLOCKS.VOLUME];
		int[] ez = new int[Chunk.BLOCKS.VOLUME];
		
		int index = 0;
		
		for (int x = 1; x < Chunk.BLOCKS.SIZE + 1; x++) {
			for (int y = 1; y < Chunk.BLOCKS.SIZE + 1; y++) {
				for (int z = 1; z < Chunk.BLOCKS.SIZE + 1; z++) {
					int center = emitLight[x][y][z];
					if (
							emitLight[x-1][y][z] < center || 
							emitLight[x+1][y][z] < center ||
							emitLight[x][y+1][z] < center ||
							emitLight[x][y-1][z] < center ||
							emitLight[x][y][z+1] < center ||
							emitLight[x][y][z-1] < center
						) {
						index = append(x + bx - 1, y + by - 1, z + bz - 1, ex, ey, ez, index);
					}
				}
			}
		}
		
		if (index == 0) {
			return null;
		} else {
			return new IntVector3Array(ex, ey, ez, index);
		}
	}
	
	private static int append(int x, int y, int z, int[] ax, int[] ay, int[] az, int pos) {
		ax[pos] = x;
		ay[pos] = y;
		az[pos] = z;
		return pos + 1;
	}
	
}
