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
package org.spout.vanilla.material.block.component;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;

import org.spout.physics.collision.shape.BoxShape;

import org.spout.vanilla.component.block.material.Hopper;
import org.spout.vanilla.data.MoveReaction;
import org.spout.vanilla.data.resources.VanillaMaterialModels;
import org.spout.vanilla.data.tool.ToolLevel;
import org.spout.vanilla.data.tool.ToolType;
import org.spout.vanilla.material.VanillaBlockMaterial;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.RedstoneUtil;

public class HopperBlock extends VanillaBlockMaterial implements RedstoneTarget {
	public HopperBlock(String name, int id) {
		super(name, id, VanillaMaterialModels.HOPPER, new BoxShape(1, 1, 1), Hopper.class);
		this.setHardness(3.5F).setResistance(5.8F).addMiningType(ToolType.PICKAXE).setMiningLevel(ToolLevel.WOOD);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public void onUpdate(BlockMaterial oldMaterial, Block block) {
		super.onUpdate(oldMaterial, block);
		Hopper hopper = block.get(Hopper.class);
		hopper.setPowered(this.isReceivingPower(block));
	}

	@Override
	public MoveReaction getMoveReaction(Block block) {
		return MoveReaction.DENY;
	}

	@Override
	public boolean isPlacementSuppressed() {
		return true;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block);
	}

	/**
	 * Dumps an item from the hopper into a block below.
	 */
	public void dropItem(Block block, ItemStack item) {
		if (isReceivingPower(block)) {
			return;
		}
	}

	/**
	 * Pulls an item into the hopper from the block above.
	 */
	public void pullItem(Block block, ItemStack item) {
		if (isReceivingPower(block)) {
			return;
		}
	}
}
