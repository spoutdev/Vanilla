/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
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

import org.spout.api.geo.World;
import org.spout.api.material.BlockMaterial;

import org.spout.math.TrigMath;
import org.spout.math.vector.Vector2;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.RandomObject;

public class OreObject extends RandomObject {
	private final OreType type;

	public OreObject(Random random, OreType type) {
		super(random);
		this.type = type;
	}

	public OreObject(OreType type) {
		this(null, type);
	}

	public OreType getType() {
		return type;
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
		final int clusterSize = type.clusterSize;
		final float angle = random.nextFloat() * (float) Math.PI;
		final Vector2 offset = Vector2.createDirection(angle).mul(clusterSize).div(8);
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
									world.setBlockMaterial(x, y, z, type.material, (short) 0, null);
								}
							}
						}
					}
				}
			}
		}
	}

	public static class VanillaOreTypes {
		public static final OreType DIRT = new OreType(VanillaMaterials.DIRT, 20, 32, 0, 128);
		public static final OreType GRAVEL = new OreType(VanillaMaterials.GRAVEL, 10, 32, 0, 128);
		public static final OreType COAL = new OreType(VanillaMaterials.COAL_ORE, 20, 16, 0, 128);
		public static final OreType IRON = new OreType(VanillaMaterials.IRON_ORE, 20, 8, 0, 64);
		public static final OreType REDSTONE = new OreType(VanillaMaterials.REDSTONE_ORE, 8, 7, 0, 16);
		public static final OreType LAPIS_LAZULI = new OreType(VanillaMaterials.LAPIS_LAZULI_ORE, 1, 6, 0, 32);
		public static final OreType GOLD = new OreType(VanillaMaterials.GOLD_ORE, 2, 8, 0, 32);
		public static final OreType DIAMOND = new OreType(VanillaMaterials.DIAMOND_ORE, 1, 7, 0, 16);

		private VanillaOreTypes() {
		}
	}

	public static class OreType {
		private final BlockMaterial material;
		private final int clusterCount;
		private final int clusterSize;
		private final int maxHeight;
		private final int minHeight;

		public OreType(BlockMaterial material, int clusterCount, int clusterSize,
					   int minHeight, int maxHeight) {
			this.material = material;
			this.clusterCount = clusterCount;
			this.clusterSize = clusterSize;
			this.maxHeight = maxHeight;
			this.minHeight = minHeight;
		}

		public int getClusterSize() {
			return clusterSize;
		}

		public int getClusterCount() {
			return clusterCount;
		}

		public BlockMaterial getMaterial() {
			return material;
		}

		public int getMinHeight() {
			return minHeight;
		}

		public int getMaxHeight() {
			return maxHeight;
		}
	}
}
