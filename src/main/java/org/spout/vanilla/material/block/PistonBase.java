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
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.LogicUtil;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.util.RedstoneUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public abstract class PistonBase extends VanillaBlockMaterial implements Directional, RedstoneTarget {
	public static final BlockFaces BTEWNS = new BlockFaces(BlockFace.BOTTOM, BlockFace.TOP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);

	public PistonBase(String name, int id) {
		super(name, id);
	}

	@Override
	public void loadProperties() {
		super.loadProperties();
		this.setResistance(0.8F);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		boolean powered = this.isReceivingPower(block);
		if (powered != this.isExtended(block)) {
			this.setExtended(block, powered);
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block, false);
	}

	/**
	 * Extends or retracts this block, complete with the animation and block changes.
	 * 
	 * @param block of the piston
	 * @param extended True to extend, False to retract
	 */
	public abstract void setExtended(Block block, boolean extended);

	/**
	 * Gets whether this piston block is extended
	 * @param block to get it of
	 * @return True if extended, False if not
	 */
	public boolean isExtended(Block block) {
		return LogicUtil.getBit(block.getData(), 0x8);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BTEWNS.get(block.getData());
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setData(BTEWNS.indexOf(facing, 1));
	}

	public BlockFace getPlacedFacing(Block pistonBlock, Entity entity) {
		Vector3 diff = pistonBlock.getPosition().subtract(entity.getPosition());
		diff = diff.subtract(0.0f, 0.2f, 0.0f);
		Vector3 diffabs = diff.abs();
        if (diffabs.getX() < 2.0f && diffabs.getZ() < 2.0f) {
        	if (diff.getY() < 0.0f) {
        		return BlockFace.TOP;
        	} else if (diff.getY() > 2.0f) {
        		return BlockFace.BOTTOM;
        	}
        }
        return VanillaPlayerUtil.getFacing(entity).getOpposite();
    }

	@Override
	public boolean onPlacement(Block block, short data, BlockFace against, boolean isClickedBlock) {
		if (super.onPlacement(block, data, against, isClickedBlock)) {
			BlockFace facing = BlockFace.TOP;
			if (block.getSource() instanceof Entity) {
				facing = this.getPlacedFacing(block, (Entity) block.getSource());
			}
			this.setFacing(block, facing);
			return true;
		}
		return false;
	}
}
