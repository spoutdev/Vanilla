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
package org.spout.vanilla.material.block.portal;

import org.spout.api.geo.World;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Portal;
import org.spout.vanilla.world.generator.theend.TheEndGenerator;

public class NetherPortal extends Portal {
	public NetherPortal(String name, int id) {
		super(name, id, (String) null);
		this.setHardness(-1.0F).setResistance(0.0F);
	}

	@Override
	public byte getLightLevel(short data) {
		return 11;
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	/**
	 * Finds a {@link NetherPortalFrame} from the specified center searching through a bounding
	 * box from the center with the specified radius.
	 *
	 * @param center of bounding box
	 * @param radius to search from center
	 * @return portal frame found, null if no portal found
	 */
	public static NetherPortalFrame findFrame(Point center, int radius) {
		return findFrame(center, radius, radius, radius);
	}

	/**
	 * Finds a {@link NetherPortalFrame} from the specified center searching through a bounding
	 * box from the center with the specified radii.
	 *
	 * @param center of bounding box
	 * @param xRadius to search from the center
	 * @param yRadius to search from the center
	 * @param zRadius to search from the center
	 * @return portal frame if found, null if no portal found
	 */
	public static NetherPortalFrame findFrame(Point center, int xRadius, int yRadius, int zRadius) {
		final World world = center.getWorld();
		for (int y = center.getBlockY() - xRadius; y < center.getBlockY() + xRadius; y++) {
			for (int x = center.getBlockX() - yRadius; x < center.getBlockX() + yRadius; x++) {
				for (int z = center.getBlockZ() - zRadius; z < center.getBlockZ() + zRadius; z++) {
					NetherPortalFrame frame = findFrame(world.getBlock(x, y, z));
					if (frame != null) {
						return frame;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Finds a {@link NetherPortalFrame} from the specified {@link Block} which
	 * should always be the bottom left corner of the frame which can be air.
	 *
	 * @param bottomBlock bottom left block of the portal
	 * @return portal frame if found, null if no portal found
	 */
	public static NetherPortalFrame findFrame(Block bottomBlock) {
		return findFrame(bottomBlock, true);
	}

	/**
	 * Finds a {@link NetherPortalFrame} from the specified {@link Block} which
	 * can either be the bottom left corner of the frame or any block on the bottom
	 * of the frame. If using a block other then the bottom left block, this should be
	 * specified with the origin argument.
	 *
	 * @param bottomBlock
	 * @param origin
	 * @return portal frame
	 */
	public static NetherPortalFrame findFrame(Block bottomBlock, boolean origin) {

		// Specified block is not the origin of the frame (bottom left block)
		if (!origin) {
			NetherPortalFrame frame = null;
			for (int dx = 0; dx < 3; dx++) {
				frame = findFrame(bottomBlock.translate(dx, 0, 0), true);
				if (frame != null) {
					break;
				}
			}

			if (frame == null) {
				for (int dz = 0; dz < 3; dz++) {
					frame = findFrame(bottomBlock.translate(0, 0, dz), true);
					if (frame != null) {
						break;
					}
				}
			}

			return frame;
		}

		final Block bottomLeftCorner = bottomBlock;

		// Start with bottom left corner of frame
		if (!bottomLeftCorner.isMaterial(VanillaMaterials.OBSIDIAN)) {
			return null;
		}

		BlockFace direction = null;
		boolean success = true;

		// Get the direction of the frame on horizontal axis
		for (BlockFace face : BlockFaces.NESW) {
			if (bottomLeftCorner.translate(face).isMaterial(VanillaMaterials.OBSIDIAN)) {
				if (direction != null) {
					// Two corners
					success = false;
					break;
				}
				direction = face;
			}
		}

		// Frame not there or misshapen frame
		if (direction == null || !success) {
			return null;
		}

		// Define the other corners of the frame
		final Block bottomRightCorner = bottomLeftCorner.translate(direction, 3);
		final Block topLeftCorner = bottomLeftCorner.translate(BlockFace.TOP, 4);

		// Verify the vertical columns
		for (int dy = 0; dy < 3; dy++) {
			if (!bottomLeftCorner.translate(0, dy, 0).isMaterial(VanillaMaterials.OBSIDIAN)
					|| !bottomRightCorner.translate(0, dy, 0).isMaterial(VanillaMaterials.OBSIDIAN)) {
				success = false;
				break;
			}
		}

		// Missing pieces from columns
		if (!success) {
			return null;
		}

		// Verify the horizontal columns
		for (int d = 0; d < 2; d++) {
			if (!bottomLeftCorner.translate(direction, d).isMaterial(VanillaMaterials.OBSIDIAN)
					|| !topLeftCorner.translate(direction, d).isMaterial(VanillaMaterials.OBSIDIAN)) {
				success = false;
				break;
			}
		}

		// Missing pieces from columns
		if (!success) {
			return null;
		}

		return new NetherPortalFrame(direction, bottomLeftCorner.getPosition());
	}

	/**
	 * Tries to create a portal model at the location given
	 * @param bottomBlock of the portal (is Obsidian)
	 * @return True if it was successful, False if not
	 */
	public static boolean createPortal(Block bottomBlock) {
		NetherPortalFrame frame = findFrame(bottomBlock, false);
		if (frame == null || bottomBlock.getWorld().getGenerator() instanceof TheEndGenerator) {
			return false;
		}

		// Validated, time to create the portal
		Point framePos = frame.getPosition();
		Block corner1 = framePos.getWorld().getBlock(framePos).translate(frame.getDirection()).translate(BlockFace.TOP);
		Block corner2 = corner1.translate(frame.getDirection());
		for (int i = 0; i < 3; i++) {
			corner1.setMaterial(VanillaMaterials.PORTAL);
			corner2.setMaterial(VanillaMaterials.PORTAL);
			corner1 = corner1.translate(BlockFace.TOP);
			corner2 = corner2.translate(BlockFace.TOP);
		}

		return true;
	}

	public static class NetherPortalFrame {
		private final BlockFace direction;
		private final Point pos;

		public NetherPortalFrame(BlockFace direction, Point pos) {
			this.direction = direction;
			this.pos = pos;
		}

		public BlockFace getDirection() {
			return direction;
		}

		public Point getPosition() {
			return pos;
		}
	}
}
