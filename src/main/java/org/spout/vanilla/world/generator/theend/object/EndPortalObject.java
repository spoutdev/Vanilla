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
package org.spout.vanilla.world.generator.theend.object;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;

public class EndPortalObject extends WorldGeneratorObject {
	private static boolean isEndFrame(Block block, BlockFace facing, boolean withEnderEye) {
		return (block.isMaterial(VanillaMaterials.END_PORTAL_FRAME) &&
				VanillaMaterials.END_PORTAL_FRAME.getFacing(block) == facing) &&
				(!withEnderEye || VanillaMaterials.END_PORTAL_FRAME.hasEyeOfTheEnder(block));
	}

	private static void placeFrame(Block block, BlockFace facing) {
		block.setMaterial(VanillaMaterials.END_PORTAL_FRAME);
		VanillaMaterials.END_PORTAL_FRAME.setFacing(block, facing);
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
		BlockFace lookDirection = BlockFaces.NESW.next(facing, -1);
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
			if (!isEndFrame(frame.translate(BlockFaces.NESW.previous(face, 1)), facing, withEnderEye)) {
				return false;
			}
			if (!isEndFrame(frame.translate(BlockFaces.NESW.next(face, 1)), facing, withEnderEye)) {
				return false;
			}
		}
		return true;
	}

	private void setActive(Block origin, boolean active) {
		BlockMaterial material;
		if (active) {
			material = VanillaMaterials.END_PORTAL;
		} else {
			material = VanillaMaterials.AIR;
		}

		// Activate or deactivate the portal
		int dx, dz;
		// Set the portal blocks
		for (dx = -1; dx <= 1; dx++) {
			for (dz = -1; dz <= 1; dz++) {
				origin.translate(dx, 0, dz).setMaterial(material);
			}
		}
		// Set ender eyes
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

	public void setActive(World w, int x, int y, int z, boolean active) {
		// No portal found
		Block origin = getOrigin(w.getBlock(x, y, z));
		if (origin != null && findFrame(origin, false)) {
			setActive(origin, active);
		}
	}

	public boolean isActive(World w, int x, int y, int z) {
		Block origin = getOrigin(w.getBlock(x, y, z));
		return origin != null && findFrame(origin, true);
	}

	public void placeObject(World w, int x, int y, int z, boolean active) {
		Block origin = w.getBlock(x, y, z);

		// Generate the frames
		for (BlockFace face : BlockFaces.NESW) {
			Block frame = origin.translate(face, 2);
			BlockFace facing = face.getOpposite();
			// Place the three pieces
			placeFrame(frame, facing);
			placeFrame(frame.translate(BlockFaces.NESW.previous(face, 1)), facing);
			placeFrame(frame.translate(BlockFaces.NESW.next(face, 1)), facing);
		}

		// Set the state of the portal
		setActive(origin, active);
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

	@Override
	public void placeObject(World w, int x, int y, int z) {
		placeObject(w, x, y, z, false);
	}
}
