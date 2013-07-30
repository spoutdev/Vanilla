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
package org.spout.vanilla.material.block.solid;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.Directional;
import org.spout.vanilla.material.block.Solid;

/**
 * Represents a block that has an upright position, and two horizontal directions.
 */
public class RotationalSolid extends Solid implements Directional {
	private static final BlockFaces DIRECTION_OPPOS = new BlockFaces(BlockFace.BOTTOM, BlockFace.NORTH, BlockFace.EAST);
	private static final BlockFaces DIRECTION_FACES = new BlockFaces(BlockFace.TOP, BlockFace.SOUTH, BlockFace.WEST, BlockFace.THIS);
	private static final byte directionMask = 0xC;

	public RotationalSolid(String name, int id, String model) {
		super(name, id, model);
	}

	public RotationalSolid(short dataMask, String name, int id, String model) {
		super(dataMask, name, id, model);
	}

	public RotationalSolid(String name, int id, int data, VanillaBlockMaterial parent, String model) {
		super(name, id, data, parent, model);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return DIRECTION_FACES.get(block.getDataField(directionMask));
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		if (DIRECTION_OPPOS.contains(facing)) {
			facing = facing.getOpposite();
		}
		block.setDataField(directionMask, DIRECTION_FACES.indexOf(facing, 0));
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, against);
	}
}
