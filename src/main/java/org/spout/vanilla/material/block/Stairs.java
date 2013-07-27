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
package org.spout.vanilla.material.block;

import org.spout.api.event.Cause;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;
import org.spout.api.math.Vector3;
import org.spout.api.util.bytebit.ByteBitSet;

import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.util.PlayerUtil;

public abstract class Stairs extends VanillaBlockMaterial implements Directional {
	private final ByteBitSet[] occlusion;

	public Stairs(String name, int id, String model) {
		super(name, id, model);
		this.occlusion = new ByteBitSet[8];
		for (int i = 0; i < 4; i++) {
			this.occlusion[i] = new ByteBitSet(BlockFaces.NSEW.get(i).getOpposite(), BlockFace.BOTTOM);
			this.occlusion[i | 0x4] = new ByteBitSet(BlockFaces.NSEW.get(i).getOpposite(), BlockFace.TOP);
		}
	}

	/**
	 * Gets if this stair is upside down
	 * @param block to get it of
	 * @return True if it is upside down
	 */
	public boolean isUpsideDown(Block block) {
		return block.isDataBitSet(0x4);
	}

	/**
	 * Sets if this stair is upside down
	 * @param block to set it for
	 * @param upsideDown state
	 */
	public void setUpsideDown(Block block, boolean upsideDown) {
		block.setDataBits(0x4, upsideDown);
	}

	@Override
	public BlockFace getFacing(Block block) {
		return BlockFaces.NSEW.get(block.getBlockData() & 0x3);
	}

	@Override
	public void setFacing(Block block, BlockFace facing) {
		block.setDataField(0x3, BlockFaces.NSEW.indexOf(facing, 0));
	}

	@Override
	public void onPlacement(Block block, short data, BlockFace against, Vector3 clickedPos, boolean isClickedBlock, Cause<?> cause) {
		super.onPlacement(block, data, against, clickedPos, isClickedBlock, cause);
		this.setFacing(block, PlayerUtil.getFacing(cause).getOpposite());
		this.setUpsideDown(block, against == BlockFace.TOP || (BlockFaces.NESW.contains(against) && clickedPos.getY() > 0.5f));
	}

	@Override
	public ByteBitSet getOcclusion(short data) {
		return this.occlusion[data & 0x7];
	}
}
