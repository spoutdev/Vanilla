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
package org.spout.vanilla.material.block.plant;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.Material;
import org.spout.api.material.block.BlockFace;
import org.spout.api.material.block.BlockFaces;

import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.attachable.GroundAttachable;

public class SugarCaneBlock extends GroundAttachable {
	private final Set<Material> allowedBases = new HashSet<Material>(4);

	public SugarCaneBlock(String name, int id) {
		super(name, id);

		this.addAllowedBase(VanillaMaterials.DIRT, VanillaMaterials.GRASS, VanillaMaterials.SAND, VanillaMaterials.SUGAR_CANE_BLOCK);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.0F).setResistance(0.0F);
	}

	@Override
	public boolean canAttachTo(BlockMaterial material, BlockFace face) {
		return super.canAttachTo(material, face) && this.allowedBases.contains(material);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return face == BlockFace.TOP && material.equals(VanillaMaterials.SUGAR_CANE_BLOCK);
	}

	@Override
	public boolean canAttachTo(Block block, BlockFace face) {
		if (super.canAttachTo(block, face)) {
			BlockMaterial wmat;
			for (BlockFace around : BlockFaces.NESW) {
				wmat = block.translate(around).getMaterial();
				if (wmat.equals(VanillaMaterials.STATIONARY_WATER, VanillaMaterials.WATER)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this, 1));
		return drops;
	}

	public void addAllowedBase(Material... materials) {
		for (Material mat : materials) {
			allowedBases.add(mat);
		}
	}
}
