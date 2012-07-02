/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, VanillaDev <http://www.spout.org/>
 * Vanilla is licensed under the SpoutDev License Version 1.
 *
 * Vanilla is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the SpoutDev License Version 1.
 *
 * Vanilla is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the SpoutDev License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://www.spout.org/SpoutDevLicenseV1.txt> for the full license,
 * including the MIT license.
 */
package org.spout.vanilla.world.generator.normal.object.tree;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.material.block.plant.Sapling;
import org.spout.vanilla.world.generator.object.LargePlantObject;
import org.spout.vanilla.world.generator.object.VanillaObjects;

public abstract class TreeObject extends LargePlantObject {
	// metadata control
	protected short leavesMetadata;
	protected short logMetadata;
	// for canPlaceObject check
	protected final Set<BlockMaterial> overridable = new HashSet<BlockMaterial>();

	protected TreeObject(byte baseHeight, byte randomHeight, short metadata) {
		this(null, baseHeight, randomHeight, metadata, metadata);
	}

	protected TreeObject(Random random, byte baseHeight, byte randomHeight, short metadata) {
		this(random, baseHeight, randomHeight, metadata, metadata);
	}

	protected TreeObject(Random random, byte baseHeight, byte randomHeight, short leavesMetadata, short logMetadata) {
		super(random, baseHeight, randomHeight);
		this.leavesMetadata = leavesMetadata;
		this.logMetadata = logMetadata;
	}

	public void setLeavesMetadata(short leavesMetadata) {
		this.leavesMetadata = leavesMetadata;
	}

	public void setLogMetadata(short logMetadata) {
		this.logMetadata = logMetadata;
	}

	public Set<BlockMaterial> getOverridableMaterials() {
		return overridable;
	}

	public void setTreeType(TreeType type) {
		leavesMetadata = type.metadata;
		logMetadata = type.metadata;
	}

	public static void growTree(Sapling sapling, Block pos, Random random) {
		final TreeObject tree;
		final World world = pos.getWorld();
		final int y = pos.getY();
		if (sapling == Sapling.JUNGLE) {
			final int x = pos.getX();
			final int z = pos.getZ();
			byte saplingCount = 0;
			Block firstSapling = null;
			for (byte xx = -1; xx < 2; xx++) {
				for (byte zz = -1; zz < 2; zz++) {
					if (world.getBlockMaterial(x + xx, y, z + zz) == Sapling.JUNGLE) {
						saplingCount++;
						if (saplingCount == 1) {
							firstSapling = world.getBlock(x + xx, y, z + zz);
						}
					}
				}
			}
			if (saplingCount > 3 && firstSapling.translate(1, 0, 1).getMaterial() == Sapling.JUNGLE
					&& firstSapling.translate(0, 0, 1).getMaterial() == Sapling.JUNGLE
					&& firstSapling.translate(1, 0, 0).getMaterial() == Sapling.JUNGLE) {
				pos = firstSapling;
				tree = VanillaObjects.HUGE_JUNGLE_TREE;
			} else {
				tree = VanillaObjects.SMALL_JUNGLE_TREE;
			}
		} else if (sapling == Sapling.BIRCH) {
			tree = VanillaObjects.SMALL_BIRCH_TREE;
		} else if (sapling == Sapling.SPRUCE) {
			if (random.nextBoolean()) {
				tree = VanillaObjects.PINE_TREE;
			} else {
				tree = VanillaObjects.SPRUCE_TREE;
			}
		} else {
			if (random.nextInt(10) == 0) {
				tree = VanillaObjects.BIG_OAK_TREE;
			} else {
				tree = VanillaObjects.SMALL_OAK_TREE;
			}
		}
		tree.setRandom(random);
		tree.randomize();
		final int x = pos.getX();
		final int z = pos.getZ();
		if (tree.canPlaceObject(world, x, y, z)) {
			tree.placeObject(world, x, y, z);
		}
	}

	public static enum TreeType {
		OAK((short) 0),
		SPRUCE_PINE((short) 1),
		BIRCH((short) 2),
		JUNGLE((short) 3);
		//
		final private short metadata;
		final private static TreeType[] cache;

		static {
			cache = new TreeType[TreeType.values().length];
			for (TreeType t : TreeType.values()) {
				cache[t.getMetadata() & 0xFFFF] = t;
			}
		}

		public static TreeType getType(int metadata) {
			if (metadata < 0 || metadata > 3) {
				return null;
			}

			return cache[metadata];
		}

		private TreeType(short metadata) {
			this.metadata = metadata;
		}

		public short getMetadata() {
			return metadata;
		}
	}
}
