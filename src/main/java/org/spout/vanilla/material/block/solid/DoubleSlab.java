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

import org.spout.api.entity.Entity;
import org.spout.api.geo.cuboid.Block;
import org.spout.api.inventory.ItemStack;

import org.spout.vanilla.material.Mineable;
import org.spout.vanilla.material.block.Solid;
import org.spout.vanilla.material.block.misc.Slab;
import org.spout.vanilla.material.item.tool.Pickaxe;
import org.spout.vanilla.material.item.tool.Tool;
import org.spout.vanilla.util.VanillaPlayerUtil;

public class DoubleSlab extends Solid implements Mineable {
	public static final DoubleSlab STONE = new DoubleSlab("Stone Double Slab", Slab.STONE);
	public static final DoubleSlab SANDSTONE = new DoubleSlab("Sandstone Double Slab", 1, STONE, Slab.SANDSTONE);
	public static final DoubleSlab WOOD = new DoubleSlab("Wooden Double Slab", 2, STONE, Slab.WOOD);
	public static final DoubleSlab COBBLESTONE = new DoubleSlab("Cobblestone Double Slab", 3, STONE, Slab.COBBLESTONE);
	public static final DoubleSlab BRICK = new DoubleSlab("Brick Double Slab", 4, STONE, Slab.BRICK);
	public static final DoubleSlab STONE_BRICK = new DoubleSlab("Stone Brick Double Slab", 5, STONE, Slab.STONE_BRICK);
	private Slab singletype;

	public Slab getSingleType() {
		return this.singletype;
	}

	public DoubleSlab setSingleType(Slab slab) {
		this.singletype = slab;
		slab.setDoubleType(this);
		return this;
	}

	private DoubleSlab(String name, Slab slab) {
		super(name, 43);
		this.setSingleType(slab).setHardness(2.0F).setResistance(10.0F);
	}

	private DoubleSlab(String name, int data, DoubleSlab parent, Slab slab) {
		super(name, 43, data, parent);
		this.setSingleType(slab).setHardness(2.0F).setResistance(10.0F);
	}

	@Override
	public boolean canBurn() {
		return this == WOOD;
	}

	@Override
	public short getDurabilityPenalty(Tool tool) {
		return tool instanceof Pickaxe ? (short) 1 : (short) 2;
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		ItemStack held = VanillaPlayerUtil.getCurrentItem(block.getSource());
		if (held != null && held.getMaterial() instanceof Pickaxe) {
			drops.add(new ItemStack(this, 1));
		}
		return drops;
	}
}
