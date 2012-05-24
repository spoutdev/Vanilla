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
package org.spout.vanilla.material.block.ore;

import java.util.ArrayList;
import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.material.Ore;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.controlled.SignBase;
import org.spout.vanilla.material.item.tool.MiningTool;
import org.spout.vanilla.util.EnchantmentUtil;

public class Glowstone extends Ore {
	public Glowstone(String name, int id) {
		super(name, id);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.3F).setResistance(0.5F).setLightLevel(15).setOpacity((byte) 0);
	}

	@Override
	public boolean canSupport(BlockMaterial material, BlockFace face) {
		return material instanceof SignBase || material.equals(VanillaMaterials.REDSTONE_WIRE);
	}

	@Override
	public boolean isRedstoneConductor() {
		return false;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (block.getSource() instanceof Entity) {
			ItemStack held = ((Entity) block.getSource()).getInventory().getCurrentItem();
			if (held != null && held.getMaterial() instanceof MiningTool) {
				if (EnchantmentUtil.hasEnchantment(held, Enchantments.SILK_TOUCH)) {
					drops.add(new ItemStack(this, 1));
				} else {
					drops.add(new ItemStack(VanillaMaterials.GLOWSTONE_DUST, new Random().nextInt(3) + 2));
				}
			}
		}
		return drops;
	}
}
