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
package org.spout.vanilla.plugin.material.block.rail;

import org.spout.vanilla.api.material.block.rail.RailBase;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.plugin.data.RailsState;
import org.spout.vanilla.api.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.plugin.util.RedstoneUtil;

public class PoweredRail extends RailBase implements RedstoneTarget {
	public PoweredRail(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean canCurve() {
		return false;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (block.getMaterial().equals(this)) {
			boolean powered = this.isReceivingPower(block);
			if (powered != this.isPowered(block)) {
				this.setPowered(block, powered);
			}
		}
	}

	private boolean isReceivingPower(Block block, BlockFace face) {
		final int maxRange = 8;
		for (int i = 0; i < maxRange; i++) {
			block = block.translate(face);
			if (!block.getMaterial().equals(this) || !this.isConnected(block, face)) {
				return false;
			} else if (RedstoneUtil.isReceivingPower(block)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		if (RedstoneUtil.isReceivingPower(block)) {
			return true;
		}

		BlockFace[] facing = this.getState(block).getDirections();
		return isReceivingPower(block, facing[0]) || isReceivingPower(block, facing[1]);
	}

	/**
	 * Gets if this block is powered
	 * @param block to get it of
	 * @return True if powered, False if not
	 */
	public boolean isPowered(Block block) {
		return block.isDataBitSet(0x8);
	}

	/**
	 * Sets if this block is powered
	 * @param block to set it of
	 * @param powered Whether to power the block or not
	 */
	public void setPowered(Block block, boolean powered) {
		block.setDataBits(0x8, powered);
	}

	@Override
	public void setState(Block block, RailsState state) {
		if (state.isCurved()) {
			throw new IllegalArgumentException("A powered rail can not curve!");
		}
		short data = (short) (block.getData() & ~0x7);
		data += state.getData();
		block.setData(data);
	}

	@Override
	public RailsState getState(Block block) {
		return RailsState.get(block.getData() & 0x7);
	}
}
