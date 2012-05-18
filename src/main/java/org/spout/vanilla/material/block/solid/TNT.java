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
package org.spout.vanilla.material.block.solid;

import java.util.ArrayList;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.geo.discrete.Point;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.controller.object.moving.PrimedTnt;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.redstone.RedstoneTarget;
import org.spout.vanilla.util.RedstoneUtil;

public class TNT extends Solid implements RedstoneTarget {
	public TNT(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.0F).setResistance(0.0F);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		return false;
	}

	@Override
	public void onIgnite(Block block) {
		block.setMaterial(VanillaMaterials.AIR).update();
		// spawn a primed TNT
		Point point = block.getPosition();
		point.getWorld().createAndSpawnEntity(point, new PrimedTnt());
	}

	@Override
	public void onUpdate(Block block) {
		super.onUpdate(block);
		if (this.isReceivingPower(block)) {
			this.onIgnite(block);
		}
	}

	@Override
	public boolean canBurn() {
		return true;
	}

	@Override
	public boolean isReceivingPower(Block block) {
		return RedstoneUtil.isReceivingPower(block, false);
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}
}
