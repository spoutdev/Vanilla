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
package org.spout.vanilla.material.block.redstone;

import java.util.ArrayList;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.cuboid.Region;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.DynamicMaterial;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.range.EffectRange;
import org.spout.api.material.range.ListEffectRange;
import org.spout.api.math.IntVector3;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.misc.Torch;
import org.spout.vanilla.util.RedstonePowerMode;
import org.spout.vanilla.util.RedstoneUtil;

public class RedstoneTorch extends Torch implements RedstoneSource, RedstoneTarget, DynamicMaterial {
	public static final int TICK_DELAY = 2;
	private static final EffectRange physicsRange = new ListEffectRange(
			new ListEffectRange(IntVector3.createList(0, 2, 0)),
			EffectRange.THIS_AND_NEIGHBORS);
	private boolean powered;

	public RedstoneTorch(String name, int id, boolean powered) {
		super(name, id);
		this.powered = powered;
	}

	@Override
	public void onDestroy(Block block, double dropChance) {
		super.onDestroy(block, dropChance);
	}

	@Override
	public byte getLightLevel(short data) {
		return powered ? (byte) 7 : (byte) 0;
	}

	@Override
	public void handlePlacement(Block block, short data, BlockFace against) {
		block.setMaterial(VanillaMaterials.REDSTONE_TORCH_ON);
		this.setAttachedFace(block, against);
	}

	public boolean isPowered() {
		return this.powered;
	}

	public void setPowered(Block block, boolean powered) {
		block.setMaterial(powered ? VanillaMaterials.REDSTONE_TORCH_ON : VanillaMaterials.REDSTONE_TORCH_OFF, block.getData());
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		boolean receiving = this.isReceivingPower(block);
		if (this.isPowered() == receiving) {
			block.dynamicUpdate(block.getWorld().getAge() + TICK_DELAY);
		}
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isPowered(this.getBlockAttachedTo(block));
	}

	@Override
	public short getRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.hasRedstonePower(block, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePower(Block block, RedstonePowerMode powerMode) {
		return this.isPowered();
	}

	@Override
	public short getRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.hasRedstonePowerTo(block, direction, powerMode) ? REDSTONE_POWER_MAX : REDSTONE_POWER_MIN;
	}

	@Override
	public boolean hasRedstonePowerTo(Block block, BlockFace direction, RedstonePowerMode powerMode) {
		return this.isPowered() && direction == BlockFace.TOP;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaMaterials.REDSTONE_TORCH_ON, 1));
		return drops;
	}

	@Override
	public void onPlacement(Block b, Region r, long currentTime) {
		b.dynamicUpdate(currentTime + TICK_DELAY);
	}

	@Override
	public void onDynamicUpdate(Block block, Region r, long updateTime, long lastUpdateTime, int data, Object hint) {
		boolean receiving = this.isReceivingPower(block);
		if (this.isPowered() == receiving) {
			this.setPowered(block, !receiving);
		}
	}

	@Override
	public EffectRange getDynamicRange() {
		return EffectRange.THIS;
	}
	
	@Override
	public EffectRange getPhysicsRange(short data) {
		return physicsRange;
	}

}
