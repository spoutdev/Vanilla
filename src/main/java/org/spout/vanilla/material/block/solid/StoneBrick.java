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

import org.spout.vanilla.material.item.tool.Pickaxe;

public class StoneBrick extends Solid {
	public static final StoneBrick STONE = register(new StoneBrick("Stone Brick"));
	public static final StoneBrick MOSSY_STONE = register(new StoneBrick("Mossy Stone Brick", 1, STONE));
	public static final StoneBrick CRACKED_STONE = register(new StoneBrick("Cracked Stone Brick", 2, STONE));

	private StoneBrick(String name) {
		super(name, 98);
	}

	private StoneBrick(String name, int data, StoneBrick parent) {
		super(name, 98, data, parent);
	}

	@Override
	public void initialize() {
		super.initialize();
		this.setHardness(1.5F).setResistance(10.0F);
	}

	@Override
	public ArrayList<ItemStack> getDrops(Block block) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		if (block.getSource() instanceof Entity) {
			if (((Entity) block.getSource()).getInventory().getCurrentItem().getMaterial() instanceof Pickaxe) {
				drops.add(new ItemStack(this, 1));
			}
		}
		return drops;
	}
}
