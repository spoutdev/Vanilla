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
package org.spout.vanilla.world.generator.nether.object;

import java.util.Random;

import org.spout.api.generator.WorldGeneratorObject;
import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class NetherPortalObject extends WorldGeneratorObject {
	public BlockFace getDirection(Point pos) {
		final Block bottomLeftCorner = pos.getWorld().getBlock(pos);
		BlockFace direction = null;
		// Get the direction of the frame on horizontal axis
		for (BlockFace face : BlockFaces.NESW) {
			if (bottomLeftCorner.translate(face).isMaterial(VanillaMaterials.OBSIDIAN)) {
				if (direction != null) {
					// Two many corners
					return null;
				}
				direction = face;
			}
		}

		return direction;
	}

	public boolean find(Point center, int radius) {
		return find(center, radius, radius, radius);
	}

	public boolean find(Point center, int xRadius, int yRadius, int zRadius) {
		final World world = center.getWorld();
		for (int y = center.getBlockY() - xRadius; y < center.getBlockY() + xRadius; y++) {
			for (int x = center.getBlockX() - yRadius; x < center.getBlockX() + yRadius; x++) {
				for (int z = center.getBlockZ() - zRadius; z < center.getBlockZ() + zRadius; z++) {
					if (find(world.getBlock(x, y, z))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean find(Block bottomBlock) {

		final Block bottomLeftCorner = bottomBlock;

		// Frame not there or misshapen frame
		BlockFace direction = getDirection(bottomLeftCorner.getPosition());
		if (direction == null) {
			return false;
		}

		// Define other corners of the frame
		final Block bottomRightCorner = bottomLeftCorner.translate(direction, 3);
		final Block topLeftCorner = bottomLeftCorner.translate(BlockFace.TOP, 4);

		// Verify the vertical columns
		boolean success = true;
		for (int dy = 1; dy < 4; dy++) {
			if (!bottomLeftCorner.translate(0, dy, 0).isMaterial(VanillaMaterials.OBSIDIAN)
					|| !bottomRightCorner.translate(0, dy, 0).isMaterial(VanillaMaterials.OBSIDIAN)) {
				success = false;
				break;
			}
		}

		// Missing pieces from columns
		if (!success) {
			return false;
		}

		// Verify the horizontal columns
		for (int d = 1; d < 3; d++) {
			if (!bottomLeftCorner.translate(direction, d).isMaterial(VanillaMaterials.OBSIDIAN)
					|| !topLeftCorner.translate(direction, d).isMaterial(VanillaMaterials.OBSIDIAN)) {
				success = false;
				break;
			}
		}

		return success;
	}

	private Block getOrigin(Block bottomBlock) {
		Block origin = null;
		for (int d = -2; d < 2; d++) {
			Block x = bottomBlock.translate(d, 0, 0);
			Block z = bottomBlock.translate(0, 0, d);
			if (find(x)) {
				origin = x;
				break;
			} else if (find(z)) {
				origin = z;
				break;
			}
		}
		return origin;
	}

	public void setActive(World w, int x, int y, int z, boolean active) {
		// No portal found
		Block bottomBlock = getOrigin(w.getBlock(x, y, z));
		if (bottomBlock == null || !find(bottomBlock) || w.getGenerator() instanceof TheEndGenerator) {
			return;
		}

		BlockMaterial material;
		if (active) {
			material = VanillaMaterials.PORTAL;
		} else {
			material = VanillaMaterials.AIR;
		}

		// Activate or deactivate the portal
		BlockFace direction = getDirection(bottomBlock.getPosition());
		Block corner1 = bottomBlock.translate(direction).translate(BlockFace.TOP);
		Block corner2 = corner1.translate(direction);
		for (int d = 0; d < 3; d++) {
			corner1.translate(BlockFace.TOP, d).setMaterial(material);
			corner2.translate(BlockFace.TOP, d).setMaterial(material);
		}
	}

	public void placeObject(World w, int x, int y, int z, BlockFace direction, boolean active) {

		final Block bottomLeftBlock = w.getBlock(x, y, z).setMaterial(VanillaMaterials.OBSIDIAN);
		final Block bottomRightBlock = bottomLeftBlock.translate(direction, 3).setMaterial(VanillaMaterials.OBSIDIAN);
		final Block topLeftBlock = bottomLeftBlock.translate(BlockFace.TOP, 4).setMaterial(VanillaMaterials.OBSIDIAN);
		topLeftBlock.translate(direction, 3).setMaterial(VanillaMaterials.OBSIDIAN);

		// Build vertical columns
		for (int dy = 0; dy < 4; dy++) {
			bottomLeftBlock.translate(BlockFace.TOP, dy).setMaterial(VanillaMaterials.OBSIDIAN);
			bottomRightBlock.translate(BlockFace.TOP, dy).setMaterial(VanillaMaterials.OBSIDIAN);
		}

		// Build horizontal columns
		for (int dw = 0; dw < 3; dw++) {
			bottomLeftBlock.translate(direction, dw).setMaterial(VanillaMaterials.OBSIDIAN);
			topLeftBlock.translate(direction, dw).setMaterial(VanillaMaterials.OBSIDIAN);
		}

		// Set the state of the portal
		setActive(w, x, y, z, active);
	}

	public void placeObject(World w, int x, int y, int z, boolean active) {
		placeObject(w, x, y, z, BlockFaces.NESW.random(new Random()), active);
	}

	public void placeObject(World w, int x, int y, int z, BlockFace direction) {
		placeObject(w, x, y, z, direction, false);
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
