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
package org.spout.vanilla.material.block.rail;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.material.BlockMaterial;

import org.spout.vanilla.data.RailsState;
import org.spout.vanilla.material.block.redstone.RedstoneSource;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.RedstoneUtil;

public class Rail extends RailBase implements RedstoneTarget {
	public Rail(String name, int id) {
		super(name, id);
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		if (oldMaterial instanceof RedstoneSource && block.getMaterial().equals(this)) {
			this.doTrackLogic(block);
		}
	}

	@Override
	public boolean canCurve() {
		return true;
	}

	@Override
	public void setState(Block block, RailsState state) {
		block.setData(state.getData());
	}

	@Override
	public RailsState getState(Block block) {
		return RailsState.get(block.getBlockData());
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}
}
