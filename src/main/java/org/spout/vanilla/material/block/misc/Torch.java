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

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.AbstractAttachable;
import org.spout.vanilla.material.block.attachable.PointAttachable;

public class Torch extends AbstractAttachable implements PointAttachable {
	public Torch(String name, int id) {
		super(name, id);
		this.setAttachable(BlockFaces.NSEWB);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.0F).setResistance(0.0F).setLightLevel(14);
	}

	@Override
	public void setAttachedFace(Block block, BlockFace attachedFace) {
		block.setData((short) (BlockFaces.NSEWB.indexOf(attachedFace, 4) + 1));
	}

	@Override
	public BlockFace getAttachedFace(Block block) {
		return BlockFaces.NSEWB.get(block.getData() - 1, BlockFace.BOTTOM);
	}

	@Override
	public boolean canSeekAttachedAlternative() {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(VanillaMaterials.TORCH, block.getData(), 1));
		return drops;
	}
}
