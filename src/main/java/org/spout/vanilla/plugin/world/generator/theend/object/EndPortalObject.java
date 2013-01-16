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
package org.spout.vanilla.plugin.world.generator.theend.object;

import java.util.Random;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.plugin.material.VanillaMaterials;
import org.spout.vanilla.plugin.world.generator.object.RandomObject;

public class EndPortalObject extends RandomObject {

	private static boolean isEndFrame(Block block, BlockFace facing, boolean withEnderEye) {
		return (block.isMaterial(VanillaMaterials.END_PORTAL_FRAME) &&
				VanillaMaterials.END_PORTAL_FRAME.getFacing(block) == facing) &&
				(!withEnderEye || VanillaMaterials.END_PORTAL_FRAME.hasEyeOfTheEnder(block));
	}

	private static void placeFrame(Block block, BlockFace facing) {
		block.setMaterial(VanillaMaterials.END_PORTAL_FRAME);
		VanillaMaterials.END_PORTAL_FRAME.setFacing(block, facing);
	}

	public EndPortalObject() {
		super();
	}

	public EndPortalObject(Random random) {
		super(random);
	}

	/**
	 * Finds the center of the end portal object
	 * @param frameBlock to start looking from
	 * @return Center origin block
	 */
	private Block getOrigin(Block frameBlock) {
		if (!frameBlock.isMaterial(VanillaMaterials.END_PORTAL_FRAME)) {
			return null;
		}
		BlockFace facing = VanillaMaterials.END_PORTAL_FRAME.getFacing(frameBlock);
		// Rotates the facing 90 degrees to the left
		BlockFace lookDirection = BlockFaces.NESW.previous(facing);
		// Get the corner piece
		Block corner = frameBlock;
		for (int i = 0; i < 4 && isEndFrame(corner, facing, false); i++) {
			corner = corner.translate(lookDirection);
		}
		// Now go two steps back and two steps towards the middle (facing)
		return corner.translate(lookDirection, -2).translate(facing, 2);
	}

	private boolean findFrame(Block origin, boolean withEnderEye) {
		for (BlockFace face : BlockFaces.NESW) {
			Block frame = origin.translate(face, 2);
			BlockFace facing = face.getOpposite();
			if (!isEndFrame(frame, facing, withEnderEye)) {
				return false;
			}
			if (!isEndFrame(frame.translate(BlockFaces.NESW.previous(face)), facing, withEnderEye)) {
				return false;
			}
			if (!isEndFrame(frame.translate(BlockFaces.NESW.next(face)), facing, withEnderEye)) {
				return false;
			}
		}
		return true;
	}

	private void setRandomActive(Block origin, float chance) {
		// Randomly set the ender eyes
		final Random random = this.getRandom();
		boolean allactive = true;
		int dx, dz;
		boolean active;
		for (dx = -2; dx <= 2; dx++) {
			for (dz = -2; dz <= 2; dz++) {
				// Ignore corner pieces
				if (Math.abs(dx) == 2 && Math.abs(dz) == 2) {
					continue;
				}
				allactive |= (active = random.nextFloat() <= chance);
				VanillaMaterials.END_PORTAL_FRAME.setEyeOfTheEnder(origin.translate(dx, 0, dz), active);
			}
		}
		setPortalActive(origin, allactive);
	}

	private void setEyesActive(Block origin, boolean active) {
		// Set ender eyes
		int dx, dz;
		for (dx = -2; dx <= 2; dx++) {
			for (dz = -2; dz <= 2; dz++) {
				// Ignore corner pieces
				if (Math.abs(dx) == 2 && Math.abs(dz) == 2) {
					continue;
				}
				VanillaMaterials.END_PORTAL_FRAME.setEyeOfTheEnder(origin.translate(dx, 0, dz), active);
			}
		}
	}

	private void setPortalActive(Block origin, boolean active) {
		BlockMaterial material;
		if (active) {
			material = VanillaMaterials.END_PORTAL;
		} else {
			material = VanillaMaterials.AIR;
		}

		// Set the portal blocks
		int dx, dz;
		for (dx = -1; dx <= 1; dx++) {
			for (dz = -1; dz <= 1; dz++) {
				origin.translate(dx, 0, dz).setMaterial(material);
			}
		}
	}

	public void setActive(World w, int x, int y, int z, boolean active) {
		// No portal found
		Block origin = getOrigin(w.getBlock(x, y, z));
		if (origin != null && findFrame(origin, false)) {
			setEyesActive(origin, active);
			setPortalActive(origin, active);
		}
	}

	public boolean isActive(World w, int x, int y, int z) {
		Block origin = getOrigin(w.getBlock(x, y, z));
		return origin != null && findFrame(origin, true);
	}

	@Override
	public void placeObject(World w, int x, int y, int z) {
		Block origin = w.getBlock(x, y, z);

		// Generate the frames
		for (BlockFace face : BlockFaces.NESW) {
			Block frame = origin.translate(face, 2);
			BlockFace facing = face.getOpposite();
			// Place the three pieces
			placeFrame(frame, facing);
			placeFrame(frame.translate(BlockFaces.NESW.previous(face)), facing);
			placeFrame(frame.translate(BlockFaces.NESW.next(face)), facing);
		}

		// Set to a random state
		setRandomActive(origin, 0.1f);
	}

	@Override
	public boolean canPlaceObject(World w, final int x, final int y, final int z) {
		for (int yy = y; yy < y + 4; yy++) {
			for (int xx = x - 3; xx < x + 3; xx++) {
				for (int zz = z - 3; zz < z + 3; zz++) {
					if (!w.getBlock(x, y, z).isMaterial(VanillaMaterials.AIR)) {
						return false;
					}
				}
			}
		}
		return true;
	}
}
