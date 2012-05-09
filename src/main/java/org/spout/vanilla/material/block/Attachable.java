/*
 * This file is part of Vanilla.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spout.vanilla.material.block;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

public interface Attachable {
	/**
	 * Whether this material seeks an alternative attachment block upon placement
	 */
	public boolean canSeekAttachedAlternative();

	/**
	 * Checks if this material can be attached to certain face of a block material
	 * @param material to attach to
	 * @param face     of the material to attach to
	 * @return if this material can be attached to face of the block material given
	 */
	public boolean canAttachTo(BlockMaterial material, BlockFace face);

	/**
	 * Checks if this material can be attached to certain face of a block
	 * @param block to attach to
	 * @param face  of the block to attach to
	 * @return if this material can be attached to face of the block given
	 */
	public boolean canAttachTo(Block block, BlockFace face);

	/**
	 * Sets the face the block is attached to
	 * @param block        to set
	 * @param attachedFace to set the block to
	 */
	public void setAttachedFace(Block block, BlockFace attachedFace);

	/**
	 * Gets the face the block is attached to
	 * @param block to get it of
	 * @return to which face the block is attached to
	 */
	public BlockFace getAttachedFace(Block block);

	/**
	 * Returns the block that this attachable is attached to
	 * @param block of this attachable
	 * @return the block
	 */
	public Block getBlockAttachedTo(Block block);

	/**
	 * Finds out what face this attachable can properly attach to<br>
	 * The north-east-south-west-bottom-top search pattern is used.
	 * @param block of the attachable
	 * @return the attached face, or null if not found
	 */
	public BlockFace findAttachedFace(Block block);

	/**
	 * Performs placement of this attachable
	 * @param block        to place at
	 * @param data         to use
	 * @param attachedFace to use
	 */
	public void handlePlacement(Block block, short data, BlockFace attachedFace);

	/**
	 * Checks if this attachable is at a position it can actually be<br>
	 * This is called in the underlying physics function to check if the block has to be broken<br>
	 * No checks on the block itself should be performed other than the face it is attached to
	 * 
	 * @param block           to place at
	 * @param data            to use
	 * @param attachedFace    to use
	 * @param seekAlternative whether an alternative attached face should be sought
	 * @return whether placement is possible
	 */
	public boolean isValidPosition(Block block, BlockFace attachedFace, boolean seekAlternative);
}
