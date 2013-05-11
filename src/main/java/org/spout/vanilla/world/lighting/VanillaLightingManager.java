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

import java.util.Arrays;
import java.util.Comparator;
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
import org.spout.api.util.bytebit.ByteBitSet;
import org.spout.api.util.cuboid.ChunkCuboidLightBufferWrapper;
import org.spout.api.util.cuboid.ImmutableCuboidBlockMaterialBuffer;
import org.spout.api.util.cuboid.ImmutableHeightMapBuffer;
import org.spout.api.util.hashing.Int10TripleHashed;
import org.spout.api.util.list.IntVector3FIFO;
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
	public void resolveColumns(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, int[] hx, int[] hz, int[] oldHy, int[] newHy, int changedColumns) {
	}
	
	@Override
	public VanillaCuboidLightBuffer newLightBuffer(Modifiable holder, int baseX, int baseY, int baseZ, int sizeX, int sizeY, int sizeZ) {
		return deserialize(holder, baseX, baseY, baseZ, sizeX, sizeY, sizeZ, null);
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
	protected void initChunks(ChunkCuboidLightBufferWrapper<VanillaCuboidLightBuffer> light, ImmutableCuboidBlockMaterialBuffer material, ImmutableHeightMapBuffer height, Chunk[] chunks) {
		// Scan for new chunks needs to check
		// - boundary of entire volume
		
		Iterable<IntVector3> boundary = getBoundary(material, chunks);
		
		resolve(light, material, height, boundary);
	}
	
	private final static Comparator<Chunk> chunkSorter = new Comparator<Chunk>() {

		@Override
		public int compare(Chunk c1, Chunk c2) {
			int genDiff = c1.getGenerationIndex() - c2.getGenerationIndex();
			if (genDiff != 0) {
				return genDiff;
			}
			// X sort rising
			if (c1.getX() != c2.getX()) {
				return c1.getX() - c2.getX();
			}
			// Z sort rising
			if (c1.getZ() != c2.getZ()) {
				return c1.getZ() - c2.getZ();
			}
			// Y sort falling
			return c2.getY() - c1.getY();
		}
		
	};
	
	protected Iterable<IntVector3> getBoundary(ImmutableCuboidBlockMaterialBuffer material, Chunk[] chunks) {

		Arrays.sort(chunks, chunkSorter);
		
		int blocks = chunks.length << 11;
		
		int[] xArray = new int[blocks];
		int[] yArray = new int[blocks];
		int[] zArray = new int[blocks];

		int count = 0;
		
		int startChunk = 0;
		int endChunk = 0;
		
		while (startChunk < chunks.length) {
			int genIndex = chunks[startChunk].getGenerationIndex();
			endChunk = startChunk + 1;
			
			while (endChunk < chunks.length && chunks[endChunk].getGenerationIndex() == genIndex) {
				endChunk++;
			}
			
			count = getBoundary(chunks, count, xArray, yArray, zArray, startChunk, endChunk);
			
			startChunk = endChunk;
		}
		
		return new IntVector3Array(xArray, yArray, zArray, count);
	}
	
	private int getBoundary(Chunk[] chunks, int count, int[] xArray, int[] yArray, int[] zArray, int startChunk, int endChunk) {
		
		Chunk first = chunks[startChunk];
		Chunk last = chunks[endChunk - 1];
		
		// Note: X and Z are sorted low to high and Y is sorted high to low
		
		int baseX = first.getX();
		int baseY = last.getY();
		int baseZ = first.getZ();
		
		int topX = last.getX() + 1;
		int topY = first.getY() + 1;
		int topZ = last.getZ() + 1;
		
		int sizeX = topX - baseX;
		int sizeY = topY - baseY;
		int sizeZ = topZ - baseZ;
		
		int volume = sizeX * sizeY * sizeZ;
		
		if (volume != (endChunk - startChunk)) {
			throw new IllegalStateException("Chunks must be generated in cuboids");
		}
		
		Chunk[][][] cuboid = new Chunk[sizeX][sizeY][sizeZ];
		
		int index = startChunk;
		for (int x = 0; x < sizeX; x++) {
			for (int z = 0; z < sizeZ; z++) {
				for (int y = sizeY - 1; y >=0; y--) {
					if (cuboid[x][y][z] == null) {
						Chunk c = chunks[index++];
						if (c.getX() != baseX + x || c.getY() != baseY + y || c.getZ() != baseZ + z) {
							throw new IllegalStateException("Chunks incorrectly ordered " + c.getBase().toChunkString());
						}
						cuboid[x][y][z] = c;
					} else {
						throw new IllegalStateException("Chunks appears twice in list, " + x + ", " + y + ", " + z);
					}
				}
			}
		}
		
		int baseBlockX = baseX << Chunk.BLOCKS.BITS;
		int baseBlockY = baseY << Chunk.BLOCKS.BITS;
		int baseBlockZ = baseZ << Chunk.BLOCKS.BITS;
		
		int topBlockX = topX << Chunk.BLOCKS.BITS;
		int topBlockY = topY << Chunk.BLOCKS.BITS;
		int topBlockZ = topZ << Chunk.BLOCKS.BITS;
		
		
		for (int x = baseBlockX; x < topBlockX; x++) {
			for (int y = baseBlockY; y < topBlockY; y++) {
				xArray[count] = x;
				yArray[count] = y;
				zArray[count] = baseBlockZ;
				count++;
				xArray[count] = x;
				yArray[count] = y;
				zArray[count] = topBlockZ - 1;
				count++;
			}
		}
		
		for (int x = baseBlockX; x < topBlockX; x++) {
			for (int z = baseBlockZ; z < topBlockZ; z++) {
				xArray[count] = x;
				yArray[count] = baseBlockY;
				zArray[count] = z;
				count++;
				xArray[count] = x;
				yArray[count] = topBlockY - 1;
				zArray[count] = z;
				count++;
			}
		}
		
		for (int z = baseBlockZ; z < topBlockZ; z++) {
			for (int y = baseBlockY; y < topBlockY; y++) {
				xArray[count] = baseBlockX;
				yArray[count] = y;
				zArray[count] = z;
				count++;
				xArray[count] = topBlockX - 1;
				yArray[count] = y;
				zArray[count] = z;
				count++;
			}
		}
		
		return count;
		
	}

	
	public abstract void bulkEmittingInitialize(ImmutableCuboidBlockMaterialBuffer buffer, int[][][] light, int[][] height);
	
	@Override
	public VanillaCuboidLightBuffer[][][] bulkInitialize(ImmutableCuboidBlockMaterialBuffer buffer, int[][] height) {
		Vector3 size = buffer.getSize();
		
		final int sizeX = size.getFloorX();
		final int sizeY = size.getFloorY();
		final int sizeZ = size.getFloorZ();
		
		Vector3 base = buffer.getBase();
		
		final int baseX = base.getFloorX();
		final int baseY = base.getFloorY();
		final int baseZ = base.getFloorZ();
		
		Vector3 top = buffer.getTop();
		
		final int topX = top.getFloorX();
		final int topY = top.getFloorY();
		final int topZ = top.getFloorZ();
		
		// TODO - this should be passed as a input parameter
		//        It still needs to scan the new buffer, since it hasn't been added to the column
		
		final boolean[][][] dirty = new boolean[sizeX + 2][sizeY + 2][sizeZ + 2];
		final int[][][] newLight = new int[sizeX + 2][sizeY + 2][sizeZ + 2];
		final IntVector3FIFO fifo = new IntVector3FIFO((sizeX + 2) * (sizeY + 2) * (sizeZ + 2));
		
		bulkEmittingInitialize(buffer, newLight, height); 
		
		// Mark the edges as dirty so they don't get added to the FIFO
		
		for (int x = 0; x <= sizeX + 1; x++) {
			for (int y = 0; y <= sizeY + 1; y++) {
				dirty[x][y][0] = true;
				dirty[x][y][sizeZ + 1] = true;
			}
		}
		
		for (int x = 0; x <= sizeX + 1; x++) {
			for (int z = 0; z <= sizeZ + 1; z++) {
				dirty[x][0][z] = true;
				dirty[x][sizeY + 1][z] = true;
			}
		}
		
		for (int y = 0; y <= sizeY + 1; y++) {
			for (int z = 0; z <= sizeZ + 1; z++) {
				dirty[0][y][z] = true;
				dirty[sizeX + 1][y][z] = true;
			}
		}
		
		for (int x = 1; x <= sizeX; x++) {
			for (int y = 1; y <= sizeY; y++) {
				for (int z = 1; z <= sizeZ; z++) {
					if (newLight[x][y][z] > 0) {
						fifo.write(x, y, z);
						dirty[x][y][z] = true;
					}
				}
			}
		}
		
		IntVector3 v;
		
		while ((v = fifo.read()) != null) {
			
			int x = v.getX();
			int y = v.getY();
			int z = v.getZ();
			
			BlockMaterial m = buffer.get(x + baseX - 1, y + baseY - 1, z + baseZ - 1);
			
			ByteBitSet occulusion = m.getOcclusion(m.getData());
			
			int center = newLight[x][y][z];

			for (BlockFace face : allFaces) {
				if (occulusion.get(face)) {
					continue;
				}
				IntVector3 off = face.getIntOffset();
				int nx = x + off.getX();
				int ny = y + off.getY();
				int nz = z + off.getZ();
				if (nx <= 0 || nx > sizeX || ny <= 0 || ny > sizeY || nz <= 0 || nz >= sizeZ) {
					continue;
				}
				BlockMaterial other = buffer.get(nx + baseX - 1, ny + baseY - 1, nz + baseZ - 1);

				int opacity = other.getOpacity() + 1;

				int newLevel = center - opacity;

				if (newLevel > newLight[nx][ny][nz] && !other.getOcclusion(other.getData()).get(face.getOpposite())) {
					newLight[nx][ny][nz] = newLevel;
					if (!dirty[nx][ny][nz]) {
						dirty[nx][ny][nz] = true;
						fifo.write(nx, ny, nz);
					}
				}
			}
			dirty[x][y][z] = false;
		}
		
		int cx = sizeX >> Chunk.BLOCKS.BITS;
		int cy = sizeY >> Chunk.BLOCKS.BITS;
		int cz = sizeZ >> Chunk.BLOCKS.BITS;
		
		VanillaCuboidLightBuffer[][][] lightBufferArray = new VanillaCuboidLightBuffer[cx][cy][cz];
		
		for (int x = 0; x < cx; x++) {
			for (int y = 0; y < cy; y++) {
				for (int z = 0; z < cz; z++) {
					
					int shift = Chunk.BLOCKS.BITS;
					int chunkSize = Chunk.BLOCKS.SIZE;
					
					VanillaCuboidLightBuffer light = newLightBuffer(null, baseX + (cx << shift), baseY + (cy << shift), baseZ + (cz << shift), chunkSize, chunkSize, chunkSize);
					
					Vector3 lightBase = light.getBase();
					
					int lightBaseX = lightBase.getFloorX();
					int lightBaseY = lightBase.getFloorY();
					int lightBaseZ = lightBase.getFloorZ();
					
					int arrayStart = 1 + (z << shift);
					int arrayEnd = arrayStart + chunkSize;
					
					int xOff = 1 + (x << shift);
					int yOff = 1 + (y << shift);
					
					for (int bx = 0; bx < chunkSize; bx++) {
						for (int by = 0; by < chunkSize; by++) {
							light.copyZRow(lightBaseX + bx, lightBaseY + by, lightBaseZ, arrayStart, arrayEnd, newLight[xOff + bx][yOff + by]);
						}
					}
					
					lightBufferArray[x][y][z] = light;
				}
			}
		}

		return lightBufferArray;
	}

}
