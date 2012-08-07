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
package org.spout.vanilla.material.block.rail;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;

import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.util.RailsState;
import org.spout.vanilla.util.RedstonePowerMode;

public class DetectorRail extends RailBase implements RedstoneSource, DynamicMaterial {
	public static final int TICK_DELAY = 1000;

	public DetectorRail(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean canCurve() {
		return false;
	}

	public void activate(Block block) {
		this.setPowering(block, true);
		block.dynamicUpdate(block.getWorld().getAge() + TICK_DELAY);
	}

	/**
	 * Gets if this block is supplying power
	 * @param block to get it of
	 * @return True if powered, False if not
	 */
	public boolean isPowering(Block block) {
		return block.isDataBitSet(0x8);
	}

	/**
	 * Sets if this block is supplying power
	 * @param block to set it of
	 * @param powering Whether the block is supplying power
	 */
	public void setPowering(Block block, boolean powering) {
		block.setDataBits(0x8, powering);
	}

	@Override
	public void setState(Block block, RailsState state) {
		if (state.isCurved()) {
			throw new IllegalArgumentException("A detector rail can not curve!");
		}
		short data = (short) (block.getData() & ~0x7);
		data += state.getData();
		block.setData(data);
	}

	@Override
	public RailsState getState(Block block) {
		return RailsState.get(block.getData() & 0x7);
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isPowering(block);
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePowerTo(block, direction, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.isPowering(block) && direction == BlockFace.BOTTOM;
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS_AND_NEIGHBORS;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
	}

	@Override
	public void onDynamicUpdate(Block block, Region r, long updateTime, int data) {
		if (!this.isPowering(block)) {
			block.dynamicUpdate(updateTime + TICK_DELAY);
			return;
		}

		//TODO: Check if a minecart is on top of this block right now...
		this.setPowering(block, false);
	}
}
