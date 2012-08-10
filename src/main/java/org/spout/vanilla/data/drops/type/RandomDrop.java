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
package org.spout.vanilla.data.drops.type;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.spout.api.inventory.ItemStack;
import org.spout.api.material.Material;
import org.spout.vanilla.data.drops.Drop;
import org.spout.vanilla.data.drops.flag.DropFlagSingle;

public class RandomDrop extends Drop {
	private final Material material;
	private final int[] amounts;

	public RandomDrop(Material material, int... amounts) {
		this.material = material;
		this.amounts = amounts;
	}

	public Material getMaterial() {
		return this.material;
	}

	@Override
	public List<ItemStack> getDrops(Random random, Set<DropFlagSingle> flags, List<ItemStack> drops) {
		if (this.canDrop(random, flags)) {
			int amount = amounts[random.nextInt(amounts.length)];
			if (amount > 0) {
				drops.add(new ItemStack(getMaterial(), amount));
			}
		}
		return drops;
	}

	@Override
	public boolean containsDrop(Material material) {
		return getMaterial().isMaterial(material);
	}
}
