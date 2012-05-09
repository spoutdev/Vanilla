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

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.MathHelper;

public class DoorBlock extends GroundAttachable {
	public static final short HINGE_NORTH_WEST = 0x0;
	public static final short HINGE_NORTH_EAST = 0x1;
	public static final short HINGE_SOUTH_EAST = 0x2;
	public static final short HINGE_SOUTH_WEST = 0x3;

	public DoorBlock(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		if (face == BlockFace.TOP) {
			return material.equals(this);
		} else {
			return false;
		}
	}

	/**
	 * Gets the top or face door block when either of the blocks is given
	 * @param doorBlock the top or bottom door block
	 * @param top whether to get the top block, if false, gets the bottom block
	 * @return the requested door half block
	 */
	private Block getCorrectHalf(Block doorBlock, boolean top) {
		if ((doorBlock.getData() & 0x8) == 0x8) {
			if (!top) {
				doorBlock = doorBlock.translate(BlockFace.BOTTOM);
			}
		} else {
			if (top) {
				doorBlock = doorBlock.translate(BlockFace.TOP);
			}
		}
		if (!doorBlock.getMaterial().equals(this)) {
			//create default door block to 'fix' things up
			doorBlock.setMaterial(this, top ? (short) 8 : (short) 0);
		}
		return doorBlock;
	}

	public boolean isHingeLeft(Block doorHalf) {
		short data = getCorrectHalf(doorHalf, true).getData();
		return (data & 0x1) == 0x1;
	}

	public void setHinge(Block doorHalf, boolean left) {
		doorHalf = getCorrectHalf(doorHalf, true);
		short data = doorHalf.getData();
		if (left) {
			data |= 0x1;
		} else {
			data &= ~0x1;
		}
		doorHalf.setData(data);
	}

	public boolean isOpened(Block doorHalf) {
		short data = getCorrectHalf(doorHalf, false).getData();
		return (data & 0x4) == 0x4;
	}

	public void setOpened(Block doorHalf, boolean opened) {
		doorHalf = getCorrectHalf(doorHalf, false);
		short data = doorHalf.getData();
		if (opened) {
			data |= 0x4;
		} else {
			data &= ~0x4;
		}
		doorHalf.setData(data);
	}

	public void toggleOpened(Block doorHalf) {
		doorHalf = getCorrectHalf(doorHalf, false);
		short data = doorHalf.getData();
		if ((data & 0x4) == 0x4) {
			data &= ~0x4;
		} else {
			data |= 0x4;
		}
		doorHalf.setData(data);
	}

	public BlockFace getFacing(Block doorHalf) {
		short data = getCorrectHalf(doorHalf, false).getData();
		return BlockFaces.NESW.get(data & ~0x4);
	}

	public void setAll(Block bottomHalf, Block topHalf, BlockFace facing, boolean hingeLeft, boolean opened) {
		topHalf.setMaterial(this, hingeLeft ? (short) 9 : (short) 8);
		short bottomData = opened ? (short) 0x4 : (short) 0x0;
		bottomData += BlockFaces.NESW.indexOf(facing, 0);
		bottomHalf.setMaterial(this, bottomData);

		/*

    The bottom two bits determine which direction the door faces (these directions given for which direction the door faces while closed)

        0: Facing west
        1: Facing north
        2: Facing east
        3: Facing south 


		 */
	}

	//TODO: Place in math helper or maybe even Quaternion?
	public static BlockFace getYawFace(float yaw) {
		yaw = MathHelper.wrapAngle(yaw);
		//Faster method to get the face if applicable
		switch ((int) yaw) {
		case -90 : return BlockFace.NORTH;
		case 0 : return BlockFace.WEST;
		case 90 : return BlockFace.SOUTH;
		case 180 : return BlockFace.EAST;
		}
		//Let's apply angle differences
		if (yaw >= -135 && yaw < -45) {
			return BlockFace.NORTH;
		} else if (yaw >= -45 && yaw < 45) {
			return BlockFace.WEST;
		} else if (yaw >= 45 && yaw < 135) {
			return BlockFace.SOUTH;
		} else {
			return BlockFace.EAST;
		}
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace face, boolean isClicked) {
		BlockFace facing = BlockFace.NORTH;
		if (block.getSource() instanceof Entity) {
			facing = getYawFace(((Entity) block.getSource()).getYaw());
		}

		setAll(block, block.translate(BlockFace.TOP), facing.getOpposite(), false, false);

		return true;
		//setAll(block, block.translate(BlockFace.TOP), BlockFace.NORTH, true, false);
		//return true;
	}
}
