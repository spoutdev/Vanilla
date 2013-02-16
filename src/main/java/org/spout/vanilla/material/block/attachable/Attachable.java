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
package org.spout.vanilla.material.block.attachable;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;

public interface Attachable {
	/**
	 * Checks if this material can be attached to certain face of a block material
	 * @param block to attach to
	 * @param face of the material to attach to
	 * @return if this material can be attached to face of the block material given
	 */
	public boolean canAttachTo(Block block, BlockFace face);

	/**
	 * Sets the face the block is attached to
	 * @param block to set
	 * @param attachedFace to set the block to
	 * @param cause of the attachment
	 */
	public void setAttachedFace(Block block, BlockFace attachedFace, Cause<?> cause);

	/**
	 * Gets the face the block is attached to
	 * @param block to get it of
	 * @return to which face the block is attached to
	 */
	public BlockFace getAttachedFace(Block block);

	/**
	 * Gets the face the block is attached to
	 * @param data of the block
	 * @return to which face the block is attached to
	 */
	public BlockFace getAttachedFace(short data);

	/**
	 * Returns the block that this attachable is attached to
	 * @param block of this attachable
	 * @return the block
	 */
	public Block getBlockAttachedTo(Block block);
}
