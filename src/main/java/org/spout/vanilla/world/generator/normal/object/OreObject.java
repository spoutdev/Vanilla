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
package org.spout.vanilla.world.generator.normal.object;

import java.util.Random;
import org.spout.vanilla.world.generator.object.RandomObject;

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;
import org.spout.api.math.TrigMath;
import org.spout.api.math.Vector2;
import org.spout.api.math.VectorMath;

import org.spout.vanilla.material.VanillaMaterials;

public class OreObject extends RandomObject {
	private int amount;
	private int maxHeight;
	private int clusterSize;
	private BlockMaterial material;

	public OreObject(BlockMaterial material, int count, int clusterSize, int maxHeight) {
		this(null, material, count, clusterSize, maxHeight);
	}

	public OreObject(Random random, BlockMaterial material, int count, int clusterSize, int maxHeight) {
		super(random);
		this.amount = count;
		this.material = material;
		this.maxHeight = maxHeight;
		this.clusterSize = clusterSize;
	}

	public OreObject(Random random, OreType type) {
		this(random, type.material, type.count, type.clusterSize, type.maxHeight);
	}

	public OreObject(OreType type) {
		this(null, type);
	}

	/**
	 * Gets the maximum height this ore should be placed
	 *
	 * @return maximum height
	 */
	public int getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Gets the amount of this ore placed per chunk
	 *
	 * @return placement count
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the size of a cluster of this ore
	 *
	 * @return cluster size
	 */
	public int getClusterSize() {
		return clusterSize;
	}

	/**
	 * Gets the ore material used
	 *
	 * @return ore material
	 */
	public BlockMaterial getMaterial() {
		return this.material;
	}

	public void setClusterSize(int clusterSize) {
		this.clusterSize = clusterSize;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setMaterial(BlockMaterial material) {
		this.material = material;
	}

	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		return true;
	}

	/**
	 * Generates a cluster of this ore at the position using the random given
	 *
	 * @param world to place in
	 * @param originX of the cluster
	 * @param originY of the cluster
	 * @param originZ of the cluster
	 */
	@Override
	public void placeObject(World world, int originX, int originY, int originZ) {
		final float angle = random.nextFloat() * (float) Math.PI;
		Vector2 offset = VectorMath.getDirection2D(angle).multiply(clusterSize).divide(8);
		final float x1 = ((originX + 8) + offset.getX());
		final float x2 = ((originX + 8) - offset.getX());
		final float z1 = ((originZ + 8) + offset.getY());
		final float z2 = ((originZ + 8) - offset.getY());
		final float y1 = (originY + random.nextInt(3) + 2);
		final float y2 = (originY + random.nextInt(3) + 2);

		for (int count = 0; count <= clusterSize; count++) {
			final float seedX = x1 + (x2 - x1) * count / clusterSize;
			final float seedY = y1 + (y2 - y1) * count / clusterSize;
			final float seedZ = z1 + (z2 - z1) * count / clusterSize;
			final float size = ((TrigMath.sin(count * (float) Math.PI / clusterSize) + 1) * random.nextFloat() * clusterSize / 16 + 1) / 2;

			final int startX = (int) (seedX - size);
			final int startY = (int) (seedY - size);
			final int startZ = (int) (seedZ - size);
			final int endX = (int) (seedX + size);
			final int endY = (int) (seedY + size);
			final int endZ = (int) (seedZ + size);

			for (int x = startX; x <= endX; x++) {
				float sizeX = (x + 0.5f - seedX) / size;
				sizeX *= sizeX;

				if (sizeX < 1) {
					for (int y = startY; y <= endY; y++) {
						float sizeY = (y + 0.5f - seedY) / size;
						sizeY *= sizeY;

						if (sizeX + sizeY < 1) {
							for (int z = startZ; z <= endZ; z++) {
								float sizeZ = (z + 0.5f - seedZ) / size;
								sizeZ *= sizeZ;
								if (sizeX + sizeY + sizeZ < 1 && world.getBlockMaterial(x, y, z).isMaterial(VanillaMaterials.STONE)) {
									world.setBlockMaterial(x, y, z, material, (short) 0, null);
								}
							}
						}
					}
				}
			}
		}
	}

	public static class OreTypes {
		public static final OreType DIRT = new OreType(VanillaMaterials.DIRT, 20, 32, 128);
		public static final OreType GRAVEL = new OreType(VanillaMaterials.GRAVEL, 10, 32, 128);
		public static final OreType COAL = new OreType(VanillaMaterials.COAL_ORE, 20, 16, 128);
		public static final OreType IRON = new OreType(VanillaMaterials.IRON_ORE, 20, 8, 64);
		public static final OreType REDSTONE = new OreType(VanillaMaterials.REDSTONE_ORE, 8, 7, 16);
		public static final OreType LAPIS_LAZULI = new OreType(VanillaMaterials.LAPIS_LAZULI_ORE, 1, 6, 32);
		public static final OreType GOLD = new OreType(VanillaMaterials.GOLD_ORE, 2, 8, 32);
		public static final OreType DIAMOND = new OreType(VanillaMaterials.DIAMOND_ORE, 1, 7, 16);

		private OreTypes() {
		}
	}

	public static class OreType {
		private final BlockMaterial material;
		private final int count;
		private final int clusterSize;
		private final int maxHeight;

		private OreType(BlockMaterial material, int count, int clusterSize, int maxHeight) {
			this.material = material;
			this.count = count;
			this.clusterSize = clusterSize;
			this.maxHeight = maxHeight;
		}

		public int getClusterSize() {
			return clusterSize;
		}

		public int getCount() {
			return count;
		}

		public BlockMaterial getMaterial() {
			return material;
		}

		public int getMaxHeight() {
			return maxHeight;
		}
	}
}
