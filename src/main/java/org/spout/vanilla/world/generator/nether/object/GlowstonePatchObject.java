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
package org.spout.vanilla.world.generator.nether.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.math.vector.Vector3f;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.object.RandomObject;
import org.spout.vanilla.world.generator.object.RandomizableObject;

public class GlowstonePatchObject extends RandomObject implements RandomizableObject {
	private byte baseSize = 20;
	private byte randSize = 21;
	private byte totalSize;
	private byte maxXRadius = 8;
	private byte maxYDepth = 12;
	private byte maxZRadius = 8;
	private BlockMaterial attachTo = VanillaMaterials.NETHERRACK;
	private BlockMaterial main = VanillaMaterials.GLOWSTONE_BLOCK;

	public GlowstonePatchObject(Random random) {
		super(random);
		randomize();
	}

	public GlowstonePatchObject() {
		this(null);
	}

	@Override
	public boolean canPlaceObject(World w, int x, int y, int z) {
		final Block block = w.getBlock(x, y, z);
		return block.isMaterial(VanillaMaterials.AIR)
				&& block.translate(BlockFace.TOP).isMaterial(attachTo);
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		final Vector3f seed = new Vector3f(x, y, z);
		final List<Growth> growths = new ArrayList<Growth>();
		growths.add(new Growth(w.getBlock(seed)));
		byte count = 0;
		while (!growths.isEmpty() && count < totalSize) {
			final Growth current = growths.get(0);
			if (count == 0 || (isInBounds(seed, current.getPosition()) && current.canGrow())) {
				current.grow();
				count++;
				growths.addAll(current.getNextGrowths(current));
			}
			growths.remove(current);
		}
	}

	private boolean isInBounds(Vector3f seed, Vector3f target) {
		final Vector3f diff = target.sub(seed);
		return Math.abs(diff.getFloorX()) < maxXRadius && Math.abs(diff.getFloorZ()) < maxZRadius
				&& diff.getFloorY() > -maxYDepth;
	}

	@Override
	public final void randomize() {
		totalSize = (byte) (random.nextInt(randSize) + baseSize);
	}

	private class Growth {
		private final Block block;

		public Growth(Block block) {
			this.block = block;
		}

		public boolean canGrow() {
			if (!block.isMaterial(VanillaMaterials.AIR)) {
				return false;
			}
			byte adjacentGlowstoneCount = 0;
			for (BlockFace face : BlockFaces.ALL) {
				if (block.translate(face).isMaterial(main)) {
					if (++adjacentGlowstoneCount > 1) {
						return false;
					}
				}
			}
			return true;
		}

		public void grow() {
			block.setMaterial(main);
		}

		public List<Growth> getNextGrowths(Growth last) {
			final List<Growth> nextGrowths = new ArrayList<Growth>();
			for (BlockFace face : BlockFaces.ALL) {
				final Block next = block.translate(face);
				if (!next.equals(last) && random.nextBoolean()) {
					nextGrowths.add(new Growth(next));
				}
			}
			return nextGrowths;
		}

		public Point getPosition() {
			return block.getPosition();
		}
	}

	public void setAttachTo(BlockMaterial attachTo) {
		this.attachTo = attachTo;
	}

	public void setBaseSize(byte baseSize) {
		this.baseSize = baseSize;
	}

	public void setMain(BlockMaterial main) {
		this.main = main;
	}

	public void setRandSize(byte randSize) {
		this.randSize = randSize;
	}

	public void setTotalSize(byte totalSize) {
		this.totalSize = totalSize;
	}

	public void setMaxXRadius(byte maxXRadius) {
		this.maxXRadius = maxXRadius;
	}

	public void setMaxZRadius(byte maxZRadius) {
		this.maxZRadius = maxZRadius;
	}

	public void setMaxYDepth(byte maxYDepth) {
		this.maxYDepth = maxYDepth;
	}
}
