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
import org.spout.api.material.block.BlockFaces;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.protocol.VanillaNetworkSynchronizer;
import org.spout.vanilla.protocol.msg.PlayEffectMessage;
import org.spout.vanilla.util.RedstoneUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class DoorBlock extends GroundAttachable implements Openable, RedstoneTarget {
	public DoorBlock(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (block.getMaterial().equals(this)) {
			boolean powered = this.isReceivingPower(block);
			if (powered != this.isOpen(block)) {
				this.setOpen(block, powered);
				VanillaNetworkSynchronizer.playBlockEffect(block, null, PlayEffectMessage.Messages.RANDOM_DOOR);
			}
		}
	}

	@Override
	public boolean canSeekAttachedAlternative() {
		return false;
	}

	@Override
	public void onDestroyBlock(Block block) {
		Block top = getCorrectHalf(block, true);
		Block bottom = getCorrectHalf(block, false);
		top.setMaterial(VanillaMaterials.AIR).update();
		bottom.setMaterial(VanillaMaterials.AIR).update();
	}

	@Override
	public boolean isReceivingPower(Block block) {
		block = this.getCorrectHalf(block, false);
		return RedstoneUtil.isReceivingPower(block) || RedstoneUtil.isReceivingPower(block.translate(BlockFace.TOP));
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
	 * @param top	   whether to get the top block, if false, gets the bottom block
	 * @return the requested door half block
	 */
	private Block getCorrectHalf(Block doorBlock, boolean top) {
		if (LogicUtil.getBit(doorBlock.getData(), 0x8)) {
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
			doorBlock.setMaterial(this, top ? (short) 0x8 : (short) 0x0);
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

	@Override
	public boolean isOpen(Block doorHalf) {
		return LogicUtil.getBit(getCorrectHalf(doorHalf, false).getData(), 0x4);
	}

	@Override
	public void setOpen(Block doorHalf, boolean opened) {
		doorHalf = getCorrectHalf(doorHalf, false);
		short data = doorHalf.getData();
		short newdata = LogicUtil.setBit(data, 0x4, opened);
		if (data != newdata) {
			doorHalf.setData(newdata);
		}
	}

	@Override
	public void toggleOpen(Block doorHalf) {
		doorHalf = getCorrectHalf(doorHalf, false);
		doorHalf.setData(doorHalf.getData() ^ 0x4);
	}

	public BlockFace getFacing(Block doorHalf) {
		short data = getCorrectHalf(doorHalf, false).getData();
		return BlockFaces.NESW.get(data & ~0x4);
	}

	public void create(Block bottomHalf, Block topHalf, BlockFace facing, boolean hingeLeft, boolean opened) {
		topHalf.setMaterial(this, hingeLeft ? (short) 9 : (short) 8);
		short bottomData = opened ? (short) 0x4 : (short) 0x0;
		bottomData += BlockFaces.NESW.indexOf(facing, 0);
		bottomHalf.setMaterial(this, bottomData);
	}

	//TODO: Place in SpoutAPI
	public static BlockFace rotate(BlockFace from, int notchCount) {
		while (notchCount > 0) {
			switch (from) {
				case NORTH:
					from = BlockFace.EAST;
					break;
				case EAST:
					from = BlockFace.SOUTH;
					break;
				case SOUTH:
					from = BlockFace.WEST;
					break;
				case WEST:
					from = BlockFace.NORTH;
					break;
				default:
					return from;
			}
			if (notchCount-- == 0) {
				return from;
			}
		}
		while (notchCount < 0) {
			switch (from) {
				case NORTH:
					from = BlockFace.WEST;
					break;
				case WEST:
					from = BlockFace.SOUTH;
					break;
				case SOUTH:
					from = BlockFace.EAST;
					break;
				case EAST:
					from = BlockFace.NORTH;
					break;
				default:
					return from;
			}
			if (notchCount++ == 0) {
				return from;
			}
		}
		return from;
	}

	private boolean isDoorBlock(Block bottomBlock) {
		return bottomBlock.getMaterial().equals(this) || bottomBlock.translate(BlockFace.TOP).getMaterial().equals(this);
	}

	private boolean isHingeBlock(Block bottomBlock) {
		return RedstoneUtil.isConductor(bottomBlock) || RedstoneUtil.isConductor(bottomBlock.translate(BlockFace.TOP));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace face, boolean isClicked) {
		BlockFace facing = VanillaPlayerUtil.getFacing(block.getSource()).getOpposite();
		Block above = block.translate(BlockFace.TOP);
		if (!above.getSubMaterial().isPlacementObstacle()) {
			Block left = block.translate(rotate(facing, -1));
			Block right = block.translate(rotate(facing, 1));
			boolean hingeLeft = isDoorBlock(right) || (!isDoorBlock(left) && !isHingeBlock(right) && isHingeBlock(left));
			create(block, above, facing, hingeLeft, false);
			block.update();
			above.update();
			return true;
		}
		return false;
	}
}
