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
package org.spout.vanilla.material.block.rail;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.util.LogicUtil;
import org.spout.vanilla.material.block.RailsBase;
import org.spout.vanilla.util.RailsState;

public class PoweredRails extends RailsBase {
	public PoweredRails() {
		super("Powered Rail", 27);
	}

	@Override
	public boolean canCurve() {
		return false;
	}

	/**
	 * Gets if this block is powered
	 *
	 * @param block to get it of
	 * @return True if powered, False if not
	 */
	public boolean isPowered(Block block) {
		return LogicUtil.getBit(block.getData(), 0x8);
	}

	/**
	 * Sets if this block is powered
	 *
	 * @param block   to set it of
	 * @param powered Whether to power the block or not
	 */
	public void setPowered(Block block, boolean powered) {
		block.setData(LogicUtil.setBit(block.getData(), 0x8, powered));
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
