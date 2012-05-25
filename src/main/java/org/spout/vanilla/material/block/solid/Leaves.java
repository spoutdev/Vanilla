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
import java.util.Random;

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;
import org.spout.api.material.BlockMaterial;
import org.spout.api.material.block.BlockFace;

import org.spout.vanilla.enchantment.Enchantments;
import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.VanillaMaterials;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.controlled.SignBase;
import org.spout.vanilla.material.item.misc.Shears;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.EnchantmentUtil;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class Leaves extends Solid implements Mineable {
	public static final Leaves DEFAULT = new Leaves("Leaves");
	public static final Leaves SPRUCE = new Leaves("Spruce Leaves", 0, DEFAULT);
	public static final Leaves BIRCH = new Leaves("Birch Leaves", 0, DEFAULT);
	public static final Leaves JUNGLE = new Leaves("Jungle Leaves", 0, DEFAULT);
	private Random rand = new Random();

	private Leaves(String name) {
		super(name, 18);
	}

	private Leaves(String name, int data, Leaves parent) {
		super(name, 18, data, parent);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(0.2F).setResistance(0.3F).setOpacity((byte) 1);
	}

	@Override
	public boolean canSupport(BlockMaterial mat, BlockFace face) {
		return mat.equals(VanillaMaterials.VINES) || mat instanceof SignBase;
	}

	@Override
	public boolean isRedstoneConductor() {
		return false;
	}

	@Override
	public boolean canBurn() {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack held = VanillaPlayerUtil.getCurrentItem(block.getSource());
		if (held != null && (held.getMaterial().equals(VanillaMaterials.SHEARS) || EnchantmentUtil.hasEnchantment(held, Enchantments.SILK_TOUCH))) {
			drops.add(new ItemStack(this, 1));
		} else {
			if (rand.nextInt(20) == 0) {
				drops.add(new ItemStack(VanillaMaterials.SAPLING, 1));
			} else if (rand.nextInt(200) == 0) {
				drops.add(new ItemStack(VanillaMaterials.RED_APPLE, 1));
			}
		}
		return drops;
	}

	// TODO: Decay

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Shears ? (short) 1 : (short) 2;
	}
}
