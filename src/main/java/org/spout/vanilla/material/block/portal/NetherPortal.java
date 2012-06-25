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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Portal;
import org.spout.vanilla.util.MoveReaction;

public class NetherPortal extends Portal {
	public NetherPortal(String name, int id) {
		super(name, id);
		this.setHardness(-1.0F).setResistance(0.0F).setOpacity((byte) 1);
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
	 * Tries to create a portal model at the location given
	 * @param bottomBlock of the portal (is Obsidian)
	 * @return True if it was successful, False if not
	 */
	public boolean createPortal(Block bottomBlock) {
		Block above = bottomBlock.translate(BlockFace.TOP);
		// Find out what direction to create the portal to
		BlockFace direction = null;
		for (BlockFace face : BlockFaces.NESW) {
			if (above.translate(face).isMaterial(VanillaMaterials.OBSIDIAN)) {
				if (direction != null) {
					// Two corners? Abort
					return false;
				} else {
					direction = face.getOpposite();
				}
			}
		}
		if (direction == null) {
			// Failed to find
			return false;
		}
		// Validate the floor
		if (!bottomBlock.isMaterial(VanillaMaterials.OBSIDIAN)) {
			return false;
		}
		if (!bottomBlock.translate(direction).isMaterial(VanillaMaterials.OBSIDIAN)) {
			return false;
		}
		// Validate the 3-height columns
		Block corner1 = above.translate(direction.getOpposite());
		Block corner2 = corner1.translate(direction, 3);
		for (int i = 0; i < 3; i++) {
			if (!corner1.isMaterial(VanillaMaterials.OBSIDIAN) || !corner2.isMaterial(VanillaMaterials.OBSIDIAN)) {
				return false;
			}
			corner1 = corner1.translate(BlockFace.TOP);
			corner2 = corner2.translate(BlockFace.TOP);
		}
		// Validate the roof
		corner1 = bottomBlock.translate(BlockFace.TOP, 4);
		corner2 = corner1.translate(direction);
		if (!corner1.isMaterial(VanillaMaterials.OBSIDIAN)) {
			return false;
		}
		if (!corner2.isMaterial(VanillaMaterials.OBSIDIAN)) {
			return false;
		}
		// Validated, time to create the portal
		corner1 = above;
		corner2 = above.translate(direction);
		for (int i = 0; i < 3; i++) {
			corner1.setMaterial(this);
			corner2.setMaterial(this);
			corner1 = corner1.translate(BlockFace.TOP);
			corner2 = corner2.translate(BlockFace.TOP);
		}
		return true;
	}
}
