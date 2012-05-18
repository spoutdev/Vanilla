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

import java.util.ArrayList;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.util.LogicUtil;

import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.RailsState;
import org.spout.vanilla.util.RedstoneUtil;

public class PoweredRail extends RailBase implements RedstoneTarget {
	public PoweredRail(String name, int id) {
		super(name, id);
	}

	@Override
	public boolean canCurve() {
		return false;
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (block.getMaterial().equals(this)) {
			boolean powered = this.isReceivingPower(block);
			if (powered != this.isPowered(block)) {
				this.setPowered(block, powered);
				block.update();
			} else {
				Block neigh;
				for (BlockFace face : this.getState(block).getDirections()) {
					neigh = block.translate(face);
					if (neigh.getMaterial().equals(this)) {
						if (this.isConnected(neigh, face)) {
							if (this.isReceivingPower(neigh) != this.isPowered(neigh)) {
								neigh.update();
							}
						}
					}
				}
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
		} else {
			BlockFace[] facing = this.getState(block).getDirections();
			return isReceivingPower(block, facing[0]) || isReceivingPower(block, facing[1]);
		}
	}

	/**
	 * Gets if this block is powered
	 * @param block to get it of
	 * @return True if powered, False if not
	 */
	public boolean isPowered(Block block) {
		return LogicUtil.getBit(block.getData(), 0x8);
	}

	/**
	 * Sets if this block is powered
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

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}
}
