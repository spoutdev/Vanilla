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
package org.spout.vanilla.material.block.misc;

import java.util.ArrayList;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;
import org.spout.vanilla.material.item.MiningTool;
import org.spout.vanilla.material.item.tool.Spade;

public class Snow extends GroundAttachable implements Mineable {
	public Snow(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.1F).setResistance(0.2F).setOpacity((byte) 0);
	}

	@Override
	public boolean hasPhysics() {
		return true;
	}

	@Override
	public boolean isPlacementObstacle() {
		return false;
	}

	@Override
	public short getDurabilityPenalty(MiningTool tool) {
		return tool instanceof Spade ? (short) 1 : (short) 2;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (block.getSource() instanceof Entity) {
			if (((Entity) block.getSource()).getInventory().getCurrentItem().getMaterial() instanceof Spade) {
				drops.add(new ItemStack(VanillaMaterials.SNOWBALL, block.getData(), 1));
			}
		}
		return drops;
	}

	@Override
	public void onUpdate(Block block) {
		BlockMaterial below = block.translate(BlockFace.BOTTOM).getMaterial();
		if (below.getMaterial() == VanillaMaterials.AIR) {
			block.setMaterial(VanillaMaterials.AIR).update(true);
		}
	}
}
