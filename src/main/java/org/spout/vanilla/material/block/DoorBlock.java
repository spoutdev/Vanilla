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
package org.spout.vanilla.material.block;

import org.spout.api.collision.CollisionStrategy;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;

import org.spout.vanilla.data.effect.store.GeneralEffects;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.RedstoneUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public abstract class DoorBlock extends GroundAttachable implements Openable, RedstoneTarget {
	public DoorBlock(String name, int id) {
		super(name, id);
		this.setCollision(CollisionStrategy.SOLID);
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (!(oldMaterial instanceof DoorBlock) && block.getMaterial().equals(this)) {
			boolean powered = this.isReceivingPower(block);
			if (powered != this.isOpen(block)) {
				this.setOpen(block, powered);
				GeneralEffects.DOOR.playGlobal(block.getPosition(), powered);
			}
		}
	}

	@Override
	public boolean canSeekAttachedAlternative() {
		return false;
	}

	@Override
	public void onDestroy(Block block) {
		Block top = getCorrectHalf(block, true);
		Block bottom = getCorrectHalf(block, false);
		top.setMaterial(VanillaMaterials.AIR);
		bottom.setMaterial(VanillaMaterials.AIR);
	}

	@Override
	public boolean isReceivingPower(Block block) {
		block = this.getCorrectHalf(block, false);
		return RedstoneUtil.isReceivingPower(block) || RedstoneUtil.isReceivingPower(block.translate(BlockFace.TOP));
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return face == BlockFace.TOP && material.equals(this);
	}

	/**
	 * Gets the top or face door block when either of the blocks is given
	 * @param doorBlock the top or bottom door block
	 * @param top whether to get the top block, if false, gets the bottom block
	 * @return the requested door half block
	 */
	private Block getCorrectHalf(Block doorBlock, boolean top) {
		if (doorBlock.isDataBitSet(0x8)) {
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
			doorBlock.setMaterial(this, top ? 0x8 : 0x0);
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
		return getCorrectHalf(doorHalf, false).isDataBitSet(0x4);
	}

	@Override
	public void setOpen(Block doorHalf, boolean opened) {
		doorHalf = getCorrectHalf(doorHalf, false);
		doorHalf.setDataBits(0x4, opened);
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
		int bottomData = opened ? 0x4 : 0x0;
		bottomData += BlockFaces.NESW.indexOf(facing, 0);
		bottomHalf.setMaterial(this, bottomData);
	}

	private boolean isDoorBlock(Block bottomBlock) {
		return bottomBlock.getMaterial().equals(this) || bottomBlock.translate(BlockFace.TOP).getMaterial().equals(this);
	}

	private boolean isHingeBlock(Block bottomBlock) {
		return RedstoneUtil.isConductor(bottomBlock) || RedstoneUtil.isConductor(bottomBlock.translate(BlockFace.TOP));
	}

	@Override
	public boolean onPlacement(Block block, short data, BlockFace face, Vector3 clickedPos, boolean isClicked) {
		BlockFace facing = VanillaPlayerUtil.getFacing(block.getSource()).getOpposite();
		Block above = block.translate(BlockFace.TOP);
		if (!above.getMaterial().isPlacementObstacle()) {
			Block left = block.translate(BlockFaces.NESW.previous(facing, 1));
			Block right = block.translate(BlockFaces.NESW.next(facing, 1));
			boolean hingeLeft = isDoorBlock(right) || (!isDoorBlock(left) && !isHingeBlock(right) && isHingeBlock(left));
			create(block, above, facing, hingeLeft, false);
			return true;
		}
		return false;
	}
}
